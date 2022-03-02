#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;

out vec2 out_textureCoords;
out vec3 out_vertexPos;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

void main() {
    out_textureCoords = textureCoords;
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
}