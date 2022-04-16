#ifdef VERTEX

layout (location = 0) in int position;

out vec2 out_textureCoords;
out vec3 out_vertexPos;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

void main() {
    float x = position & 0x1F;
    int y = (position >> 5 & 0x1FF) - 256;
    float z = position >> 14 & 0x1F;
    float uv = position >> 19 & 0x7;
    float tid = position >> 22 & 0x7;

    out_vertexPos = vec3(x, y, z);

    float m = 16.0;

    int col = int(tid) % int(m);
    int row = int(floor(tid / m));

    if (uv == 0.0) {
        out_textureCoords = vec2(0.0 + col, 0.0 + row) / m;
    } else if (uv == 1.0) {
        out_textureCoords = vec2(0.0 + col, 1.0 + row) / m;
    } else if (uv == 2.0) {
        out_textureCoords = vec2(1.0 + col, 1.0 + row) / m;
    } else if (uv == 3.0) {
        out_textureCoords = vec2(1.0 + col, 0.0 + row) / m;
    }

    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(x, y, z, 1);
}

#else

in vec2 out_textureCoords;
in vec3 out_vertexPos;

out vec4 out_Color;

uniform sampler2D sampler;

void main() {
    out_Color = texture(sampler, out_textureCoords);
}

#endif