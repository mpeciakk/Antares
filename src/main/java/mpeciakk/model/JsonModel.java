package mpeciakk.model;

import java.util.Map;

public class JsonModel {
    private Element[] elements;

    public Element[] getElements() {
        return elements;
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

            public float[] getUv() {
                return uv;
            }

            public String getTexture() {
                return texture;
            }
        }
    }
}
