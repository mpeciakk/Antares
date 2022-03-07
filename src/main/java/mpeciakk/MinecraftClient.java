package mpeciakk;

import mpeciakk.asset.AssetLoader;
import mpeciakk.asset.AssetManager;
import mpeciakk.font.FontManager;
import mpeciakk.input.InputManager;
import mpeciakk.registry.Registry;
import mpeciakk.render.GameRenderer;
import mpeciakk.window.Window;
import mpeciakk.world.World;
import mpeciakk.world.block.Block;
import mpeciakk.world.block.Blocks;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;

public class MinecraftClient {

    private static MinecraftClient instance;

    private Window window;
    private InputManager inputManager;

    private World world;
    private FontManager fontManager;
    private GameRenderer gameRenderer;

    private int fps;

    public static MinecraftClient getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        instance = new MinecraftClient();
        instance.start();
    }

    public void start() {
        AssetLoader.INSTANCE.load();

        window = new Window(800, 600, "Antares");
        window.create();

        AssetManager.INSTANCE.stitchTextures();

        Blocks.init();

        inputManager = new InputManager();
        inputManager.start(window);

        world = new World();
        world.generate();

        fontManager = new FontManager();

        gameRenderer = new GameRenderer(this, world);

        loop();

        destroy();
    }

    public void loop() {
        double lastTime = glfwGetTime();
        int nbFrames = 0;

        while (!window.shouldClose()) {
            double currentTime = glfwGetTime();
            nbFrames++;
            if (currentTime - lastTime >= 1.0) {
                fps = nbFrames;
                nbFrames = 0;
                lastTime += 1.0;
            }

            window.update();
            inputManager.update();

            if (inputManager.isKeyPressed(GLFW_KEY_ESCAPE)) {
                window.shouldClose(true);
            }

            if (inputManager.isKeyPressed(GLFW_KEY_Q)) {
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            }

            if (inputManager.isKeyPressed(GLFW_KEY_E)) {
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            }

            gameRenderer.render();
        }
    }

    public void destroy() {
        inputManager.destroy();
        gameRenderer.destroy();
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public FontManager getFontManager() {
        return fontManager;
    }

    public int getFps() {
        return fps;
    }

    public Window getWindow() {
        return window;
    }

    public GameRenderer getGameRenderer() {
        return gameRenderer;
    }
}
