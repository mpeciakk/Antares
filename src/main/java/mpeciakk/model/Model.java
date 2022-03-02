package mpeciakk.model;

import java.util.*;

public class Model {
    private final Map<String, ModelPart> parts = new HashMap<>();

    public void addPart(ModelPart part) {
        parts.put(part.getName(), part);
    }

    public ModelPart getPart(String name) {
        return parts.get(name);
    }

    public Collection<ModelPart> getParts() {
        return parts.values();
    }
}
