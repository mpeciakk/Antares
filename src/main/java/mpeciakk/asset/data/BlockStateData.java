package mpeciakk.asset.data;

import java.util.Map;

public class BlockStateData {
    private SingleStateData[] states;

    public SingleStateData[] getStates() {
        return states;
    }

    public static class SingleStateData {
        private Map<String, Object> properties;
        private String model;

        public Map<String, Object> getProperties() {
            return properties;
        }

        public String getModel() {
            return model;
        }
    }
}
