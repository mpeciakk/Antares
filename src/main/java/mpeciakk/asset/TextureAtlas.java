package mpeciakk.asset;

import mpeciakk.asset.data.Texture;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12C.GL_BGRA;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL42.glTexStorage2D;

public class TextureAtlas {
    private final int texture = glGenTextures();
    private final int tileSize;
    private int index = 0;
    private int rows;
    private int cols;
    private BufferedImage image;

    public TextureAtlas(int size, int tileSize) {
        this.tileSize = tileSize;
        this.image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        this.rows = image.getHeight() / tileSize;
        this.cols = image.getWidth() / tileSize;
    }

    public Texture addTexture(BufferedImage newImage) {
        int col = index % cols;
        int row = (int) Math.floor((float) index / (float) cols);

        image = appendImage(image, newImage, col * tileSize, row * tileSize);

        int width = getImage().getWidth();
        int height = getImage().getHeight();

        int[] pixelsRaw = getImage().getRGB(0, 0, width, height, null, 0, width);

        ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

        try {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int pixel = pixelsRaw[i * height + j];
                    pixels.put((byte) ((pixel >> 16) & 0xFF));
                    pixels.put((byte) ((pixel >> 8) & 0xFF));
                    pixels.put((byte) (pixel & 0xFF));
                    pixels.put((byte) ((pixel >> 24) & 0xFF));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            pixels.put((byte) 0x88);
            pixels.put((byte) 0x88);
            pixels.put((byte) 0x88);
            pixels.put((byte) 0x00);
        }

        pixels.flip();

        try {
            ImageIO.write(getImage(), "png", new File("blocks.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        glBindTexture(GL_TEXTURE_2D, getTexture());

        glTexStorage2D(GL_TEXTURE_2D, 4, GL_RGBA8, width, height);
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, 0);

        float amount = Math.min(4, glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));

        glTexParameterf(GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);

        glBindTexture(GL_TEXTURE_2D, 0);

        int textureIndex = index;
        index++;

        return new Texture(textureIndex);
    }

    public int getTexture() {
        return texture;
    }

    public BufferedImage getImage() {
        return image;
    }

    private static BufferedImage appendImage(BufferedImage source, BufferedImage image, int x, int y) {
        if (y > source.getHeight() || x > source.getWidth()) {
            System.err.println("Texture coordinates extends atlas size");

            return null;
        }

        int height = source.getHeight();
        int width = source.getWidth();

        BufferedImage concatImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D ctx = concatImage.createGraphics();

        ctx.drawImage(source, 0, 0, null);
        ctx.drawImage(image, x, y, null);

        ctx.dispose();

        return concatImage;
    }

    @Override
    public String toString() {
        return "TextureAtlas{" +
                "texture=" + texture +
                ", tileSize=" + tileSize +
                ", index=" + index +
                ", rows=" + rows +
                ", cols=" + cols +
                ", image=" + image +
                '}';
    }
}