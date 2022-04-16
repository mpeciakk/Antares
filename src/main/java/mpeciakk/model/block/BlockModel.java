package mpeciakk.model.block;

import mpeciakk.asset.data.Texture;
import mpeciakk.model.Model;

import java.util.HashMap;
import java.util.Map;

public class BlockModel extends Model {
    private String type;
    private boolean full;

    private final Map<String, Texture> textures = new HashMap<>();

    public BlockModel(String type, boolean full) {
        this.type = type;
        this.full = full;
    }

    public String getType() {
        return type;
    }

    public boolean isFull() {
        return full;
    }

    public Map<String, Texture> getTextures() {
        return textures;
    }

    public boolean isComplex() {
        return !type.equals("cube");
    }

    @Override
    public String toString() {
        return "BlockModel{" +
                "type='" + type + '\'' +
                ", full=" + full +
                ", textures=" + textures +
                '}';
    }
}
