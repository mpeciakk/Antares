package mpeciakk.render.renderers;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;
import mpeciakk.asset.TextureAtlas;
import mpeciakk.shader.ChunkShader;
import mpeciakk.shader.ComplexShader;
import mpeciakk.util.Destroyable;
import mpeciakk.world.World;
import mpeciakk.world.chunk.Chunk;
import mpeciakk.world.chunk.ChunkMeshState;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

// So basically it may not be a good idea to have additional shader in this renderer
// it can be split into two sub renderers for each cubic and complex block models (what would make MeshRenderer less complicated (passing shaders to rendering functions))
public class WorldRenderer extends MeshRenderer<World> implements Destroyable {

    private final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

    private final ComplexShader additionalShader;

    public WorldRenderer() {
        super(new ChunkShader());
        this.additionalShader = new ComplexShader();
    }

    @Override
    public void render(World world) {
        for (Chunk chunk : world.getChunks()) {
            if (chunk.getState() == ChunkMeshState.REQUESTED_UPDATE) {
                chunk.setState(ChunkMeshState.UPDATING);
                threadPool.submit(chunk::updateMesh);
            }

            if (chunk.getState() == ChunkMeshState.UPDATED) {
                chunk.getSimpleBlocksMesh().flush();
                chunk.getComplexBlocksMesh().flush();

                chunk.setState(ChunkMeshState.NONE);
            }

            defaultShader.start();
            defaultShader.loadTransformationMatrix(chunk.getTransformationMatrix());
            defaultShader.loadVector("highlightedBlock", chunk.getHighlightedBlock().asVector());
            defaultShader.stop();

            additionalShader.start();
            additionalShader.loadTransformationMatrix(chunk.getTransformationMatrix());
            additionalShader.loadVector("highlightedBlock", chunk.getHighlightedBlock().asVector());
            additionalShader.stop();

            if (chunk.getSimpleBlocksMesh().isFlushed()) {
                render(chunk.getSimpleBlocksMesh());
            }

            if (chunk.getComplexBlocksMesh().isFlushed()) {
                render(additionalShader, chunk.getComplexBlocksMesh());
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
