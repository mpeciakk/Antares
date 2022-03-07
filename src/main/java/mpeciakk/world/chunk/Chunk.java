package mpeciakk.world.chunk;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;
import mpeciakk.asset.data.Texture;
import mpeciakk.render.mesh.SimpleMesh;
import mpeciakk.render.mesh.builder.SimpleMeshBuilder;
import mpeciakk.util.Direction;
import mpeciakk.world.World;
import mpeciakk.world.block.Block;
import mpeciakk.world.block.BlockModel;
import mpeciakk.world.block.BlockPos;
import mpeciakk.world.block.Blocks;
import org.joml.Matrix4f;
import org.joml.Vector3i;

public class Chunk {

    public static final int CHUNK_SIZE = 16;

    private final Block[][][] blocks = new Block[16][256][16];

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
                    setBlock(x, y, z, Blocks.AIR);
                }
            }
        }

//        for (int x = 0; x < CHUNK_SIZE; x++) {
//            for (int z = 0; z < CHUNK_SIZE; z++) {
//                setBlock(x, (int) world.getNoise().generateHeight(x + this.x * CHUNK_SIZE, z + this.z * CHUNK_SIZE) + 20, z, Blocks.COBBLESTONE);
//            }
//        }

        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    if (world.getNoise().getNoise(x + this.x * CHUNK_SIZE, y, z + this.z * CHUNK_SIZE) > 0.4) {
                        setBlock(x, y - 18, z, Blocks.COBBLESTONE);
                    }
                }
            }
        }

//        for (int x = 0; x < CHUNK_SIZE; x++) {
//            for (int z = 0; z < CHUNK_SIZE; z++) {
//                setBlock(x, 0, z, Blocks.COBBLESTONE);
//            }
//        }
    }

    public Block getBlock(Vector3i position) {
        return getBlock(position.x, position.y, position.z);
    }

    public Block getBlock(int x, int y, int z) {
        return blocks[x][y][z];
    }

    public void setBlock(int x, int y, int z, Block block) {
        // TODO: it should never be negative (by now at least)
        if (y < 0) return;

        blocks[x][y][z] = block;

        setState(ChunkMeshState.REQUESTED_UPDATE);
    }

    public void setBlock(BlockPos pos, Block block) {
        setBlock(pos.getX(), pos.getY(), pos.getZ(), block);
    }

    public void updateMesh() {
        SimpleMeshBuilder meshBuilder = new SimpleMeshBuilder();

        for (int bx = 0; bx < 16; bx++) {
            for (int by = 0; by < 256; by++) {
                for (int bz = 0; bz < 16; bz++) {
                    Block block = blocks[bx][by][bz];

                    Vector3i position = new Vector3i(bx, by, bz);
                    Vector3i worldPosition = new Vector3i(x * Chunk.CHUNK_SIZE, 0, z * Chunk.CHUNK_SIZE).add(position);

                    if (block != null && block != Blocks.AIR) {
                        Block northBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.NORTH));
                        Block southBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.SOUTH));
                        Block eastBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.EAST));
                        Block westBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.WEST));
                        Block upBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.UP));
                        Block downBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.DOWN));

                        BlockModel model = block.getModel();

                        Texture front = model.getTextures().get("front");
                        Texture back = model.getTextures().get("back");
                        Texture left = model.getTextures().get("left");
                        Texture right = model.getTextures().get("right");
                        Texture bottom = model.getTextures().get("bottom");
                        Texture top = model.getTextures().get("top");

                        meshBuilder.drawCuboid(position.x, position.y, position.z, 1, 1, 1, front, back, left, right, bottom, top, northBlock == null || !northBlock.isFull(), southBlock == null || !southBlock.isFull(), eastBlock == null || !eastBlock.isFull(), westBlock == null || !westBlock.isFull(), downBlock == null || !downBlock.isFull(), upBlock == null || !upBlock.isFull());
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