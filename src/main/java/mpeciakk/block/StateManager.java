package mpeciakk.block;

import mpeciakk.block.property.Property;
import mpeciakk.util.Pair;

import java.util.*;

public class StateManager {
    private final List<Property<?>> propertiesOrder = new ArrayList<>();
    private final List<BlockState> states = new ArrayList<>();

    private final Block block;

    public StateManager(Block block) {
        this.block = block;
    }

    private List<List<Object>> cartesianProduct(List<List<?>> lists) {
        List<List<Object>> resultLists = new ArrayList<>();
        if (lists.size() == 0) {
            resultLists.add(new ArrayList<>());
            return resultLists;
        } else {
            List<?> firstList = lists.get(0);
            List<List<Object>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
            for (Object condition : firstList) {
                for (List<Object> remainingList : remainingLists) {
                    ArrayList<Object> resultList = new ArrayList<>();
                    resultList.add(condition);
                    resultList.addAll(remainingList);
                    resultLists.add(resultList);
                }
            }
        }
        return resultLists;
    }

    public void updateStates() {
        List<Property<?>> props = propertiesOrder;

        List<List<?>> valuesList = new ArrayList<>();

        for (Property<?> prop : props) {
            valuesList.add(prop.getValues());
        }

        List<List<Object>> sorted = cartesianProduct(valuesList);

        for (List<Object> values : sorted) {
            Map<Property<?>, Comparable<?>> properties = new HashMap<>();

            for (int i = 0; i < values.size(); i++) {
                properties.put(props.get(i), (Comparable<?>) values.get(i));
            }

            states.add(new BlockState(properties, propertiesOrder, block));
            System.out.println(properties);
        }
    }

    public StateManager addProperty(Property<?> property) {
        this.propertiesOrder.add(property);

        return this;
    }

    public List<BlockState> getStates() {
        return states;
    }
}
