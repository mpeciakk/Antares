package mpeciakk.render.mesh.builder;

import mpeciakk.render.mesh.ComplexMesh;
import mpeciakk.render.mesh.Vertex;

import java.util.ArrayList;
import java.util.List;

public class ComplexMeshBuilder {
    private final List<Vertex> vertices = new ArrayList<>();

    public void drawQuad(Vertex a, Vertex b, Vertex c, Vertex d) {
        vertices.add(a);
        vertices.add(b);
        vertices.add(c);
        vertices.add(d);
    }

    public ComplexMesh getMesh() {
        return (ComplexMesh) new ComplexMesh().setVertices(vertices).flush();
    }

    public List<Vertex> getVertices() {
        return vertices;
    }
}
