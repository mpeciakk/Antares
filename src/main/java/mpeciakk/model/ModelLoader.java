package mpeciakk.model;

import com.google.gson.Gson;

public class ModelLoader {
    public Model loadJsonModel(String path) {
        Gson gson = new Gson();

        JsonModel jsonModel = gson.fromJson("", JsonModel.class);

        Model model = new Model();

//        for (int i = 0; i < jsonModel.getElements().length; i++) {
//            JsonModel.Element element = jsonModel.getElements()[i];
//
//            entityModel.addPart(new ModelPart(element.getFrom(), element.getTo(), element.getFaces(), texture));
//        }

        for (JsonModel.Element element : jsonModel.getElements()) {
            model.addPart(new ModelPart(element.getFrom(), element.getTo(), element.getName(), element.getFaces(), null));
        }

        return model;
    }
}

