package mpeciakk.shader;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;

public class ChunkShader extends Shader {

    public ChunkShader() {
        super(AssetManager.INSTANCE.get(AssetType.Shader, "chunk"));
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }
}