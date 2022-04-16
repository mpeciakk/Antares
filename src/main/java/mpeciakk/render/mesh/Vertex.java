package mpeciakk.render.mesh;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Vertex {
    private final Vector3f position;
    private final Vector2f textureCoordinate;
    private final Vector3f normal;

    public Vertex(Vector3f position, Vector2f textureCoordinate, Vector3f normal) {
        this.position = position;
        this.textureCoordinate = textureCoordinate;
        this.normal = normal;
    }

    public Vertex(float x, float y, float z, float u, float v, float nx, float ny, float nz) {
        this.position = new Vector3f(x, y, z);
        this.textureCoordinate = new Vector2f(u, v);
        this.normal = new Vector3f(nx, ny, nz);
    }

    public Vertex(Vector3f position, float u, float v, float nx, float ny, float nz) {
        this.position = position;
        this.textureCoordinate = new Vector2f(u, v);
        this.normal = new Vector3f(nx, ny, nz);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector2f getTextureCoordinate() {
        return textureCoordinate;
    }

    public Vector3f getNormal() {
        return normal;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "position=" + position +
                ", textureCoordinate=" + textureCoordinate +
                ", normal=" + normal +
                '}';
    }
}
