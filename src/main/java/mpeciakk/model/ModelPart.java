package mpeciakk.model;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;
import mpeciakk.asset.data.Texture;
import mpeciakk.render.mesh.Vertex;
import mpeciakk.render.mesh.builder.ComplexMeshBuilder;
import org.joml.Vector3f;

import java.util.Map;

public class ModelPart {

    private final float[] from;
    private final float[] to;
    private final String name;
    private final Map<String, JsonModel.Element.Face> faces;

    public ModelPart(JsonModel.Element element) {
        this(element.getFrom(), element.getTo(), element.getName(), element.getFaces());
    }

    public ModelPart(float[] from, float[] to, String name, Map<String, JsonModel.Element.Face> faces) {
        this.name = name;
        this.from = from;
        this.to = to;
        this.faces = faces;
    }

    public void draw(ComplexMeshBuilder meshBuilder, Vector3f offset) {
        Texture texture = AssetManager.INSTANCE.get(AssetType.Texture, "anvil");

        float atlasSize = 16.0f;
        float blockScale = 16.0f;
        int tid = texture.index();

        int col = (int) tid % (int) atlasSize;
        int row = (int) Math.floor(tid / atlasSize);

        // v + x offset + y offset + z offset
        Vector3f v000 = new Vector3f(from[0] / blockScale + offset.x, from[1] / blockScale + offset.y, from[2] / blockScale + offset.z);
        Vector3f v100 = new Vector3f(to[0] / blockScale + offset.x, from[1] / blockScale + offset.y, from[2] / blockScale + offset.z);
        Vector3f v110 = new Vector3f(to[0] / blockScale + offset.x, to[1] / blockScale + offset.y, from[2] / blockScale + offset.z);
        Vector3f v010 = new Vector3f(from[0] / blockScale + offset.x, to[1] / blockScale + offset.y, from[2] / blockScale + offset.z);

        Vector3f v001 = new Vector3f(from[0] / blockScale + offset.x, from[1] / blockScale + offset.y, to[2] / blockScale + offset.z);
        Vector3f v101 = new Vector3f(to[0] / blockScale + offset.x, from[1] / blockScale + offset.y, to[2] / blockScale + offset.z);
        Vector3f v111 = new Vector3f(to[0] / blockScale + offset.x, to[1] / blockScale + offset.y, to[2] / blockScale + offset.z);
        Vector3f v011 = new Vector3f(from[0] / blockScale + offset.x, to[1] / blockScale + offset.y, to[2] / blockScale + offset.z);

        if (faces.containsKey("west")) {
            float[] west = faces.get("west").getUv();

            int[] uvIndexes = getUvIndexes(faces.get("west").getRotation());

            meshBuilder.drawQuad(
                    new Vertex(v000, (col + 1 / blockScale * west[uvIndexes[0]]) / atlasSize, (row + 1 / blockScale * west[uvIndexes[1]]) / atlasSize, 0, 0, 0),
                    new Vertex(v001, (col + 1 / blockScale * west[uvIndexes[2]]) / atlasSize, (row + 1 / blockScale * west[uvIndexes[3]]) / atlasSize, 0, 0, 0),
                    new Vertex(v011, (col + 1 / blockScale * west[uvIndexes[4]]) / atlasSize, (row + 1 / blockScale * west[uvIndexes[5]]) / atlasSize, 0, 0, 0),
                    new Vertex(v010, (col + 1 / blockScale * west[uvIndexes[6]]) / atlasSize, (row + 1 / blockScale * west[uvIndexes[7]]) / atlasSize, 0, 0, 0)
            );
        }

        if (faces.containsKey("east")) {
            float[] east = faces.get("east").getUv();
            int[] uvIndexes = getUvIndexes(faces.get("east").getRotation());

            meshBuilder.drawQuad(
                    new Vertex(v100, (col + 1 / blockScale * east[uvIndexes[0]]) / atlasSize, (row + 1 / blockScale * east[uvIndexes[1]]) / atlasSize, 0, 0, 0),
                    new Vertex(v101, (col + 1 / blockScale * east[uvIndexes[2]]) / atlasSize, (row + 1 / blockScale * east[uvIndexes[3]]) / atlasSize, 0, 0, 0),
                    new Vertex(v111, (col + 1 / blockScale * east[uvIndexes[4]]) / atlasSize, (row + 1 / blockScale * east[uvIndexes[5]]) / atlasSize, 0, 0, 0),
                    new Vertex(v110, (col + 1 / blockScale * east[uvIndexes[6]]) / atlasSize, (row + 1 / blockScale * east[uvIndexes[7]]) / atlasSize, 0, 0, 0)
            );
        }

        if (faces.containsKey("south")) {
            float[] south = faces.get("south").getUv();
            int[] uvIndexes = getUvIndexes(faces.get("south").getRotation());

            meshBuilder.drawQuad(
                    new Vertex(v001, (col + 1 / blockScale * south[uvIndexes[0]]) / atlasSize, (row + 1 / blockScale * south[uvIndexes[1]]) / atlasSize, 0, 0, 0),
                    new Vertex(v101, (col + 1 / blockScale * south[uvIndexes[2]]) / atlasSize, (row + 1 / blockScale * south[uvIndexes[3]]) / atlasSize, 0, 0, 0),
                    new Vertex(v111, (col + 1 / blockScale * south[uvIndexes[4]]) / atlasSize, (row + 1 / blockScale * south[uvIndexes[5]]) / atlasSize, 0, 0, 0),
                    new Vertex(v011, (col + 1 / blockScale * south[uvIndexes[6]]) / atlasSize, (row + 1 / blockScale * south[uvIndexes[7]]) / atlasSize, 0, 0, 0)
            );
        }

        if (faces.containsKey("north")) {
            float[] north = faces.get("north").getUv();
            int[] uvIndexes = getUvIndexes(faces.get("north").getRotation());

            meshBuilder.drawQuad(
                    new Vertex(v000, (col + 1 / blockScale * north[uvIndexes[0]]) / atlasSize, (row + 1 / blockScale * north[uvIndexes[1]]) / atlasSize, 0, 0, 0),
                    new Vertex(v100, (col + 1 / blockScale * north[uvIndexes[2]]) / atlasSize, (row + 1 / blockScale * north[uvIndexes[3]]) / atlasSize, 0, 0, 0),
                    new Vertex(v110, (col + 1 / blockScale * north[uvIndexes[4]]) / atlasSize, (row + 1 / blockScale * north[uvIndexes[5]]) / atlasSize, 0, 0, 0),
                    new Vertex(v010, (col + 1 / blockScale * north[uvIndexes[6]]) / atlasSize, (row + 1 / blockScale * north[uvIndexes[7]]) / atlasSize, 0, 0, 0)
            );
        }

        if (faces.containsKey("up")) {
            float[] up = faces.get("up").getUv();
            int[] uvIndexes = getUvIndexes(faces.get("up").getRotation());

            meshBuilder.drawQuad(
                    new Vertex(v010, (col + 1 / blockScale * up[uvIndexes[0]]) / atlasSize, (row + 1 / blockScale * up[uvIndexes[1]]) / atlasSize, 0, 0, 0),
                    new Vertex(v110, (col + 1 / blockScale * up[uvIndexes[2]]) / atlasSize, (row + 1 / blockScale * up[uvIndexes[3]]) / atlasSize, 0, 0, 0),
                    new Vertex(v111, (col + 1 / blockScale * up[uvIndexes[4]]) / atlasSize, (row + 1 / blockScale * up[uvIndexes[5]]) / atlasSize, 0, 0, 0),
                    new Vertex(v011, (col + 1 / blockScale * up[uvIndexes[6]]) / atlasSize, (row + 1 / blockScale * up[uvIndexes[7]]) / atlasSize, 0, 0, 0)
            );
        }

        if (faces.containsKey("down")) {
            float[] down = faces.get("down").getUv();
            int[] uvIndexes = getUvIndexes(faces.get("down").getRotation());

            meshBuilder.drawQuad(
                    new Vertex(v000, (col + 1 / blockScale * down[uvIndexes[0]]) / atlasSize, (row + 1 / blockScale * down[uvIndexes[1]]) / atlasSize, 0, 0, 0),
                    new Vertex(v100, (col + 1 / blockScale * down[uvIndexes[2]]) / atlasSize, (row + 1 / blockScale * down[uvIndexes[3]]) / atlasSize, 0, 0, 0),
                    new Vertex(v101, (col + 1 / blockScale * down[uvIndexes[4]]) / atlasSize, (row + 1 / blockScale * down[uvIndexes[5]]) / atlasSize, 0, 0, 0),
                    new Vertex(v001, (col + 1 / blockScale * down[uvIndexes[6]]) / atlasSize, (row + 1 / blockScale * down[uvIndexes[7]]) / atlasSize, 0, 0, 0)
            );
        }
    }

    private int[] getUvIndexes(int rotation) {
        int[] uv = new int[]{0, 1, 2, 1, 2, 3, 0, 3};

        if (rotation == 90) {
            uv = new int[]{2, 1, 2, 3, 0, 3, 0, 1};
        }

        if (rotation == 180) {
            uv = new int[]{2, 3, 0, 3, 0, 1, 2, 1};
        }

        if (rotation == 270) {
            uv = new int[]{0, 3, 0, 1, 2, 1, 2, 3};
        }

        return uv;
    }

    public String getName() {
        return name;
    }
}
