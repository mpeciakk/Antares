package mpeciakk.registry;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;
import mpeciakk.asset.data.BlockModelData;
import mpeciakk.world.block.Block;
import mpeciakk.world.block.BlockModel;

import java.util.*;

public class Registry<T> {
    public final static Registry<Block> BLOCK = new BlockRegistry();

    protected final Map<String, T> items = new HashMap<>();

    public static <V> V register(Registry<V> registry, String id, V object) {
        registry.register(id, object);

        return object;
    }

    public void register(String id, T object) {
    }

    private static class BlockRegistry extends Registry<Block> {
        @Override
        public void register(String id, Block block) {
            BlockModelData modelData = AssetManager.INSTANCE.get(AssetType.BlockModel, "cobblestone");

            BlockModel model = new BlockModel();
            model.setType(modelData.type());

            for (Map.Entry<String, String> entry : modelData.textures().entrySet()) {
                model.getTextures().put(entry.getKey(), AssetManager.INSTANCE.get(AssetType.Texture, entry.getValue()));
            }

            block.setModel(model);

            items.put(id, block);
        }
    }
}
