package mpeciakk.render.mesh;

import java.nio.IntBuffer;

public class SimpleMesh extends Mesh<Integer> {
    private VBO verticesBuffer;

    @Override
    public void initBuffers() {
        verticesBuffer = addVbo(0, 1);
    }

    @Override
    public SimpleMesh flush() {
        start();

        IntBuffer buffer = getIntBuffer(vertices.stream().mapToInt(i -> i).toArray());

        verticesBuffer.flush(buffer);

        buffer.clear();

        stop();

        flushed = true;

        System.out.println("nietl");

        return this;
    }
}
