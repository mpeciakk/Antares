package mpeciakk.model;

import mpeciakk.render.mesh.ComplexMesh;
import mpeciakk.render.mesh.builder.ComplexMeshBuilder;
import mpeciakk.render.mesh.Vertex;
import mpeciakk.asset.data.Texture;
import org.joml.Matrix4f;

import java.util.Map;

public class ModelPart {

    private final Matrix4f transformationMatrix = new Matrix4f().identity();

    private final String name;

//    private final float[] textureCoords;

    private Texture texture;
    private ComplexMesh mesh;

    public ModelPart(float[] from, float[] to, String name, Map<String, JsonModel.Element.Face> faces, Texture texture) {
        ComplexMeshBuilder meshBuilder = new ComplexMeshBuilder();
        meshBuilder.drawCuboid(new Vertex(from[0], from[1], from[2], 0, 0, 0, 0, 0), new Vertex(to[0], to[1], to[2], 0, 0, 0, 0, 0));
        mesh = meshBuilder.getMesh().flush();

        this.name = name;

        float[] north = faces.get("north").getUv();
        float[] east = faces.get("east").getUv();
        float[] south = faces.get("south").getUv();
        float[] west = faces.get("west").getUv();
        float[] up = faces.get("up").getUv();
        float[] down = faces.get("down").getUv();

//        textureCoords = new float[]{
//                north[2], (texture.getOffset() / 4f) + north[3],
//                north[0], (texture.getOffset() / 4f) + north[3],
//                north[2], (texture.getOffset() / 4f) + north[1],
//                north[0], (texture.getOffset() / 4f) + north[1],
//
//                east[2], (texture.getOffset() / 4f) + east[3],
//                east[0], (texture.getOffset() / 4f) + east[3],
//                east[2], (texture.getOffset() / 4f) + east[1],
//                east[0], (texture.getOffset() / 4f) + east[1],
//
//                south[2], (texture.getOffset() / 4f) + south[3],
//                south[0], (texture.getOffset() / 4f) + south[3],
//                south[2], (texture.getOffset() / 4f) + south[1],
//                south[0], (texture.getOffset() / 4f) + south[1],
//
//                west[2], (texture.getOffset() / 4f) + west[3],
//                west[0], (texture.getOffset() / 4f) + west[3],
//                west[2], (texture.getOffset() / 4f) + west[1],
//                west[0], (texture.getOffset() / 4f) + west[1],
//
//                up[2], (texture.getOffset() / 4f) + up[3],
//                up[0], (texture.getOffset() / 4f) + up[3],
//                up[2], (texture.getOffset() / 4f) + up[1],
//                up[0], (texture.getOffset() / 4f) + up[1],
//
//                down[2], (texture.getOffset() / 4f) + down[3],
//                down[0], (texture.getOffset() / 4f) + down[3],
//                down[2], (texture.getOffset() / 4f) + down[1],
//                down[0], (texture.getOffset() / 4f) + down[1]
//        };
    }

    public void rotate(float x, float y, float z) {
        transformationMatrix.rotateXYZ(x, y, z);
    }

    public Matrix4f getTransformationMatrix() {
        return transformationMatrix;
    }

    public String getName() {
        return name;
    }

    public ComplexMesh getMesh() {
        return mesh;
    }
}
