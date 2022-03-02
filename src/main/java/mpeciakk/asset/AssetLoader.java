package mpeciakk.asset;

import mpeciakk.asset.data.ShadersData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class AssetLoader {

    public final static AssetLoader INSTANCE = new AssetLoader();

    public void load() {
        loadShader("simple");
        loadShader("complex");
        loadShader("text");
        loadShader("chunk");

        loadImage("cobblestone");
        loadImage("dirt");
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
}
