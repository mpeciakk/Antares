#version 400 core

in vec3 position;
in vec2 textureCoords;

out vec2 out_textureCoords;

uniform mat4 projectionMatrix;

void main() {
    out_textureCoords = textureCoords;

    gl_Position = vec4(position, 1.0);
}