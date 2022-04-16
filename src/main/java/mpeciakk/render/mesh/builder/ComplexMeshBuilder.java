package mpeciakk.render.mesh.builder;

import mpeciakk.render.mesh.ComplexMesh;
import mpeciakk.render.mesh.Vertex;

public class ComplexMeshBuilder extends MeshBuilder<Vertex> {

    public void drawQuad(Vertex a, Vertex b, Vertex c, Vertex d) {
        vertices.add(a);
        vertices.add(b);
        vertices.add(c);
        vertices.add(d);
    }

    @Override
    public ComplexMesh build() {
        return (ComplexMesh) new ComplexMesh().setVertices(vertices).flush();
    }
}
