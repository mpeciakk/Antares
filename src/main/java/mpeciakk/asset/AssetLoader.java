package mpeciakk.asset;

import com.google.gson.*;
import mpeciakk.asset.data.BlockModelData;
import mpeciakk.asset.data.ShadersData;
import mpeciakk.world.block.BlockModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AssetLoader {

    public final static AssetLoader INSTANCE = new AssetLoader();

    private final Gson blockModelGson = new GsonBuilder().registerTypeAdapter(BlockModelData.class, new BlockModelDeserializer()).create();

    public void load() {
        loadShader("simple");
        loadShader("complex");
        loadShader("text");
        loadShader("chunk");

        loadImage("cobblestone");
        loadImage("dirt");

        loadBlockModel("cobblestone");
        loadBlockModel("air");
    }

    public void loadShader(String shader) {
        String basePath = "/shaders/";
        String fsPath = basePath + shader + ".fs.glsl";
        String vsPath = basePath + shader + ".vs.glsl";

        String fs = getTextFile(fsPath);
        String vs = getTextFile(vsPath);

        AssetManager.INSTANCE.register(AssetType.Shader, shader, new ShadersData(fs, vs));
    }

    private void loadImage(String texture) {
        String basePath = "/textures/";
        String path = basePath + texture + ".png";

        BufferedImage image = null;

        try {
            image = ImageIO.read(getFileStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        AssetManager.INSTANCE.register(AssetType.Image, texture, image);
    }

    private void loadBlockModel(String model) {
        String basePath = "/models/block/";
        String path = basePath + model + ".json";

        BlockModelData blockModel = blockModelGson.fromJson(getTextFile(path), BlockModelData.class);

        AssetManager.INSTANCE.register(AssetType.BlockModel, model, blockModel);
    }

    private String getTextFile(String path) {
        try (InputStreamReader streamReader = new InputStreamReader(getFileStream(path), StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new IllegalArgumentException("Can't read file! (" + path + ")");
    }

    private InputStream getFileStream(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        InputStream inputStream = AssetLoader.class.getResourceAsStream(path);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! (" + path + ")");
        } else {
            return inputStream;
        }
    }

    public static class BlockModelDeserializer implements JsonDeserializer<BlockModelData> {

        @Override
        public BlockModelData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject textures = json.getAsJsonObject().get("textures").getAsJsonObject();
            Set<String> textureKeys = textures.keySet();

            Map<String, String> texturesMap = new HashMap<>();

            for (String textureKey : textureKeys) {
                texturesMap.put(textureKey, textures.get(textureKey).getAsString());
            }

            return new BlockModelData(json.getAsJsonObject().get("type").getAsString(), texturesMap);
        }
    }
}
