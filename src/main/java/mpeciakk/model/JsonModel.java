package mpeciakk.model;

import java.util.Map;

public class JsonModel {
    private Element[] elements;
    private Map<String, String> textures;
    private String type;

    public Element[] getElements() {
        return elements;
    }

    public Map<String, String> getTextures() {
        return textures;
    }

    public String getType() {
        return type;
    }

    public static class Element {
        private String name;

        private float[] from;
        private float[] to;

        private Map<String, Face> faces;

        public float[] getFrom() {
            return from;
        }

        public float[] getTo() {
            return to;
        }

        public Map<String, Face> getFaces() {
            return faces;
        }

        public String getName() {
            return name;
        }

        public static class Face {
            private float[] uv;
            private String texture;
            private int rotation;

            public float[] getUv() {
                return uv;
            }

            public String getTexture() {
                return texture;
            }

            public int getRotation() {
                return rotation;
            }
        }
    }
}
