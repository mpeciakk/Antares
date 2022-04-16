#ifdef VERTEX

in vec3 position;
in vec2 textureCoords;

out vec2 out_textureCoords;

uniform mat4 projectionMatrix;

void main() {
    out_textureCoords = textureCoords;

    gl_Position = vec4(position, 1.0);
}

#else

in vec2 out_textureCoords;

out vec4 out_Color;

uniform sampler2D sampler;

void main() {
    out_Color = texture(sampler, out_textureCoords);
}

#endif