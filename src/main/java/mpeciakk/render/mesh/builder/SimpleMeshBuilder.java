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

        y = y + 256;

        if (front) {
            vertices.add(x       | y << 5       | z << 14 | 1 << 19 | textureIdFront << 22);
            vertices.add((x + w) | y << 5       | z << 14 | 2 << 19 | textureIdFront << 22);
            vertices.add((x + w) | (y + h) << 5 | z << 14 | 3 << 19 | textureIdFront << 22);
            vertices.add(x       | (y + h) << 5 | z << 14 | 0 << 19 | textureIdFront << 22);
        }

        if (back) {
            vertices.add(x       | y << 5       | (z + d) << 14 | 1 << 19 | textureIdBack << 22);
            vertices.add((x + w) | y << 5       | (z + d) << 14 | 2 << 19 | textureIdBack << 22);
            vertices.add((x + w) | (y + h) << 5 | (z + d) << 14 | 3 << 19 | textureIdBack << 22);
            vertices.add(x       | (y + h) << 5 | (z + d) << 14 | 0 << 19 | textureIdBack << 22);
        }

        if (left) {
            vertices.add(x | y << 5       | z << 14       | 1 << 19 | textureIdLeft << 22);
            vertices.add(x | y << 5       | (z + d) << 14 | 2 << 19 | textureIdLeft << 22);
            vertices.add(x | (y + h) << 5 | (z + d) << 14 | 3 << 19 | textureIdLeft << 22);
            vertices.add(x | (y + h) << 5 | z << 14       | 0 << 19 | textureIdLeft << 22);
        }

        if (right) {
            vertices.add((x + 1) | y << 5       | z << 14       | 1 << 19 | textureIdRight << 22);
            vertices.add((x + 1) | y << 5       | (z + d) << 14 | 2 << 19 | textureIdRight << 22);
            vertices.add((x + 1) | (y + h) << 5 | (z + d) << 14 | 3 << 19 | textureIdRight << 22);
            vertices.add((x + 1) | (y + h) << 5 | z << 14       | 0 << 19 | textureIdRight << 22);
        }

        if (bottom) {
            vertices.add(x       | y << 5 | z << 14       | 1 << 19 | textureIdBottom << 22);
            vertices.add((x + w) | y << 5 | z << 14       | 2 << 19 | textureIdBottom << 22);
            vertices.add((x + w) | y << 5 | (z + d) << 14 | 3 << 19 | textureIdBottom << 22);
            vertices.add(x       | y << 5 | (z + d) << 14 | 0 << 19 | textureIdBottom << 22);
        }

        if (top) {
            vertices.add(x       | (y + 1) << 5 | z << 14       | 1 << 19 | textureIdTop << 22);
            vertices.add((x + w) | (y + 1) << 5 | z << 14       | 2 << 19 | textureIdTop << 22);
            vertices.add((x + w) | (y + 1) << 5 | (z + d) << 14 | 3 << 19 | textureIdTop << 22);
            vertices.add(x       | (y + 1) << 5 | (z + d) << 14 | 0 << 19 | textureIdTop << 22);
        }
    }

    @Override
    public SimpleMesh build() {
        return (SimpleMesh) new SimpleMesh().setVertices(vertices).flush();
    }
}
