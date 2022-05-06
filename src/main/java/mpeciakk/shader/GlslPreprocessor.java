package mpeciakk.shader;

import java.util.ArrayList;
import java.util.List;

// Yes, I made custom preprocessor for shaders, I am not mad, am I?
public class GlslPreprocessor {
    public String process(String shader) {
        StringBuilder out = new StringBuilder();

        String[] lines = shader.split("\\n");

        List<String> passVariables = new ArrayList<>();

        for (String line : lines) {
            if (line.startsWith("#section")) {
                String[] elements = line.split(" ");
                String section = elements[1];

                if (section.equals("VERTEX_SHADER")) {
                    out.append("#ifdef VERTEX").append("\n");

                    for (String variable : passVariables) {
                        out.append("out ").append(variable).append("\n");
                    }
                }

                if (section.equals("FRAGMENT_SHADER")) {
                    out.append("#endif").append("\n").append("#ifdef FRAGMENT").append("\n");

                    for (String variable : passVariables) {
                        out.append("in ").append(variable).append("\n");
                    }
                }
            } else if (line.startsWith("varying")) {
                String[] elements = line.split(" ", 2);
                String variable = elements[1];

                passVariables.add(variable);
            } else {
                out.append('\n');
                out.append(line);
            }
        }

        out.append('\n').append("#endif");

        return out.toString();
    }
}
