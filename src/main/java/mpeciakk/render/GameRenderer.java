package mpeciakk.render;

import mpeciakk.MinecraftClient;
import mpeciakk.block.Blocks;
import mpeciakk.render.renderers.TextRenderer;
import mpeciakk.render.renderers.WorldRenderer;
import mpeciakk.util.BlockPos;
import mpeciakk.util.Destroyable;
import mpeciakk.util.Raycaster;
import mpeciakk.world.World;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class GameRenderer implements Destroyable {

    private final MinecraftClient client;
    private final World world;

    private final Camera camera;

    private final TextRenderer textRenderer;
    private final WorldRenderer worldRenderer;

    private final Raycaster raycaster;

    private float timer;

    private BlockPos selectedBlock = new BlockPos(0, 0, 0);

    public GameRenderer(MinecraftClient client, World world) {
        this.client = client;
        this.world = world;

        this.camera = new Camera(client.getWindow().getWidth(), client.getWindow().getHeight());

        this.textRenderer = new TextRenderer(client.getFontManager());
        this.worldRenderer = new WorldRenderer();

        this.raycaster = new Raycaster();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void render() {
        camera.update();

        glClearColor(0, 0, 1.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        worldRenderer.render(world);

//        textRenderer.render(String.valueOf(client.getFps()), 0, 0);

        Vector3f dir = new Vector3f();
        dir = camera.getViewMatrix().positiveZ(dir).negate();

        Raycaster.RaycastHit hit = raycaster.ray(world, camera.getPosition(), dir, 640);

        if (hit != null && hit.block() != Blocks.AIR.getDefaultState()) {
            world.getChunk(selectedBlock.getX() >> 4, selectedBlock.getZ() >> 4).setHighlightedBlock(new BlockPos(2137, 2137, 2137));

            selectedBlock = new BlockPos(hit.blockPos().getX(), hit.blockPos().getY(), hit.blockPos().getZ());

            world.getChunk(selectedBlock.getX() >> 4, selectedBlock.getZ() >> 4).setHighlightedBlock(new BlockPos(selectedBlock.getX() % 16, selectedBlock.getY() % 256, selectedBlock.getZ() % 16));
        } else {
            world.getChunk(selectedBlock.getX() >> 4, selectedBlock.getZ() >> 4).setHighlightedBlock(new BlockPos(2137, 2137, 2137));
        }

        if (client.getInputManager().isButtonPressed(1) && timer == 0) {
            world.setBlock(selectedBlock.offset(hit.face()), Blocks.ANVIL.getDefaultState());
            timer = 50;
        }

        if (client.getInputManager().isButtonPressed(0) && timer == 0) {
            world.setBlock(selectedBlock, Blocks.AIR.getDefaultState());
            timer = 50;
        }

        if (timer > 0) {
            timer -= 1;
        }
    }

    @Override
    public void destroy() {
        worldRenderer.destroy();
        world.destroy();
    }

    public Camera getCamera() {
        return camera;
    }
}
