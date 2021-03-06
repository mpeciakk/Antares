package mpeciakk.block;

import mpeciakk.block.property.Property;
import mpeciakk.block.renderer.BlockRenderer;
import mpeciakk.block.renderer.DefaultBlockRenderer;
import mpeciakk.block.state.BlockState;
import mpeciakk.block.state.StateManager;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Block {
    protected final StateManager stateManager;

    private final BlockRenderer blockRenderer;

    public Block() {
        this.stateManager = new StateManager(this);
        this.blockRenderer = new DefaultBlockRenderer(this);

        appendProperties();
        stateManager.updateStates();
    }

    protected void appendProperties() {
    }

    public boolean hasBlockRenderer() {
        return true;
    }

    public BlockRenderer getBlockRenderer() {
        return blockRenderer;
    }

    public BlockStateBuilder getBlockStateBuilder() {
        return new BlockStateBuilder(this);
    }

    public BlockState getDefaultState() {
        return stateManager.getStates().get(0);
    }

    public static class BlockStateBuilder {
        private final Map<Property<?>, Comparable<?>> properties;
        private final Deque<Property<?>> propertiesOrder;
        private final Map<String, Property<?>> nameToProperty;
        private final Block block;

        public BlockStateBuilder(Block block) {
            this.properties = new HashMap<>(block.getDefaultState().getProperties());
            this.propertiesOrder = new LinkedList<>(block.getDefaultState().getPropertiesOrder());
            this.nameToProperty = block.getDefaultState().getPropertiesOrder().stream().collect(Collectors.toMap(Property::getName, Function.identity()));

            this.block = block;
        }

        public BlockStateBuilder with(Property<?> property, Comparable<?> value) {
            properties.put(property, value);

            return this;
        }

        public BlockStateBuilder with(String property, Comparable<?> value) {
            properties.put(nameToProperty.get(property), value);

            return this;
        }

        public BlockState get() {
            return block.stateManager.getStates().get(getId());
        }

        public int getId() {
            int currentBase = 1;
            int relativeId = 0;

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

        @Override
        public String toString() {
            return "BlockStateBuilder{" +
                    "properties=" + properties +
                    ", propertiesOrder=" + propertiesOrder +
                    ", nameToProperty=" + nameToProperty +
                    ", block=" + block +
                    '}';
        }
    }
}