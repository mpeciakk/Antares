package mpeciakk.shader;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;

public class ComplexShader extends Shader {

    public ComplexShader() {
        super(AssetManager.INSTANCE.get(AssetType.Shader, "complex"));
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoords");
    }
}