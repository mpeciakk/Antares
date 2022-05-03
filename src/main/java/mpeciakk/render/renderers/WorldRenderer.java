package mpeciakk.render.renderers;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;
import mpeciakk.asset.TextureAtlas;
import mpeciakk.light.Light;
import mpeciakk.render.mesh.Vertex;
import mpeciakk.render.mesh.builder.ComplexMeshBuilder;
import mpeciakk.shader.ChunkShader;
import mpeciakk.shader.ComplexShader;
import mpeciakk.util.Destroyable;
import mpeciakk.world.World;
import mpeciakk.world.chunk.Chunk;
import mpeciakk.world.chunk.ChunkMeshState;
import org.joml.Vector3f;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

// So basically it may not be a good idea to have additional shader in this renderer
// it can be split into two sub renderers for each cubic and complex block models (what would make MeshRenderer less complicated (passing shaders to rendering functions))
public class WorldRenderer extends MeshRenderer<World> implements Destroyable {

    private final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

    private final ComplexShader additionalShader;

    private final Light light;

    private float t;

    public WorldRenderer() {
        super(new ChunkShader());
        this.additionalShader = new ComplexShader();

        this.light = new Light(new Vector3f(8, 5, 8), new Vector3f(1, 1, 1));
    }

    @Override
    public void render(World world) {
        double x = 0.1 * Math.cos(5);
        double y = 0;
        double z = 0.1 * Math.sin(5);
        float deltaX = (float) (z * Math.cos(t) - x * Math.sin(t));
        float deltaZ = (float) (x * Math.cos(t) + z * Math.sin(t));

        light.getPosition().add(deltaX, 0, deltaZ);

        t += 0.01;

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
            defaultShader.loadVector("lightColor", light.getColor());
            defaultShader.loadVector("lightPosition", light.getPosition());
            defaultShader.stop();

            additionalShader.start();
            additionalShader.loadTransformationMatrix(chunk.getTransformationMatrix());
            additionalShader.loadVector("highlightedBlock", chunk.getHighlightedBlock().asVector());
            additionalShader.loadVector("lightColor", light.getColor());
            additionalShader.loadVector("lightPosition", light.getPosition());
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
