#version 400 core

in vec2 out_textureCoords;
in vec3 out_vertexPos;
in float outline;

out vec4 out_Color;

uniform sampler2D sampler;

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
//        if ((nearX && !overX) || (nearY && !overY) || (nearZ && !overZ)) {
//            out_Color = vec4(0, 0, 0, 1.0);
//        } else {
//            out_Color = texture(sampler, out_textureCoords);
//        }

        out_Color = texture(sampler, out_textureCoords) * vec4(0.3, 0.3, 0.3, 1f);

    } else {
        out_Color = texture(sampler, out_textureCoords);
    }

//    if (outline > 0.999f) {
//        out_Color = vec4(outline, 0f, 0f, 1f);
//    } else {
//        out_Color = texture(sampler, out_textureCoords);
//    }
}