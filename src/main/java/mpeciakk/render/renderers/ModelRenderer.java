package mpeciakk.render.renderers;

import mpeciakk.model.Model;
import mpeciakk.model.ModelPart;
import mpeciakk.shader.ComplexShader;

public class ModelRenderer extends MeshRenderer<Model> {

    public ModelRenderer() {
        super(new ComplexShader());
    }

    @Override
    public void render(Model model) {
        for (ModelPart part : model.getParts()) {
            shader.start();
            shader.loadTransformationMatrix(part.getTransformationMatrix());
            shader.stop();
            render(part.getMesh());
        }
    }

    @Override
    protected int bindTexture() {
        return 0;
    }
}
