package mpeciakk.block.property;

import java.util.List;

public class IntProperty extends Property<Integer> {
    private final List<Integer> values = List.of(0, 1, 2, 3, 4);

    public IntProperty(String name) {
        super(name);
    }

    @Override
    public List<Integer> getValues() {
        return values;
    }
}
