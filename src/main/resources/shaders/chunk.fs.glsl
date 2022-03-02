#version 400 core

in vec2 out_textureCoords;
in vec3 out_vertexPos;

out vec4 out_Color;

uniform sampler2D sampler;

void main() {
    out_Color = texture(sampler, out_textureCoords);
}