package mpeciakk.asset;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import mpeciakk.asset.data.BlockStateData;
import mpeciakk.asset.data.ShaderData;
import mpeciakk.model.JsonModel;
import mpeciakk.model.Model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AssetLoader {

    public enum ToNumberPolicy implements ToNumberStrategy {
        INTEGER {
            @Override public Integer readNumber(JsonReader in) throws IOException {
                String value = in.nextString();
                try {
                    return Integer.valueOf(value);
                } catch (NumberFormatException e) {
                    throw new JsonParseException("Cannot parse " + value + "; at path " + in.getPath(), e);
                }
            }
        }
    }

    public final static AssetLoader INSTANCE = new AssetLoader();

    private final Gson blockStateGson = new GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.INTEGER).create();

    public void load() {
        for (String path : getFiles("/shaders/")) {
            String file = path.split("\\.")[0];

            loadShader(file);
        }

        for (String path : getFiles("/textures/")) {
            String file = path.split("\\.")[0];

            loadImage(file);
        }

        AssetManager.INSTANCE.stitchTextures();

        for (String path : getFiles("/models/block/")) {
            String file = path.split("\\.")[0];

            loadModel(file);
        }

        for (String path : getFiles("/blockstates/")) {
            String file = path.split("\\.")[0];

            loadBlockState(file);
        }
    }

    public void loadShader(String shader) {
        String basePath = "/shaders/";
        String shaderPath = basePath + shader + ".glsl";

        String shaderContent = getTextFile(shaderPath);

        AssetManager.INSTANCE.register(AssetType.Shader, shader, new ShaderData(shaderContent));
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

    private void loadModel(String modelName) {
        String basePath = "/models/block/";
        String path = basePath + modelName + ".json";

        Model model = new Model(new Gson().fromJson(getTextFile(path), JsonModel.class));

        AssetManager.INSTANCE.register(AssetType.Model, modelName, model);
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

    public String getTextFile(String path) {
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
}
