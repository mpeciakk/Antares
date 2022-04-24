package mpeciakk.model;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;
import mpeciakk.asset.data.Texture;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Model {
    private final String type;

    private final Map<String, ModelPart> parts = new HashMap<>();
    private final Map<String, Texture> textures = new HashMap<>();

    public Model(JsonModel jsonModel) {
        this.type = jsonModel.getType();

        Map<String, String> rawTextures = jsonModel.getTextures();
        Set<String> textureKeys = rawTextures.keySet();

        for (String textureKey : textureKeys) {
            textures.put(textureKey, AssetManager.INSTANCE.get(AssetType.Texture, rawTextures.get(textureKey)));
        }

        if (!type.equals("cube") && !type.equals("empty")) {
            for (JsonModel.Element element : jsonModel.getElements()) {
                addPart(new ModelPart(this, element));
            }
        }
    }

    public String getType() {
        return type;
    }

    public void addPart(ModelPart part) {
        parts.put(part.getName(), part);
    }

    public ModelPart getPart(String name) {
        return parts.get(name);
    }

    public Collection<ModelPart> getParts() {
        return parts.values();
    }

    public Map<String, Texture> getTextures() {
        return textures;
    }
}
