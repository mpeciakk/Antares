package mpeciakk.light;

import org.joml.Vector3f;

public class PointLight {
    private final Vector3f color;
    private final Vector3f position;
    private final float intensity;
    private final Attenuation attenuation;

    public PointLight(Vector3f color, Vector3f position, float intensity, Attenuation att) {
        this.color = color;
        this.position = position;
        this.intensity = intensity;
        this.attenuation = att;
    }

    public Vector3f getColor() {
        return color;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getIntensity() {
        return intensity;
    }

    public Attenuation getAttenuation() {
        return attenuation;
    }

    public static class Attenuation {
        private final float constant;
        private final float linear;
        private final float exponent;

        public Attenuation(float constant, float linear, float exponent) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }

        public float getConstant() {
            return constant;
        }

        public float getLinear() {
            return linear;
        }

        public float getExponent() {
            return exponent;
        }
    };
};
