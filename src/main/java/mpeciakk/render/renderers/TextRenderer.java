package mpeciakk.render.renderers;

import mpeciakk.font.FontManager;
import mpeciakk.shader.TextShader;

public class TextRenderer extends MeshRenderer<String> {

    private final FontManager fontManager;

    public TextRenderer(FontManager fontManager) {
        super(new TextShader());

        this.fontManager = fontManager;
    }

    public void render(String text) {
        render(fontManager.drawText(text, 0, 0));
    }

    public void render(String text, float x, float y) {
        render(fontManager.drawText(text, x, y));
    }

    @Override
    protected int bindTexture() {
        return fontManager.getTexture();
    }
}
