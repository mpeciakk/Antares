package mpeciakk.asset;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {

    public final static AssetManager INSTANCE = new AssetManager();
    private final Map<AssetType, Map<String, Object>> assets = new HashMap<>();

    public <T> void register(AssetType type, String name, T asset) {
        if (!assets.containsKey(type)) {
            assets.put(type, new HashMap<>());
        }

        assets.get(type).put(name, asset);
    }

    public <T> T get(AssetType type, String name) {
        if (assets.containsKey(type)) {
            Map<String, Object> assetsOfType = assets.get(type);

            if (assetsOfType.containsKey(name)) {
                return (T) assetsOfType.get(name);
            }
        }

        throw new IllegalStateException("Can't file asset of type " + type + " with name " + name);
    }

    public <T> Map<String, T> get(AssetType type) {
        if (assets.containsKey(type)) {
            return (Map<String, T>) assets.get(type);
        }

        throw new IllegalStateException("Can't file assets of type " + type);
    }

    public void stitchTextures() {
        Map<String, BufferedImage> images = get(AssetType.Image);

        TextureAtlas atlas = new TextureAtlas(256, 16);

        for (Map.Entry<String, BufferedImage> entry : images.entrySet()) {
            register(AssetType.Texture, entry.getKey(), atlas.addTexture(entry.getValue()));
        }

        register(AssetType.TextureAtlas, "blocks", atlas);
    }
}
