package mpeciakk.block.property;

import java.util.List;

public abstract class Property<T extends Comparable<T>> {
    private final String name;

    public Property(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract List<T> getValues();

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                '}';
    }
}
