varying vec2 out_textureCoords;
varying vec3 out_vertexPos;

varying float outline;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;
uniform ivec3 highlightedBlock;

uniform sampler2D sampler;

#section VERTEX_SHADER

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;

void main() {
    int x = int(floor(position.x));
    int y = int(floor(position.y));
    int z = int(floor(position.z));

    if ((highlightedBlock.x != 2137 && highlightedBlock.y != 2137 && highlightedBlock.z != 2137) && (
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
        (x == highlightedBlock.x && y == highlightedBlock.y + 1 && z == highlightedBlock.z + 1))
    ) {
        outline = 1f;
    } else {
        outline = 0f;
    }

    out_textureCoords = textureCoords;
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
}

#section FRAGMENT_SHADER

out vec4 out_Color;

void main() {
    float epsilona = 0.025;
    float epsilonb = 0.0001;

    float xx = fract(out_vertexPos.x);
    float yy = fract(out_vertexPos.y);
    float zz = fract(out_vertexPos.z);
    bool nearX = (xx >= -epsilona && xx <= epsilona);
    bool nearY = (yy >= -epsilona && yy <= epsilona);
    bool nearZ = (zz >= -epsilona && zz <= epsilona);
    bool overX = (xx >= -epsilonb && xx <= epsilonb);
    bool overY = (yy >= -epsilonb && yy <= epsilonb);
    bool overZ = (zz >= -epsilonb && zz <= epsilonb);

    if (outline > 0.995) {
        out_Color = texture(sampler, out_textureCoords) * vec4(0.3, 0.3, 0.3, 1f);
    } else {
        out_Color = texture(sampler, out_textureCoords);
    }
}