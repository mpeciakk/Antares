package mpeciakk.render.mesh.builder;

import mpeciakk.asset.data.Texture;
import mpeciakk.render.mesh.SimpleMesh;

import java.util.ArrayList;
import java.util.List;

public class SimpleMeshBuilder {
    protected List<Integer> vertices = new ArrayList<>();

    public void drawCuboid(int x, int y, int z, int w, int h, int d, Texture texture, boolean front, boolean back, boolean left, boolean right, boolean bottom, boolean top) {
        int textureId = texture.index();

        y = y + 256;

        if (front) {
            vertices.add(x       | y << 5       | z << 14 | 1 << 19 | textureId << 22);
            vertices.add((x + w) | y << 5       | z << 14 | 2 << 19 | textureId << 22);
            vertices.add((x + w) | (y + h) << 5 | z << 14 | 3 << 19 | textureId << 22);
            vertices.add(x       | (y + h) << 5 | z << 14 | 0 << 19 | textureId << 22);
        }

        if (back) {
            vertices.add(x       | y << 5       | (z + d) << 14 | 1 << 19 | textureId << 22);
            vertices.add((x + w) | y << 5       | (z + d) << 14 | 2 << 19 | textureId << 22);
            vertices.add((x + w) | (y + h) << 5 | (z + d) << 14 | 3 << 19 | textureId << 22);
            vertices.add(x       | (y + h) << 5 | (z + d) << 14 | 0 << 19 | textureId << 22);
        }

        if (left) {
            vertices.add(x | y << 5       | z << 14       | 1 << 19 | textureId << 22);
            vertices.add(x | y << 5       | (z + d) << 14 | 2 << 19 | textureId << 22);
            vertices.add(x | (y + h) << 5 | (z + d) << 14 | 3 << 19 | textureId << 22);
            vertices.add(x | (y + h) << 5 | z << 14       | 0 << 19 | textureId << 22);
        }

        if (right) {
            vertices.add((x + 1) | y << 5       | z << 14       | 1 << 19 | textureId << 22);
            vertices.add((x + 1) | y << 5       | (z + d) << 14 | 2 << 19 | textureId << 22);
            vertices.add((x + 1) | (y + h) << 5 | (z + d) << 14 | 3 << 19 | textureId << 22);
            vertices.add((x + 1) | (y + h) << 5 | z << 14       | 0 << 19 | textureId << 22);
        }

        if (bottom) {
            vertices.add(x       | y << 5 | z << 14       | 1 << 19 | textureId << 22);
            vertices.add((x + w) | y << 5 | z << 14       | 2 << 19 | textureId << 22);
            vertices.add((x + w) | y << 5 | (z + d) << 14 | 3 << 19 | textureId << 22);
            vertices.add(x       | y << 5 | (z + d) << 14 | 0 << 19 | textureId << 22);
        }

        if (top) {
            vertices.add(x       | (y + 1) << 5 | z << 14       | 1 << 19 | textureId << 22);
            vertices.add((x + w) | (y + 1) << 5 | z << 14       | 2 << 19 | textureId << 22);
            vertices.add((x + w) | (y + 1) << 5 | (z + d) << 14 | 3 << 19 | textureId << 22);
            vertices.add(x       | (y + 1) << 5 | (z + d) << 14 | 0 << 19 | textureId << 22);
        }
    }

    public SimpleMesh getMesh() {
        return (SimpleMesh) new SimpleMesh().setVertices(vertices).flush();
    }

    public List<Integer> getVertices() {
        return vertices;
    }
}
