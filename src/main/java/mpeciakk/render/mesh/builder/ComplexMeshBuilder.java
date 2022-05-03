package mpeciakk.render.mesh.builder;

import mpeciakk.render.mesh.ComplexMesh;
import mpeciakk.render.mesh.Vertex;
import org.joml.Vector3f;

public class ComplexMeshBuilder extends MeshBuilder<Vertex> {

    public void drawQuad(Vertex a, Vertex b, Vertex c, Vertex d) {
        vertices.add(a);
        vertices.add(b);
        vertices.add(c);
        vertices.add(d);
    }

    public ComplexMeshBuilder drawCube(Vector3f position, Vector3f scale) {
        Vector3f v000 = new Vector3f(position.x, position.y, position.z);
        Vector3f v100 = new Vector3f(position.x + scale.x, position.y, position.z);
        Vector3f v110 = new Vector3f(position.x + scale.x, position.y + scale.y, position.z);
        Vector3f v010 = new Vector3f(position.x, position.y + scale.y, position.z);

        Vector3f v001 = new Vector3f(position.x, position.y, position.z + scale.z);
        Vector3f v101 = new Vector3f(position.x + scale.x, position.y, position.z + scale.z);
        Vector3f v111 = new Vector3f(position.x + scale.x, position.y + scale.x, position.z + scale.z);
        Vector3f v011 = new Vector3f(position.x + scale.x, position.y + scale.x, position.z + scale.z);

        drawQuad(new Vertex(v000, 0, 0, 0, 0, 0), new Vertex(v100, 0, 0, 0, 0, 0), new Vertex(v110, 0, 0, 0, 0, 0), new Vertex(v010, 0, 0, 0, 0, 0));
        drawQuad(new Vertex(v000, 0, 0, 0, 0, 0), new Vertex(v001, 0, 0, 0, 0, 0), new Vertex(v011, 0, 0, 0, 0, 0), new Vertex(v010, 0, 0, 0, 0, 0));

        return this;
    }

    @Override
    public ComplexMesh build() {
        return (ComplexMesh) new ComplexMesh().setVertices(vertices).flush();
    }
}
