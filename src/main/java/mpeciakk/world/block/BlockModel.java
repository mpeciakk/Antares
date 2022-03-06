package mpeciakk.world.block;

import mpeciakk.asset.data.Texture;

import java.util.HashMap;
import java.util.Map;

public class BlockModel {
    private String type;

    private Map<String, Texture> textures = new HashMap<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Texture> getTextures() {
        return textures;
    }

    public void setTextures(Map<String, Texture> textures) {
        this.textures = textures;
    }
}
