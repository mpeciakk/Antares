package mpeciakk.debug;

import mpeciakk.MinecraftClient;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;

public class DebugTools {

    public static boolean naive = true;

    private final MinecraftClient client;

    private boolean wireframe;

    private float timer;

    public DebugTools(MinecraftClient client) {
        this.client = client;
    }

    public void update() {
        if (client.getInputManager().isKeyPressed(GLFW_KEY_F1) && timer == 0) {
            if (wireframe) {
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                wireframe = false;
            } else {
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                wireframe = true;
            }

            timer = 50;
        }

        if (client.getInputManager().isKeyPressed(GLFW_KEY_F2) && timer == 0) {
            naive = !naive;

            client.getWorld().regenerateMesh();

            timer = 50;
        }

        if (timer > 0) {
            timer -= 1;
        }
    }
}
