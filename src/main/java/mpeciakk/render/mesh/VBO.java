package mpeciakk.render.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class VBO {
    private final int id;
    private final int attributeNumber;
    private final int size;

    public VBO(int attributeNumber, int size) {
        this.id = glGenBuffers();
        this.attributeNumber = attributeNumber;
        this.size = size;
    }

    public void flush(FloatBuffer data) {
        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, size, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        data.clear();
    }

    public void flush(IntBuffer data) {
        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, size, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        data.clear();
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "VBO{" +
                "id=" + id +
                ", attributeNumber=" + attributeNumber +
                ", size=" + size +
                '}';
    }
}
