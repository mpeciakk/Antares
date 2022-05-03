#ifdef VERTEX

layout(location = 0) in int position;

out vec2 out_textureCoords;
out vec3 out_vertexPos;

out float outline;

out vec3 surfaceNormal;
out vec3 directionTowardsLight;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;
uniform ivec3 highlightedBlock;

uniform vec3 lightPosition;

void main() {
    float x = position & 0x1F;
    int y = (position >> 5 & 0x1FF);
    float z = position >> 14 & 0x1F;
    float uv = position >> 19 & 0x7;
    float n = position >> 22 & 0x7;
    float tid = position >> 25 & 0x7;

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

    vec3 normal = vec3(0, 0, 0);

    if (n == 0.0) {
        normal = vec3(0, 0, -1);
    } else if (n == 1.0) {
        normal = vec3(0, 0, 1);
    } else if (n == 2.0) {
        normal = vec3(-1, 0, 0);
    } else if (n == 3.0) {
        normal = vec3(1, 0, 0);
    } else if (n == 4.0) {
        normal = vec3(0, -1, 0);
    } else if (n == 5.0) {
        normal = vec3(0, 1, 0);
    }

    vec3 vertexPosition = (transformationMatrix * vec4(out_vertexPos, 1.0)).xyz;
    surfaceNormal = normal;
    directionTowardsLight = lightPosition - vertexPosition;

    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(out_vertexPos, 1);
}

#else

in vec2 out_textureCoords;
in vec3 out_vertexPos;
in float outline;

out vec4 out_Color;

in vec3 surfaceNormal;
in vec3 directionTowardsLight;

uniform sampler2D sampler;

uniform vec3 lightColor;

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

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(directionTowardsLight);

    float diffuseFactor = dot(unitNormal, unitLightVector);
    float brightness = max(diffuseFactor, 0.0);
    vec3 diffuse = lightColor * diffuseFactor * brightness;

    if (outline > 0.995) {
        //        if ((nearX && !overX) || (nearY && !overY) || (nearZ && !overZ)) {
        //            out_Color = vec4(0, 0, 0, 1.0);
        //        } else {
        //            out_Color = texture(sampler, out_textureCoords);
        //        }

        out_Color = texture(sampler, out_textureCoords) * vec4(0.3, 0.3, 0.3, 1f);
    } else {
        out_Color = vec4(diffuse, 1.0) * texture(sampler, out_textureCoords);
    }
}

#endif