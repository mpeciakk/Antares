package mpeciakk.asset;

import com.google.gson.*;
import mpeciakk.asset.data.BlockModelData;
import mpeciakk.asset.data.BlockStateData;
import mpeciakk.asset.data.ShadersData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AssetLoader {

    public final static AssetLoader INSTANCE = new AssetLoader();

    private final Gson blockModelGson = new GsonBuilder().registerTypeAdapter(BlockModelData.class, new BlockModelDeserializer()).create();
    private final Gson blockStateGson = new GsonBuilder().create();

    public void load() {
        loadShader("simple");
        loadShader("complex");
        loadShader("text");
        loadShader("chunk");

        for (String path : getFiles("/textures/")) {
            String file = path.split("\\.")[0];

            loadImage(file);
        }

        for (String path : getFiles("/models/block/")) {
            String file = path.split("\\.")[0];

            loadBlockModel(file);
        }

        for (String path : getFiles("/blockstates/")) {
            String file = path.split("\\.")[0];

            loadBlockState(file);
        }
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

    private void loadBlockState(String state) {
        String basePath = "/blockstates/";
        String path = basePath + state + ".json";

        BlockStateData blockModel = blockStateGson.fromJson(getTextFile(path), BlockStateData.class);

        AssetManager.INSTANCE.register(AssetType.BlockState, state, blockModel);
    }

    private List<String> getFiles(String path) {
        List<String> results = new ArrayList<>();

        File[] files = new File[0];
        try {
            files = new File(getResource(path).toURI()).listFiles();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }

        return results;
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

        InputStream inputStream = getResourceStream(path);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! (" + path + ")");
        } else {
            return inputStream;
        }
    }

    private InputStream getResourceStream(String path) {
        URL url = getResource(path);

        if (url == null) {
            System.err.println("Can't find resource " + path);
        }

        try {
            return url.openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private URL getResource(String path) {
        return AssetLoader.class.getResource(path);
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

            return new BlockModelData(json.getAsJsonObject().get("type").getAsString(), texturesMap, json.getAsJsonObject().get("full").getAsBoolean());
        }
    }
}
