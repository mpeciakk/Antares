package mpeciakk.render.mesh.builder;

import mpeciakk.asset.data.Texture;
import mpeciakk.render.mesh.SimpleMesh;

public class SimpleMeshBuilder extends MeshBuilder<Integer> {

    public void drawCuboid(int x, int y, int z, int w, int h, int d, Texture textureFront, Texture textureBack, Texture textureLeft, Texture textureRight, Texture textureBottom, Texture textureTop, boolean front, boolean back, boolean left, boolean right, boolean bottom, boolean top) {
        int textureIdFront = textureFront.index();
        int textureIdBack = textureBack.index();
        int textureIdLeft = textureLeft.index();
        int textureIdRight = textureRight.index();
        int textureIdBottom = textureBottom.index();
        int textureIdTop = textureTop.index();

        if (front) {
            vertices.add(x       | y << 5       | z << 14 | 1 << 19 | 0 << 22 | textureIdFront << 25);
            vertices.add((x + w) | y << 5       | z << 14 | 2 << 19 | 0 << 22 | textureIdFront << 25);
            vertices.add((x + w) | (y + h) << 5 | z << 14 | 3 << 19 | 0 << 22 | textureIdFront << 25);
            vertices.add(x       | (y + h) << 5 | z << 14 | 0 << 19 | 0 << 22 | textureIdFront << 25);
        }

        if (back) {
            vertices.add(x       | y << 5       | (z + d) << 14 | 1 << 19 | 1 << 22 | textureIdBack << 25);
            vertices.add((x + w) | y << 5       | (z + d) << 14 | 2 << 19 | 1 << 22 | textureIdBack << 25);
            vertices.add((x + w) | (y + h) << 5 | (z + d) << 14 | 3 << 19 | 1 << 22 | textureIdBack << 25);
            vertices.add(x       | (y + h) << 5 | (z + d) << 14 | 0 << 19 | 1 << 22 | textureIdBack << 25);
        }

        if (left) {
            vertices.add(x | y << 5       | z << 14       | 1 << 19 | 2 << 22 | textureIdLeft << 25);
            vertices.add(x | y << 5       | (z + d) << 14 | 2 << 19 | 2 << 22 | textureIdLeft << 25);
            vertices.add(x | (y + h) << 5 | (z + d) << 14 | 3 << 19 | 2 << 22 | textureIdLeft << 25);
            vertices.add(x | (y + h) << 5 | z << 14       | 0 << 19 | 2 << 22 | textureIdLeft << 25);
        }

        if (right) {
            vertices.add((x + 1) | y << 5       | z << 14       | 1 << 19 | 3 << 22 | textureIdRight << 25);
            vertices.add((x + 1) | y << 5       | (z + d) << 14 | 2 << 19 | 3 << 22 | textureIdRight << 25);
            vertices.add((x + 1) | (y + h) << 5 | (z + d) << 14 | 3 << 19 | 3 << 22 | textureIdRight << 25);
            vertices.add((x + 1) | (y + h) << 5 | z << 14       | 0 << 19 | 3 << 22 | textureIdRight << 25);
        }

        if (bottom) {
            vertices.add(x       | y << 5 | z << 14       | 1 << 19 | 4 << 22 | textureIdBottom << 25);
            vertices.add((x + w) | y << 5 | z << 14       | 2 << 19 | 4 << 22 | textureIdBottom << 25);
            vertices.add((x + w) | y << 5 | (z + d) << 14 | 3 << 19 | 4 << 22 | textureIdBottom << 25);
            vertices.add(x       | y << 5 | (z + d) << 14 | 0 << 19 | 4 << 22 | textureIdBottom << 25);
        }

        if (top) {
            vertices.add(x       | (y + 1) << 5 | z << 14       | 1 << 19 | 5 << 22 | textureIdTop << 25);
            vertices.add((x + w) | (y + 1) << 5 | z << 14       | 2 << 19 | 5 << 22 | textureIdTop << 25);
            vertices.add((x + w) | (y + 1) << 5 | (z + d) << 14 | 3 << 19 | 5 << 22 | textureIdTop << 25);
            vertices.add(x       | (y + 1) << 5 | (z + d) << 14 | 0 << 19 | 5 << 22 | textureIdTop << 25);
        }
    }

    @Override
    public SimpleMesh build() {
        return (SimpleMesh) new SimpleMesh().setVertices(vertices).flush();
    }
}
