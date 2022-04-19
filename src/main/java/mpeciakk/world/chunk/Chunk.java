package mpeciakk.world.chunk;

import mpeciakk.util.BlockPos;
import mpeciakk.block.state.BlockState;
import mpeciakk.block.Blocks;
import mpeciakk.render.mesh.ComplexMesh;
import mpeciakk.render.mesh.SimpleMesh;
import mpeciakk.render.mesh.builder.ComplexMeshBuilder;
import mpeciakk.render.mesh.builder.SimpleMeshBuilder;
import mpeciakk.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3i;

public class Chunk {

    public static final int CHUNK_SIZE = 16;

    private final BlockState[][][] blocks = new BlockState[16][256][16];

    private final int x;
    private final int z;
    private final World world;

    private final SimpleMesh simpleBlocksMesh = new SimpleMesh();
    private final ComplexMesh complexBlocksMesh = new ComplexMesh();

    private final Matrix4f transformationMatrix;
    private ChunkMeshState state;
    private BlockPos highlightedBlock = new BlockPos(2137, 2137, 2137);

    public Chunk(int x, int z, World world) {
        this.x = x;
        this.z = z;
        this.world = world;

        this.transformationMatrix = new Matrix4f().setTranslation(x * Chunk.CHUNK_SIZE, 0, z * Chunk.CHUNK_SIZE);
    }

    public void generateChunk() {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    setBlock(x, y, z, Blocks.AIR.getDefaultState());
                }
            }
        }

//        setBlock(0, 0, 0, Blocks.ANVIL.getDefaultState());
//        setBlock(2, 0, 0, Blocks.ANVIL.getDefaultState());
        setBlock(3, 0, 0, Blocks.COBBLESTONE.getDefaultState());
        setBlock(1, 0, 0, Blocks.TEST_BLOCK.getDefaultState());
//
//        for (int x = 0; x < CHUNK_SIZE; x++) {
//            for (int z = 0; z < CHUNK_SIZE; z++) {
//                setBlock(x, (int) world.getNoise().generateHeight(x + this.x * CHUNK_SIZE, z + this.z * CHUNK_SIZE) + 20, z, Blocks.COBBLESTONE);
//            }
//        }

        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    if (world.getNoise().getNoise(x + this.x * CHUNK_SIZE, y, z + this.z * CHUNK_SIZE) > 0.2) {
                        setBlock(x, y - 18, z, Blocks.COBBLESTONE.getDefaultState());
                    }
                }
            }
        }

//        for (int x = 0; x < CHUNK_SIZE; x++) {
//            for (int z = 0; z < CHUNK_SIZE; z++) {
//                setBlock(x, 0, z, Blocks.COBBLESTONE);
//            }
//        }

//        for (int x = 0; x < CHUNK_SIZE; x++) {
//            for (int y = 0; y < 255; y++) {
//                for (int z = 0; z < CHUNK_SIZE; z++) {
//                    if ((x + y + z) % 2 == 0) {
//                        setBlock(x, y, z, Blocks.COBBLESTONE);
//                    }
//                }
//            }
//        }

        setState(ChunkMeshState.REQUESTED_UPDATE);
    }

    public BlockState getBlock(BlockPos position) {
        return getBlock(position.getX(), position.getY(), position.getZ());
    }

    public BlockState getBlock(int x, int y, int z) {
        if (x >= 16 || y >= 256 || z >= 16 || x < 0 || y < 0 || z < 0) {
            return Blocks.AIR.getDefaultState();
        }

        return blocks[x][y][z];
    }

    public void setBlock(int x, int y, int z, BlockState block) {
        // TODO: it should never be negative (by now at least)
        if (y < 0) return;

        blocks[x][y][z] = block;

        setState(ChunkMeshState.REQUESTED_UPDATE);
    }

    public void setBlock(BlockPos pos, BlockState block) {
        setBlock(pos.getX(), pos.getY(), pos.getZ(), block);
    }

    public void updateMesh() {
        SimpleMeshBuilder simpleBlocksMeshBuilder = new SimpleMeshBuilder();
        ComplexMeshBuilder complexBlocksMeshBuilder = new ComplexMeshBuilder();

        for (int bx = 0; bx < 16; bx++) {
            for (int by = 0; by < 256; by++) {
                for (int bz = 0; bz < 16; bz++) {
                    BlockState state = blocks[bx][by][bz];

                    BlockPos localPosition = new BlockPos(bx, by, bz);
                    BlockPos position = new BlockPos(x * Chunk.CHUNK_SIZE, 0, z * Chunk.CHUNK_SIZE).add(localPosition);

                    if (state != null && state.getBlock().hasBlockRenderer()) {
                        if (state.getModel().isComplex()) {
                            state.getBlock().getBlockRenderer().render(complexBlocksMeshBuilder, state, state.getModel(), world, localPosition, position);
                        } else {
                            state.getBlock().getBlockRenderer().render(simpleBlocksMeshBuilder, state, state.getModel(), world, localPosition, position);
                        }
                    }
                }
            }
        }

        simpleBlocksMesh.setVertices(simpleBlocksMeshBuilder.getVertices());
        complexBlocksMesh.setVertices(complexBlocksMeshBuilder.getVertices());

        state = ChunkMeshState.UPDATED;
    }

    public ChunkMeshState getState() {
        return state;
    }

    public void setState(ChunkMeshState state) {
        this.state = state;
    }

    public SimpleMesh getSimpleBlocksMesh() {
        return simpleBlocksMesh;
    }

    public ComplexMesh getComplexBlocksMesh() {
        return complexBlocksMesh;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public World getWorld() {
        return world;
    }

    public Matrix4f getTransformationMatrix() {
        return transformationMatrix;
    }

    public BlockPos getHighlightedBlock() {
        return highlightedBlock;
    }

    public void setHighlightedBlock(BlockPos highlightedBlock) {
        this.highlightedBlock = highlightedBlock;
    }

    @Override
    public String toString() {
        return "Chunk{" +
                "blocks=" + blocks +
                ", x=" + x +
                ", z=" + z +
                ", world=" + world +
                '}';
    }
}