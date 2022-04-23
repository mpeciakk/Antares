package mpeciakk.block.state;

import mpeciakk.block.Block;
import mpeciakk.block.property.Property;
import mpeciakk.model.Model;

import java.util.List;
import java.util.Map;

public class BlockState {
    private final Map<Property<?>, Comparable<?>> properties;
    private final List<Property<?>> propertiesOrder;

    private final Block block;

    private Model model;

    public BlockState(Map<Property<?>, Comparable<?>> properties, List<Property<?>> propertiesOrder, Block block) {
        this.properties = properties;
        this.propertiesOrder = propertiesOrder;
        this.block = block;
    }

    public Map<Property<?>, Comparable<?>> getProperties() {
        return properties;
    }

    public List<Property<?>> getPropertiesOrder() {
        return propertiesOrder;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public String toString() {
        return "BlockState{" +
                "properties=" + properties +
                ", block=" + block +
                '}';
    }
}
