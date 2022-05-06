package mpeciakk.shader;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;
import mpeciakk.light.PointLight;

public class ChunkShader extends Shader {

    public ChunkShader() {
        super(AssetManager.INSTANCE.get(AssetType.Shader, "chunk"));
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }

    public void loadPointLight(String uniformName, PointLight pointLight) {
        loadVector(uniformName + ".color", pointLight.getColor() );
        loadVector(uniformName + ".position", pointLight.getPosition());
        loadFloat(uniformName + ".intensity", pointLight.getIntensity());
        PointLight.Attenuation att = pointLight.getAttenuation();
        loadFloat(uniformName + ".att.constant", att.getConstant());
        loadFloat(uniformName + ".att.linear", att.getLinear());
        loadFloat(uniformName + ".att.exponent", att.getExponent());
    }
}