package mpeciakk.render.renderers;

import mpeciakk.MinecraftClient;
import mpeciakk.render.mesh.Mesh;
import mpeciakk.shader.Shader;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public abstract class MeshRenderer<T> {

    protected final Shader defaultShader;

    public MeshRenderer(Shader shader) {
        this.defaultShader = shader;
    }

    public void preRender(Shader shader, Mesh<?> mesh) {
        shader.start();
        glBindTexture(GL_TEXTURE_2D, bindTexture());
        glBindVertexArray(mesh.getVao());

        for (int i = 0; i < mesh.getVbosCount(); i++) {
            glEnableVertexAttribArray(i);
        }
    }

    public abstract void render(T t);
    
    public void render(Mesh<?> mesh) {
        render(defaultShader, mesh);
    }
    
    public void render(Shader shader, Mesh<?> mesh) {
        preRender(shader, mesh);

        shader.loadProjectionMatrix(MinecraftClient.getInstance().getGameRenderer().getCamera().getProjectionMatrix());
        shader.loadViewMatrix(MinecraftClient.getInstance().getGameRenderer().getCamera().getViewMatrix());

        glDrawArrays(GL_QUADS, 0, mesh.getVerticesCount());

        postRender(shader, mesh);
    }

    public void postRender(Shader shader, Mesh<?> mesh) {
        for (int i = 0; i < mesh.getVbosCount(); i++) {
            glDisableVertexAttribArray(i);
        }

        glBindVertexArray(0);
        shader.stop();
    }

    protected abstract int bindTexture();
}