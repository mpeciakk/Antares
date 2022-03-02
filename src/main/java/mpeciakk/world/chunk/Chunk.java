package mpeciakk.world.chunk;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;
import mpeciakk.render.mesh.SimpleMesh;
import mpeciakk.render.mesh.builder.SimpleMeshBuilder;
import mpeciakk.world.World;
import mpeciakk.world.block.Block;
import org.joml.Matrix4f;
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
    private ChunkMeshState state;

    private Matrix4f transformationMatrix;

    public Chunk(int x, int z, World world) {
        this.x = x;
        this.z = z;
        this.world = world;

        this.transformationMatrix = new Matrix4f().setTranslation(x * Chunk.CHUNK_SIZE, 0, z * Chunk.CHUNK_SIZE);
    }

    public void generateChunk() {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                blocks.put(new Vector3i(x, (int) world.getNoise().generateHeight(x + this.x * CHUNK_SIZE, z + this.z * CHUNK_SIZE), z), new Block(1));
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

    public void updateMesh() {
        SimpleMeshBuilder meshBuilder = new SimpleMeshBuilder();

        for (Map.Entry<Vector3i, Block> entry : blocks.entrySet()) {
            Block block = entry.getValue();
            Vector3i position = entry.getKey();
            Vector3i worldPosition = new Vector3i(x * Chunk.CHUNK_SIZE, 0, z * Chunk.CHUNK_SIZE).add(position);

            if (block.getType() == 1) {
                Block northBlock = world.getBlock(new Vector3i(worldPosition.x, worldPosition.y, worldPosition.z).sub(0, 0, 1));
                Block southBlock = world.getBlock(new Vector3i(worldPosition.x, worldPosition.y, worldPosition.z).sub(0, 0, -1));
                Block eastBlock = world.getBlock(new Vector3i(worldPosition.x, worldPosition.y, worldPosition.z).sub(1, 0, 0));
                Block westBlock = world.getBlock(new Vector3i(worldPosition.x, worldPosition.y, worldPosition.z).sub(-1, 0, 0));
                Block upBlock = world.getBlock(new Vector3i(worldPosition.x, worldPosition.y, worldPosition.z).sub(0, 1, 0));
                Block downBlock = world.getBlock(new Vector3i(worldPosition.x, worldPosition.y, worldPosition.z).sub(0, -1, 0));

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