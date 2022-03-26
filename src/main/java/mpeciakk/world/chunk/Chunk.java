package mpeciakk.world.chunk;

import mpeciakk.asset.data.Texture;
import mpeciakk.block.*;
import mpeciakk.debug.DebugTools;
import mpeciakk.render.mesh.SimpleMesh;
import mpeciakk.render.mesh.builder.SimpleMeshBuilder;
import mpeciakk.util.Direction;
import mpeciakk.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3i;

public class Chunk {

    public static final int CHUNK_SIZE = 16;

    private final BlockState[][][] blocks = new BlockState[16][256][16];

    private final int x;
    private final int z;
    private final World world;

    private final SimpleMesh mesh = new SimpleMesh();
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

        setBlock(0, 0, 0, Blocks.TEST_BLOCK.getBlockStateBuilder().with(TestBlock.property1, false).with(TestBlock.property2, false).with(TestBlock.property3, false).get());
        setBlock(1, 0, 0, Blocks.TEST_BLOCK.getBlockStateBuilder().with(TestBlock.property1, true).with(TestBlock.property2, true).with(TestBlock.property3, true).get());

//        for (int x = 0; x < CHUNK_SIZE; x++) {
//            for (int z = 0; z < CHUNK_SIZE; z++) {
//                setBlock(x, (int) world.getNoise().generateHeight(x + this.x * CHUNK_SIZE, z + this.z * CHUNK_SIZE) + 20, z, Blocks.COBBLESTONE);
//            }
//        }

//        for (int x = 0; x < CHUNK_SIZE; x++) {
//            for (int y = 0; y < 256; y++) {
//                for (int z = 0; z < CHUNK_SIZE; z++) {
//                    if (world.getNoise().getNoise(x + this.x * CHUNK_SIZE, y, z + this.z * CHUNK_SIZE) > 0.2) {
//                        setBlock(x, y - 18, z, Blocks.COBBLESTONE.getDefaultState());
//                    }
//                }
//            }
//        }

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

    public BlockState getBlock(Vector3i position) {
        return getBlock(position.x, position.y, position.z);
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
        SimpleMeshBuilder meshBuilder = new SimpleMeshBuilder();

        for (int bx = 0; bx < 16; bx++) {
            for (int by = 0; by < 256; by++) {
                for (int bz = 0; bz < 16; bz++) {
                    BlockState block = blocks[bx][by][bz];

                    Vector3i position = new Vector3i(bx, by, bz);
                    Vector3i worldPosition = new Vector3i(x * Chunk.CHUNK_SIZE, 0, z * Chunk.CHUNK_SIZE).add(position);

                    if (block != null && block != Blocks.AIR.getDefaultState()) {
                        BlockState northBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.NORTH));
                        BlockState southBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.SOUTH));
                        BlockState eastBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.EAST));
                        BlockState westBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.WEST));
                        BlockState upBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.UP));
                        BlockState downBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.DOWN));

                        BlockModel model = block.getModel();

                        Texture front = model.getTextures().get("front");
                        Texture back = model.getTextures().get("back");
                        Texture left = model.getTextures().get("left");
                        Texture right = model.getTextures().get("right");
                        Texture bottom = model.getTextures().get("bottom");
                        Texture top = model.getTextures().get("top");

                        if (DebugTools.naive) {
                            meshBuilder.drawCuboid(position.x, position.y, position.z, 1, 1, 1, front, back, left, right, bottom, top, northBlock == null || !northBlock.getModel().isFull(), southBlock == null || !southBlock.getModel().isFull(), eastBlock == null || !eastBlock.getModel().isFull(), westBlock == null || !westBlock.getModel().isFull(), downBlock == null || !downBlock.getModel().isFull(), upBlock == null || !upBlock.getModel().isFull());
                        } else {
                            meshBuilder.drawCuboid(position.x, position.y, position.z, 1, 1, 1, front, back, left, right, bottom, top, true, true, true, true, true, true);
                        }
                    }
                }
            }
        }

        mesh.setVertices(meshBuilder.getVertices());

        state = ChunkMeshState.UPDATED;
    }

    public ChunkMeshState getState() {
        return state;
    }

    public void setState(ChunkMeshState state) {
        this.state = state;
    }

    public SimpleMesh getMesh() {
        return mesh;
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