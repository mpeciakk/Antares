package mpeciakk.render.mesh;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public abstract class Mesh<T> {

    protected final int vao;
    private final List<VBO> vbos = new ArrayList<>();
    protected List<T> vertices = new ArrayList<>();
    protected boolean flushed;

    public Mesh() {
        this.vao = glGenVertexArrays();

        initBuffers();
    }

    public abstract void initBuffers();

    public abstract Mesh<T> flush();

    public VBO addVbo(int attributeNumber, int size) {
        VBO vbo = new VBO(attributeNumber, size);
        vbos.add(vbo);
        return vbo;
    }

    public void destroy() {
        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        for (VBO vbo : vbos) {
            glDeleteBuffers(vbo.getId());
        }

        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
    }

    public void clear() {
        vertices.clear();
    }

    public void start() {
        glBindVertexArray(vao);
    }

    public void stop() {
        glBindVertexArray(0);
    }

    public int getVerticesCount() {
        return vertices.size();
    }

    public int getVbosCount() {
        return vbos.size();
    }

    public int getVao() {
        return vao;
    }

    public Mesh<T> setVertices(List<T> vertices) {
        this.vertices = vertices;

        return this;
    }

    public boolean isFlushed() {
        return flushed;
    }

    protected IntBuffer getIntBuffer(int[] data) {
        return BufferUtils.createIntBuffer(data.length)
                .put(data)
                .flip();
    }

    protected FloatBuffer getFloatBuffer(float[] data) {
        return BufferUtils.createFloatBuffer(data.length)
                .put(data)
                .flip();
    }

    @Override
    public String toString() {
        return "Mesh{" +
                "vertices=" + vertices +
                ", vbos=" + vbos +
                ", vao=" + vao +
                '}';
    }
}
