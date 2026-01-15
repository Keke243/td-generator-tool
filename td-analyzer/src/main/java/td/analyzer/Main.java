package td.analyzer;

import td.analyzer.analysis.ProjectAnalyzer;
import td.analyzer.config.AnalyzerConfig;
import td.analyzer.model.MethodInfo;
import td.analyzer.yaml.YamlWriter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        AnalyzerConfig cfg = AnalyzerConfig.fromArgs(args);

        if (cfg.showHelp) {
            AnalyzerConfig.printHelp();
            return;
        }

        Path inputPath = Paths.get(cfg.input).toAbsolutePath().normalize();

        ProjectAnalyzer analyzer = new ProjectAnalyzer();
        List<MethodInfo> analyzed = analyzer.analyzeProject(inputPath, cfg.mode);

        if (analyzed == null || analyzed.isEmpty()) {
            System.err.println("Aucune méthode analysable trouvée (src/main/java).");
            return;
        }

        analyzed.sort(Comparator.comparingDouble(MethodInfo::totalScore).reversed());

        int n = Math.min(cfg.top, analyzed.size());
        List<MethodInfo> selected = analyzed.subList(0, n);

        Path yamlOut = Paths.get(cfg.output).toAbsolutePath().normalize();
        YamlWriter.writeYaml(cfg, selected, yamlOut);

        System.out.println("Fichier généré: " + yamlOut);
    }
}
