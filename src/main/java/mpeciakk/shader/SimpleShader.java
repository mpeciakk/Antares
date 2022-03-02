package mpeciakk.shader;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;

public class SimpleShader extends Shader {

    public SimpleShader() {
        super(AssetManager.INSTANCE.get(AssetType.Shader, "simple"));
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }
}