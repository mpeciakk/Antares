package mpeciakk.world.chunk;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;
import mpeciakk.render.mesh.SimpleMesh;
import mpeciakk.render.mesh.builder.SimpleMeshBuilder;
import mpeciakk.util.Direction;
import mpeciakk.world.World;
import mpeciakk.world.block.Block;
import mpeciakk.world.block.BlockPos;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.Map;

public class Chunk {

    public static final int CHUNK_SIZE = 16;

    private final Map<Vector3i, Block> blocks = new HashMap<>();

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
            for (int z = 0; z < CHUNK_SIZE; z++) {
                setBlock(x, (int) world.getNoise().generateHeight(x + this.x * CHUNK_SIZE, z + this.z * CHUNK_SIZE), z, new Block(1));
            }
        }
    }

    public Block getBlock(Vector3i position) {
        return blocks.get(position);
    }

    public Block getBlock(int x, int y, int z) {
        return getBlock(new Vector3i(x, y, z));
    }

    public void setBlock(int x, int y, int z, Block block) {
        if (blocks.containsKey(new Vector3i(x, y, z))) {
            blocks.replace(new Vector3i(x, y, z), block);
        } else {
            blocks.put(new Vector3i(x, y, z), block);
        }

        setState(ChunkMeshState.REQUESTED_UPDATE);
    }

    public void setBlock(BlockPos pos, Block block) {
        setBlock(pos.getX(), pos.getY(), pos.getZ(), block);
    }

    public void updateMesh() {
        SimpleMeshBuilder meshBuilder = new SimpleMeshBuilder();

        for (Map.Entry<Vector3i, Block> entry : blocks.entrySet()) {
            Block block = entry.getValue();
            Vector3i position = entry.getKey();
            Vector3i worldPosition = new Vector3i(x * Chunk.CHUNK_SIZE, 0, z * Chunk.CHUNK_SIZE).add(position);

            if (block.getType() == 1) {
                Block northBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.NORTH));
                Block southBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.SOUTH));
                Block eastBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.EAST));
                Block westBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.WEST));
                Block upBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.UP));
                Block downBlock = world.getBlock(new BlockPos(worldPosition.x, worldPosition.y, worldPosition.z).offset(Direction.DOWN));

                meshBuilder.drawCuboid(position.x, position.y, position.z, 1, 1, 1, AssetManager.INSTANCE.get(AssetType.Texture, "cobblestone"), northBlock == null || northBlock.getType() == 0, southBlock == null || southBlock.getType() == 0, eastBlock == null || eastBlock.getType() == 0, westBlock == null || westBlock.getType() == 0, downBlock == null || downBlock.getType() == 0, upBlock == null || upBlock.getType() == 0);
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

    public Map<Vector3i, Block> getBlocks() {
        return blocks;
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