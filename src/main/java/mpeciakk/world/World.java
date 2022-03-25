package mpeciakk.world;

import de.articdive.jnoise.JNoise;
import mpeciakk.block.Block;
import mpeciakk.block.BlockPos;
import mpeciakk.block.Blocks;
import mpeciakk.world.chunk.Chunk;
import mpeciakk.world.chunk.ChunkMeshState;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class World {

    private final List<Chunk> chunks = new ArrayList<>();
    private final JNoise noise = JNoise.newBuilder().perlin().setFrequency(0.01).build();

    private final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);

    public World() {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunks.add(new Chunk(x, z, this));
            }
        }
    }

    public void generate() {
        for (Chunk chunk : chunks) {
            threadPool.submit(chunk::generateChunk);
        }
    }

    public void regenerateMesh() {
        for (Chunk chunk : chunks) {
            chunk.setState(ChunkMeshState.REQUESTED_UPDATE);
        }
    }

    public Block getBlock(BlockPos position) {
        int x = position.getX() >> 4;
        int z = position.getZ() >> 4;

        Chunk chunk = getChunk(x, z);

        int blockX = position.getX() - x * Chunk.CHUNK_SIZE;
        int blockY = position.getY();
        int blockZ = position.getZ() - z * Chunk.CHUNK_SIZE;

        if (chunk == null || blockY < 0) {
            return Blocks.AIR;
        }

        return chunk.getBlock(blockX, blockY, blockZ);
    }

    public void setBlock(Vector3i position, Block block) {
        int x = position.x >> 4;
        int z = position.z >> 4;

        Chunk chunk = getChunk(x, z);

        int blockX = position.x - x * Chunk.CHUNK_SIZE;
        int blockY = position.y;
        int blockZ = position.z - z * Chunk.CHUNK_SIZE;

        if (chunk == null) {
            return;
        }

        chunk.setBlock(blockX, blockY, blockZ, block);
    }

    public Chunk getChunk(int x, int z) {
        int size = chunks.size();

        for (int i = 0; i < size; i++) {
            Chunk chunk = chunks.get(i);

            if (chunk.getX() == x && chunk.getZ() == z) {
                return chunk;
            }
        }

        return null;
    }

    public List<Chunk> getChunks() {
        return chunks;
    }

    public JNoise getNoise() {
        return noise;
    }
}