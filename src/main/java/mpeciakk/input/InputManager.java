package mpeciakk.input;

import mpeciakk.window.Window;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;

public class InputManager {

    private final boolean[] keys = new boolean[GLFW_KEY_LAST];
    private final boolean[] buttons = new boolean[GLFW_MOUSE_BUTTON_LAST];

    private final Vector2f mousePos = new Vector2f();
    private final Vector2f prevMousePos = new Vector2f();
    private final Vector2f deltaMousePos = new Vector2f();

    private GLFWKeyCallback keyCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWCursorPosCallback mousePosCallback;

    public void start(Window window) {
        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keys[key] = (action != GLFW_RELEASE);
            }
        };

        mousePosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                mousePos.x = (float) x;
                mousePos.y = (float) y;
            }
        };

        mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                buttons[button] = (action != GLFW_RELEASE);
            }
        };

        glfwSetKeyCallback(window.getId(), keyCallback);
        glfwSetMouseButtonCallback(window.getId(), mouseButtonCallback);
        glfwSetCursorPosCallback(window.getId(), mousePosCallback);
    }

    public void update() {
        deltaMousePos.x = mousePos.x - prevMousePos.x;
        deltaMousePos.y = prevMousePos.y - mousePos.y;

        prevMousePos.x = mousePos.x;
        prevMousePos.y = mousePos.y;
    }

    public boolean isKeyPressed(int key) {
        return keys[key];
    }

    public boolean isButtonPressed(int button) {
        return buttons[button];
    }

    public Vector2f getMousePos() {
        return mousePos;
    }

    public Vector2f getDeltaMousePos() {
        return deltaMousePos;
    }

    public void destroy() {
        keyCallback.free();
        mouseButtonCallback.free();
        mousePosCallback.free();
    }
}