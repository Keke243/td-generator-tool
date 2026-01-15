package td.analyzer.config;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AnalyzerConfig {

    public String input;
    public String output = "td-config.generated.yaml";
    public int top = 15;
    public String mode = "business";
    public boolean showHelp = false;

    public static void printHelp() {
        System.out.println("Usage:");
        System.out.println("  java -jar td-analyzer.jar --config <analyzer-config.yaml> [overrides]");
        System.out.println("  OU");
        System.out.println("  java -jar td-analyzer.jar --input <projectPath> --output <yamlPath> [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --config <file>        Fichier YAML de configuration");
        System.out.println("  --input <path>         Chemin du projet à analyser");
        System.out.println("  --output <file>        Chemin du YAML généré");
        System.out.println("  --top <N>              Nombre de méthodes proposées (défaut 15)");
        System.out.println("  --mode <business|any>  Filtre des méthodes (défaut business)");
        System.out.println("  --help                 Affiche l'aide");
    }

    public static AnalyzerConfig load(Path path) throws Exception {
        if (path == null) throw new IllegalArgumentException("Config path is null");
        if (!Files.exists(path)) throw new IllegalArgumentException("Config file not found: " + path.toAbsolutePath());

        LoaderOptions opts = new LoaderOptions();
        Yaml yaml = new Yaml(new Constructor(AnalyzerConfig.class, opts));

        try (InputStream in = Files.newInputStream(path)) {
            AnalyzerConfig cfg = yaml.load(in);
            if (cfg == null) cfg = new AnalyzerConfig();
            normalize(cfg);
            return cfg;
        }
    }

    public static AnalyzerConfig fromArgs(String[] args) throws Exception {
        String configPath = null;

        Map<String, String> kv = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String a = args[i];

            if ("--help".equals(a) || "-h".equals(a)) {
                AnalyzerConfig c = new AnalyzerConfig();
                c.showHelp = true;
                return c;
            }

            if ("--config".equals(a) && i + 1 < args.length) {
                configPath = args[++i];
                continue;
            }

            if (a.startsWith("--")) {
                String key = a;
                String val = (i + 1 < args.length && !args[i + 1].startsWith("--")) ? args[++i] : "true";
                kv.put(key, val);
            }
        }

        AnalyzerConfig cfg = (configPath != null) ? load(Path.of(configPath)) : new AnalyzerConfig();

        // Overrides CLI
        if (kv.containsKey("--input")) cfg.input = kv.get("--input");
        if (kv.containsKey("--output")) cfg.output = kv.get("--output");
        if (kv.containsKey("--top")) cfg.top = Integer.parseInt(kv.get("--top"));
        if (kv.containsKey("--mode")) cfg.mode = kv.get("--mode");

        normalize(cfg);

        if (cfg.input == null || cfg.input.isBlank()) {
            cfg.showHelp = true;
        }

        if (!"business".equalsIgnoreCase(cfg.mode) && !"any".equalsIgnoreCase(cfg.mode)) {
            throw new IllegalArgumentException("--mode doit être business ou any");
        }

        return cfg;
    }

    private static void normalize(AnalyzerConfig cfg) {
        if (cfg.output == null || cfg.output.isBlank()) cfg.output = "td-config.generated.yaml";
        if (cfg.mode == null || cfg.mode.isBlank()) cfg.mode = "business";
        if (cfg.top <= 0) cfg.top = 15;

        cfg.mode = cfg.mode.trim();
        if (cfg.input != null) cfg.input = cfg.input.trim();
        if (cfg.output != null) cfg.output = cfg.output.trim();
    }
}
