package mpeciakk;

import mpeciakk.asset.AssetLoader;
import mpeciakk.block.Blocks;
import mpeciakk.debug.DebugTools;
import mpeciakk.input.InputManager;
import mpeciakk.render.GameRenderer;
import mpeciakk.window.Window;
import mpeciakk.world.World;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class MinecraftClient {

    private static MinecraftClient instance;

    private final DebugTools debugTools = new DebugTools(this);

    private Window window;
    private InputManager inputManager;

    private World world;
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
        window = new Window(800, 600, "Antares");
        window.create();

        AssetLoader.INSTANCE.load();

        Blocks.init();

        inputManager = new InputManager();
        inputManager.start(window);

        world = new World();
        world.generate();

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

            gameRenderer.render();

            debugTools.update();
        }
    }

    public void destroy() {
        inputManager.destroy();
        gameRenderer.destroy();
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public int getFps() {
        return fps;
    }

    public Window getWindow() {
        return window;
    }

    public World getWorld() {
        return world;
    }

    public GameRenderer getGameRenderer() {
        return gameRenderer;
    }
}
