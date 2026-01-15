package td.analyzer.yaml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import td.analyzer.config.AnalyzerConfig;
import td.analyzer.model.MethodInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YamlWriter {

    public static void writeYaml(AnalyzerConfig cfg, List<MethodInfo> selected, Path outFile)
            throws IOException {

        Map<String, Object> root = new LinkedHashMap<>();
        root.put("input", cfg.input);
        root.put("output", "OUTPUT_EXERCISE_PATH_HERE");
        root.put("mode", "any");

        List<Map<String, Object>> methods = new ArrayList<>();

        for (MethodInfo m : selected) {
            double score = round2(m.totalScore());

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("id", m.getPrettyId());
            entry.put("score", score);

            if (score >= 30.0) {
                entry.put("cut", "full");
            } else {
                entry.put("cut", "partial");
                entry.put("keepStatements", computeKeepStatements(score));
            }

            methods.add(entry);
        }

        root.put("methods", methods);
        root.put("ignoreMissingMethods", false);
        root.put("exclude", List.of());

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);
        options.setIndicatorIndent(0);
        options.setWidth(120);

        Yaml yaml = new Yaml(options);
        String content = yaml.dump(root);

        if (outFile.getParent() != null) {
            Files.createDirectories(outFile.getParent());
        }
        Files.writeString(outFile, content);
    }

    private static int computeKeepStatements(double score) {
        if (score < 5.0) return 1;
        if (score < 10.0) return 2;
        if (score < 15.0) return 3;
        if (score < 20.0) return 3;
        if (score < 25.0) return 4;
        return 5; // score < 30.0
    }

    private static double round2(double x) {
        return Math.round(x * 100.0) / 100.0;
    }
}
