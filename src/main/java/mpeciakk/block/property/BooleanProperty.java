package mpeciakk.block.property;

import java.util.List;

public class BooleanProperty extends Property<Boolean> {
    private final List<Boolean> values = List.of(Boolean.FALSE, Boolean.TRUE);

    public BooleanProperty(String name) {
        super(name);
    }

    @Override
    public List<Boolean> getValues() {
        return values;
    }
}
