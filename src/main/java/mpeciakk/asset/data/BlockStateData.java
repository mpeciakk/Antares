package mpeciakk.asset.data;

import java.util.Arrays;
import java.util.Map;

public class BlockStateData {
    private SingleStateData[] states;

    public SingleStateData[] getStates() {
        return states;
    }

    @Override
    public String toString() {
        return "BlockStateData{" +
                "states=" + Arrays.toString(states) +
                '}';
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

        @Override
        public String toString() {
            return "SingleStateData{" +
                    "properties=" + properties +
                    ", model='" + model + '\'' +
                    '}';
        }
    }
}
