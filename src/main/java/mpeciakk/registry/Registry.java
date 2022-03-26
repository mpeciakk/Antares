package mpeciakk.registry;

import mpeciakk.asset.AssetManager;
import mpeciakk.asset.AssetType;
import mpeciakk.asset.data.BlockModelData;
import mpeciakk.asset.data.BlockStateData;
import mpeciakk.block.Block;
import mpeciakk.block.BlockModel;

import java.util.HashMap;
import java.util.Map;

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
            BlockStateData stateData = AssetManager.INSTANCE.get(AssetType.BlockState, id);

            if (stateData == null) {
                System.err.println("Can't find state for block " + id);
                return;
            }

           for (BlockStateData.SingleStateData state : stateData.getStates()) {
               BlockModelData modelData = AssetManager.INSTANCE.get(AssetType.BlockModel, state.getModel());

               if (modelData == null) {
                   System.err.println("Can't find model " + state.getModel());
                   return;
               }

               BlockModel model = new BlockModel();
               model.setType(modelData.type());

               for (Map.Entry<String, String> entry : modelData.textures().entrySet()) {
                   model.getTextures().put(entry.getKey(), AssetManager.INSTANCE.get(AssetType.Texture, entry.getValue()));
               }

               model.setFull(modelData.full());

               Block.BlockStateBuilder stateBuilder = block.getBlockStateBuilder();

               for (Map.Entry<String, Object> property : state.getProperties().entrySet()) {
                   stateBuilder.with(property.getKey(), (Comparable<?>) property.getValue());
               }

               stateBuilder.get().setModel(model);
           }
        }
    }
}
