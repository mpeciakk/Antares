package mpeciakk.block;

import mpeciakk.block.property.Property;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Block {
    private BlockModel model;

    protected final StateManager stateManager;

    public Block() {
        stateManager = new StateManager(this);
        appendProperties();
        stateManager.updateStates();
    }

    protected void appendProperties() {
    }

    public BlockStateBuilder getBlockStateBuilder() {
        return new BlockStateBuilder(this);
    }

    public BlockState getDefaultState() {
        return stateManager.getStates().get(0);
    }

    public BlockModel getModel() {
        return model;
    }

    public void setModel(BlockModel model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "Block{" +
                ", model=" + model +
                '}';
    }

    public static class BlockStateBuilder {
        private final Map<Property<?>, Comparable<?>> properties;
        private final Deque<Property<?>> propertiesOrder;
        private final Block block;

        public BlockStateBuilder(Block block) {
            this.properties = new HashMap<>(block.getDefaultState().getProperties());
            this.propertiesOrder = new LinkedList<>(block.getDefaultState().getPropertiesOrder());
            this.block = block;
        }

        public BlockStateBuilder with(Property<?> property, Comparable<?> value) {
            properties.put(property, value);

            return this;
        }

        public BlockState get() {
            return block.stateManager.getStates().get(getId());
        }

        public int getId() {
            int currentBase = 1;
            int relativeId = 0;

            System.out.println(propertiesOrder);

            for (Property<?> property : propertiesOrder) {
                int value = 0;
                Comparable<?> valueObject = properties.get(property);

                if (valueObject instanceof Integer) {
                    value = (Integer) valueObject;
                }

                if (valueObject instanceof Enum<?>) {
                    value = ((Enum<?>) valueObject).ordinal();
                }

                if (valueObject instanceof Boolean bool) {
                    value = (bool ? 1 : 0);
                }

                relativeId += value * currentBase;

                currentBase *= property.getValues().size();
            }

            return relativeId;
        }
    }
}