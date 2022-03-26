package mpeciakk.shader;

import mpeciakk.asset.data.ShadersData;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public abstract class Shader {

    private final int programId;
    private final int vertexId;
    private final int fragmentId;
    private final Map<String, Integer> locationCache = new HashMap<>();
    private FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public Shader(ShadersData shadersData) {
        this.vertexId = loadShader(shadersData.vs(), GL_VERTEX_SHADER);
        this.fragmentId = loadShader(shadersData.fs(), GL_FRAGMENT_SHADER);

        this.programId = glCreateProgram();

        glAttachShader(programId, vertexId);
        glAttachShader(programId, fragmentId);
        bindAttributes();
        glLinkProgram(programId);
        glValidateProgram(programId);
    }

    private static int loadShader(String source, int type) {
        int id = glCreateShader(type);
        glShaderSource(id, source);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(id, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }

        return id;
    }

    public void start() {
        glUseProgram(programId);
    }

    public void stop() {
        glUseProgram(0);
    }

    public void destroy() {
        stop();
        glDetachShader(programId, vertexId);
        glDetachShader(programId, fragmentId);

        glDeleteShader(vertexId);
        glDeleteShader(fragmentId);
        glDeleteProgram(programId);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        loadMatrix("transformationMatrix", matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        loadMatrix("projectionMatrix", matrix);
    }

    public void loadViewMatrix(Matrix4f matrix) {
        loadMatrix("viewMatrix", matrix);
    }

    public int getUniformLocation(String name) {
        if (locationCache.containsKey(name)) {
            return locationCache.get(name);
        } else {
            int location = glGetUniformLocation(programId, name);
            locationCache.put(name, location);

            return location;
        }
    }

    public void loadFloat(String name, float value) {
        glUniform1f(getUniformLocation(name), value);
    }

    public void loadInt(String name, int value) {
        glUniform1i(getUniformLocation(name), value);
    }

    public void loadVector(String name, Vector3f vector) {
        glUniform3f(getUniformLocation(name), vector.x, vector.y, vector.z);
    }

    public void loadVector(String name, Vector3i vector) {
        glUniform3i(getUniformLocation(name), vector.x, vector.y, vector.z);
    }

    public void loadBoolean(String name, boolean value) {
        glUniform1f(getUniformLocation(name), value ? 1 : 0);
    }

    public void loadMatrix(String name, Matrix4f matrix) {
        matrixBuffer = matrix.get(matrixBuffer);

        glUniformMatrix4fv(getUniformLocation(name), false, matrixBuffer);

        matrixBuffer.clear();
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String name) {
        glBindAttribLocation(programId, attribute, name);
    }
}