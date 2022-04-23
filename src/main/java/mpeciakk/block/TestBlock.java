package mpeciakk.block;

import mpeciakk.block.property.BooleanProperty;
import mpeciakk.block.property.IntProperty;

public class TestBlock extends Block {
    public static final BooleanProperty property1 = new BooleanProperty("property1");
    public static final IntProperty property2 = new IntProperty("property2");
    public static final BooleanProperty property3 = new BooleanProperty("property3");

    @Override
    protected void appendProperties() {
        stateManager.addProperty(property1).addProperty(property2).addProperty(property3);
    }
}
