package mpeciakk.shader;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;

public class TextShader extends Shader {

    public TextShader() {
        super(AssetManager.INSTANCE.get(AssetType.Shader, "text"));
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoords");
    }
}