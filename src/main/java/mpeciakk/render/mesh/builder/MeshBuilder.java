package mpeciakk.render.mesh.builder;

import mpeciakk.render.mesh.Mesh;

import java.util.ArrayList;
import java.util.List;

public abstract class MeshBuilder<T> {
    protected final List<T> vertices = new ArrayList<>();

    public abstract Mesh<T> build();

    public List<T> getVertices() {
        return vertices;
    }
}
