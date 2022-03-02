package mpeciakk.render.mesh.builder;

import mpeciakk.render.mesh.ComplexMesh;
import mpeciakk.render.mesh.Vertex;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ComplexMeshBuilder {
    private List<Vertex> vertices = new ArrayList<>();

    public void drawCuboid(Vertex a, Vertex b) {
        // Front
//        vertices.add(new Vertex(new Vector3f(a.getPosition().x, a.getPosition().y, a.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(b.getPosition().x, a.getPosition().y, a.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(b.getPosition().x, b.getPosition().y, a.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(a.getPosition().x, b.getPosition().y, a.getPosition().z)));
//
//        // Back
//        vertices.add(new Vertex(new Vector3f(a.getPosition().x, a.getPosition().y, b.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(b.getPosition().x, a.getPosition().y, b.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(b.getPosition().x, b.getPosition().y, b.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(a.getPosition().x, b.getPosition().y, b.getPosition().z)));
//
//        // Left
//        vertices.add(new Vertex(new Vector3f(a.getPosition().x, a.getPosition().y, a.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(a.getPosition().x, a.getPosition().y, b.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(a.getPosition().x, b.getPosition().y, b.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(a.getPosition().x, b.getPosition().y, a.getPosition().z)));
//
//        // Right
//        vertices.add(new Vertex(new Vector3f(b.getPosition().x, a.getPosition().y, a.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(b.getPosition().x, a.getPosition().y, b.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(b.getPosition().x, b.getPosition().y, b.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(b.getPosition().x, b.getPosition().y, a.getPosition().z)));
//
//        // Bottom
//        vertices.add(new Vertex(new Vector3f(a.getPosition().x, a.getPosition().y, a.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(b.getPosition().x, a.getPosition().y, a.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(b.getPosition().x, a.getPosition().y, b.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(a.getPosition().x, a.getPosition().y, b.getPosition().z)));
//
//        // Top
//        vertices.add(new Vertex(new Vector3f(a.getPosition().x, b.getPosition().y, a.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(b.getPosition().x, b.getPosition().y, a.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(b.getPosition().x, b.getPosition().y, b.getPosition().z)));
//        vertices.add(new Vertex(new Vector3f(a.getPosition().x, b.getPosition().y, b.getPosition().z)));
    }

    public void drawQuad(Vertex a, Vertex b, Vertex c, Vertex d) {
        vertices.add(a);
        vertices.add(b);
        vertices.add(c);
        vertices.add(d);
    }

    public ComplexMesh getMesh() {
        return (ComplexMesh) new ComplexMesh().setVertices(vertices).flush();
    }
}
