varying vec2 out_textureCoords;
varying vec3 vertexPos;
varying vec3 mvVertexPos;
varying vec3 normal;

varying float outline;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

uniform ivec3 highlightedBlock;

uniform vec3 lightPosition;

uniform sampler2D sampler;

struct Attenuation {
    float constant;
    float linear;
    float exponent;
};

struct PointLight {
    vec3 color;
    vec3 position;
    float intensity;
    Attenuation att;
};

uniform PointLight pointLight;

#section VERTEX_SHADER

layout(location = 0) in int position;

void main() {
    float x = position & 0x1F;
    int y = (position >> 5 & 0x1FF);
    float z = position >> 14 & 0x1F;
    float uv = position >> 19 & 0x7;
    float n = position >> 22 & 0x7;
    float tid = position >> 25 & 0x7;

    vertexPos = vec3(x, y, z);

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
        (x == highlightedBlock.x && y == highlightedBlock.y + 1 && z == highlightedBlock.z + 1))
    ) {
        outline = 1f;
    } else {
        outline = 0f;
    }

    normal = vec3(0, 0, 0);

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

    mvVertexPos = (transformationMatrix * vec4(vertexPos, 1.0)).xyz;

    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(vertexPos, 1);
}

#section FRAGMENT_SHADER

out vec4 out_Color;

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal, vec4 texture) {
    float reflectance = 1;

    // Diffuse Light
    vec3 toLightDirection = light.position - position;
    vec3 unitToLightDirection = normalize(toLightDirection);
    float diffuseFactor = dot(normal, unitToLightDirection);
    vec4 diffuseColor = texture * vec4(light.color, 1.0) * diffuseFactor * max(diffuseFactor, 0.0);

    // Specular Light
    vec3 toCameraDirection = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - position.xyz;
    vec3 unitToCameraDirection = normalize(toCameraDirection);

    vec3 fromLightDirection = -unitToLightDirection;

    vec3 reflectedLight = reflect(fromLightDirection, normal);

    float specularFactor = dot(reflectedLight, unitToCameraDirection);
    specularFactor = max(specularFactor, 0.0);
    specularFactor = pow(specularFactor, 10.0);
    vec4 specularColor = texture * specularFactor * reflectance * vec4(light.color, 1.0);

    // Attenuation
    float distance = length(toLightDirection);
    float attenuationInv = light.att.constant + light.att.linear * distance + light.att.exponent * distance * distance;

    return (diffuseColor + specularColor) / attenuationInv;
}

void main() {
    vec4 light = calcPointLight(pointLight, mvVertexPos, normalize(normal), texture(sampler, out_textureCoords));
    vec4 ambient = vec4(0, 0, 0, 1.0);

    if (outline > 0.995) {
        out_Color = texture(sampler, out_textureCoords) * vec4(0.3, 0.3, 0.3, 1f);
    } else {
        out_Color = ambient * texture(sampler, out_textureCoords) + light;
    }
}