#version 400 core

layout (location = 0) in int position;

out vec2 out_textureCoords;
out vec3 out_vertexPos;

out float outline;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;
uniform ivec3 highlightedBlock;

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

    if (
        (highlightedBlock.x != 2137 && highlightedBlock.y != 2137 && highlightedBlock.z != 2137) && (
            // 0, 0, 0
            (x == highlightedBlock.x && y == highlightedBlock.y && z == highlightedBlock.z) ||
            // 1, 0, 0
            (x == highlightedBlock.x + 1 && y == highlightedBlock.y && z == highlightedBlock.z) ||
            // 1, 1, 0
            (x == highlightedBlock.x + 1 && y == highlightedBlock.y + 1 && z == highlightedBlock.z) ||
            // 0, 1, 0
            (x == highlightedBlock.x && y == highlightedBlock.y + 1 && z == highlightedBlock.z) ||
            // 0, 0, 1
            (x == highlightedBlock.x && y == highlightedBlock.y && z == highlightedBlock.z + 1) ||
            // 1, 0, 1
            (x == highlightedBlock.x + 1 && y == highlightedBlock.y && z == highlightedBlock.z + 1) ||
            // 1, 1, 1
            (x == highlightedBlock.x + 1 && y == highlightedBlock.y + 1 && z == highlightedBlock.z + 1) ||
            // 0, 1, 1
            (x == highlightedBlock.x && y == highlightedBlock.y + 1 && z == highlightedBlock.z + 1)
        )
    ) {
        outline = 1f;
    } else {
        outline = 0f;
    }

    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(out_vertexPos, 1);
}