package mpeciakk.asset.data;

import mpeciakk.model.JsonModel;

import java.util.Map;

public record BlockModelData(String type, Map<String, String> textures, boolean full, JsonModel.Element[] elements) {
}
