package mpeciakk.render;

import mpeciakk.MinecraftClient;
import mpeciakk.render.renderers.WorldRenderer;
import mpeciakk.render.renderers.ModelRenderer;
import mpeciakk.render.renderers.TextRenderer;
import mpeciakk.world.World;

import static org.lwjgl.opengl.GL11.*;

public class GameRenderer {

    private final MinecraftClient client;
    private final World world;

    private final Camera camera;

    private final ModelRenderer modelRenderer;
    private final TextRenderer textRenderer;
    private final WorldRenderer chunkRenderer;

    public GameRenderer(MinecraftClient client, World world) {
        this.client = client;
        this.world = world;

        camera = new Camera(client.getWindow().getWidth(), client.getWindow().getHeight());

        modelRenderer = new ModelRenderer();
        textRenderer = new TextRenderer(client.getFontManager());
        chunkRenderer = new WorldRenderer();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void render() {
        camera.update();

        glClearColor(0, 0, 1.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        chunkRenderer.render(world);

        textRenderer.render(String.valueOf(client.getFps()), 0, 0);
    }

    public void destroy() {
        chunkRenderer.destroy();
    }

    public Camera getCamera() {
        return camera;
    }
}
