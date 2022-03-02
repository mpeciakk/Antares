package mpeciakk.font;

import mpeciakk.MinecraftClient;
import mpeciakk.render.mesh.ComplexMesh;
import mpeciakk.render.mesh.Vertex;
import mpeciakk.render.mesh.builder.ComplexMeshBuilder;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class FontManager {

    private final Map<Character, Glyph> glyphs = new HashMap<>();

    private final int texture;
    private int fontHeight;
    private float textureWidth;

    public FontManager() {
        texture = createFontTexture(new Font(Font.MONOSPACED, Font.PLAIN, 16), true);
    }

    private int createFontTexture(Font font, boolean antiAlias) {
        int imageWidth = 0;
        int imageHeight = 0;

        for (int i = 32; i < 256; i++) {
            if (i == 127) {
                continue;
            }

            char c = (char) i;
            BufferedImage ch = createCharImage(font, c, antiAlias);

            if (ch == null) {
                continue;
            }

            imageWidth += ch.getWidth();
            imageHeight = Math.max(imageHeight, ch.getHeight());
        }

        fontHeight = imageHeight;

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        this.textureWidth = imageWidth;

        int x = 0;

        for (int i = 32; i < 256; i++) {
            if (i == 127) {
                continue;
            }

            char c = (char) i;
            BufferedImage charImage = createCharImage(font, c, antiAlias);

            if (charImage == null) {
                continue;
            }

            int charWidth = charImage.getWidth();
            int charHeight = charImage.getHeight();

            Glyph ch = new Glyph(charWidth, charHeight, x, image.getHeight() - charHeight, 0f);
            g.drawImage(charImage, x, 0, null);
            x += ch.width();
            glyphs.put(c, ch);
        }

        AffineTransform transform = AffineTransform.getScaleInstance(1f, -1f);
        transform.translate(0, -image.getHeight());
        AffineTransformOp operation = new AffineTransformOp(transform,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = operation.filter(image, null);

        int width = image.getWidth();
        int height = image.getHeight();

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer buffer = MemoryUtil.memAlloc(width * height * 4);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = pixels[i * width + j];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.flip();

        int fontTexture = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, fontTexture);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glBindTexture(GL_TEXTURE_2D, 0);

        try {
            ImageIO.write(image, "png", new File("font.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        MemoryUtil.memFree(buffer);
        return fontTexture;
    }

    private BufferedImage createCharImage(java.awt.Font font, char c, boolean antiAlias) {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        g.dispose();

        int charWidth = metrics.charWidth(c);
        int charHeight = metrics.getHeight();

        if (charWidth == 0) {
            return null;
        }

        image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(font);
        g.setPaint(java.awt.Color.WHITE);
        g.drawString(String.valueOf(c), 0, metrics.getAscent());
        g.dispose();
        return image;
    }

    public int getWidth(CharSequence text) {
        int width = 0;
        int lineWidth = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\n') {
                width = Math.max(width, lineWidth);
                lineWidth = 0;
                continue;
            }
            if (c == '\r') {
                continue;
            }
            Glyph g = glyphs.get(c);
            lineWidth += g.width();
        }
        width = Math.max(width, lineWidth);
        return width;
    }

    public int getHeight(CharSequence text) {
        int height = 0;
        int lineHeight = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '\n') {
                height += lineHeight;
                lineHeight = 0;
                continue;
            }

            if (c == '\r') {
                continue;
            }

            Glyph g = glyphs.get(c);
            lineHeight = Math.max(lineHeight, g.height());
        }

        height += lineHeight;
        return height;
    }

    public ComplexMesh drawText(CharSequence text, float x, float y) {
        int textHeight = getHeight(text);

        float drawX = x;
        float drawY = y;
        if (textHeight > fontHeight) {
            drawY += textHeight - fontHeight;
        }

        glBindTexture(GL_TEXTURE_2D, texture);

        ComplexMeshBuilder meshBuilder = new ComplexMeshBuilder();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (ch == '\n') {
                drawY -= fontHeight;
                drawX = x;
                continue;
            }

            if (ch == '\r') {
                continue;
            }

            Glyph g = glyphs.get(ch);

            float width = MinecraftClient.getInstance().getWindow().getWidth() / 2f;
            float height = MinecraftClient.getInstance().getWindow().getHeight() / 2f;

            meshBuilder.drawQuad(
                    new Vertex((drawX - width) / width, (drawY - height) / height, 0, g.x() / textureWidth, 0, 0, 0, 0),
                    new Vertex((drawX + g.width() - width) / width, (drawY - height) / height, 0, (g.x() + g.width()) / textureWidth, 0, 0, 0, 0),
                    new Vertex((drawX + g.width() - width) / width, (drawY + g.height() - height) / height, 0, (g.x() + g.width()) / textureWidth, 1, 0, 0, 0),
                    new Vertex((drawX - width) / width, (drawY + g.height() - height) / height, 0, g.x() / textureWidth, 1, 0, 0, 0)
            );

            drawX += g.width();
        }

        return meshBuilder.getMesh();
    }

    public int getTexture() {
        return texture;
    }
}
