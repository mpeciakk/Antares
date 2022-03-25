package mpeciakk.registry;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;
import mpeciakk.asset.data.BlockModelData;
import mpeciakk.block.Block;
import mpeciakk.block.BlockModel;

import java.util.*;

public abstract class Registry<T> {
    public final static Registry<Block> BLOCK = new BlockRegistry();

    private final Map<String, T> idToItem = new HashMap<>();
    private final Map<Integer, T> rawIdToItem = new HashMap<>();

    private int nextId = 0;

    public abstract void prepare(String id, T object);

    public T register(String id, T object) {
        prepare(id, object);

        idToItem.put(id, object);
        rawIdToItem.put(nextId++, object);

        return object;
    }

    private static class BlockRegistry extends Registry<Block> {
        @Override
        public void prepare(String id, Block block) {
            BlockModelData modelData = AssetManager.INSTANCE.get(AssetType.BlockModel, id);

            if (modelData == null) {
                System.err.println("Can't find model data for block " + id);
                return;
            }

            BlockModel model = new BlockModel();
            model.setType(modelData.type());

            for (Map.Entry<String, String> entry : modelData.textures().entrySet()) {
                model.getTextures().put(entry.getKey(), AssetManager.INSTANCE.get(AssetType.Texture, entry.getValue()));
            }

            model.setFull(modelData.full());

            block.setModel(model);
        }
    }
}
