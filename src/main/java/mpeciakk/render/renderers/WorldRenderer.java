package mpeciakk.render.renderers;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;
import mpeciakk.asset.TextureAtlas;
import mpeciakk.shader.ChunkShader;
import mpeciakk.util.Destroyable;
import mpeciakk.world.World;
import mpeciakk.world.chunk.Chunk;
import mpeciakk.world.chunk.ChunkMeshState;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class WorldRenderer extends MeshRenderer<World> implements Destroyable {

    private final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

    public WorldRenderer() {
        super(new ChunkShader());
    }

    @Override
    public void render(World world) {
        for (Chunk chunk : world.getChunks()) {
            if (chunk.getState() == ChunkMeshState.REQUESTED_UPDATE) {
                chunk.setState(ChunkMeshState.UPDATING);
                threadPool.submit(chunk::updateMesh);
            }

            if (chunk.getState() == ChunkMeshState.UPDATED) {
                chunk.getMesh().flush();

                chunk.setState(ChunkMeshState.NONE);
            }

            shader.start();
            shader.loadTransformationMatrix(chunk.getTransformationMatrix());
            shader.loadVector("highlightedBlock", chunk.getHighlightedBlock().asVector());
            shader.stop();

            if (chunk.getMesh().isFlushed()) {
                render(chunk.getMesh());
            }
        }
    }

    @Override
    protected int bindTexture() {
        return ((TextureAtlas) AssetManager.INSTANCE.get(AssetType.TextureAtlas, "blocks")).getTexture();
    }

    @Override
    public void destroy() {
        threadPool.shutdown();
        threadPool.shutdownNow();
    }
}
