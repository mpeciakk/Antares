package mpeciakk.asset.data;

import java.util.Map;

public record BlockModelData(String type, Map<String, String> textures, boolean full) {
}
