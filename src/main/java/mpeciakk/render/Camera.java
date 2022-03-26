package mpeciakk.render;

import mpeciakk.MinecraftClient;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {

    public static final float FOV = 70;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1000f;

    private final Matrix4f projectionMatrix;
    private final Vector3f position = new Vector3f(0, 0, 0);
    private final float speed = 5f / 32f;
    private final float sensitivity = 0.05f;
    private final Matrix4f viewMatrix = new Matrix4f();
    private float pitch = 0;
    private float yaw = 0;
    private float roll;
    private float currentSpeed = 0;

    public Camera(int width, int height) {
        float aspectRatio = (float) width / height;
        float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(xScale);
        projectionMatrix.m11(yScale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustumLength));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustumLength));
        projectionMatrix.m33(0);
    }

    public void update() {
        if (MinecraftClient.getInstance().getInputManager().isKeyPressed(GLFW.GLFW_KEY_W)) {
            currentSpeed = -speed;
        } else if (MinecraftClient.getInstance().getInputManager().isKeyPressed(GLFW.GLFW_KEY_S)) {
            currentSpeed = speed;
        } else {
            currentSpeed = 0;
        }

        pitch += -MinecraftClient.getInstance().getInputManager().getDeltaMousePos().y * sensitivity;
        yaw += MinecraftClient.getInstance().getInputManager().getDeltaMousePos().x * sensitivity;

        float dx = (float) -(currentSpeed * Math.sin(Math.toRadians(yaw)));
        float dy = (float) (currentSpeed * Math.sin(Math.toRadians(pitch)));
        float dz = (float) (currentSpeed * Math.cos(Math.toRadians(yaw)));

        position.x += dx;
        position.y += dy;
        position.z += dz;

        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0));
        Vector3f cameraPos = position;
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        viewMatrix.translate(negativeCameraPos);
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }
}