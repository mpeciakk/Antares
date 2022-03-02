package mpeciakk.render.mesh;

import java.util.Arrays;

public class ComplexMesh extends Mesh<Vertex> {

    private VBO verticesBuffer;
    private VBO uvBuffer;

    @Override
    public void initBuffers() {
        verticesBuffer = addVbo(0, 3);
        uvBuffer = addVbo(1, 2);
    }

    @Override
    public ComplexMesh flush() {
        start();

        float[] verticesArray = new float[vertices.size() * 3];
        float[] uvArray = new float[vertices.size() * 2];

        int i = 0;
        int j = 0;
        for (Vertex vertex : vertices) {
            verticesArray[i++] = vertex.getPosition().x;
            verticesArray[i++] = vertex.getPosition().y;
            verticesArray[i++] = vertex.getPosition().z;
            uvArray[j++] = vertex.getTextureCoordinate().x;
            uvArray[j++] = vertex.getTextureCoordinate().y;
        }

        verticesBuffer.flush(getFloatBuffer(verticesArray));
        uvBuffer.flush(getFloatBuffer(uvArray));

        stop();

        return this;
    }
}
