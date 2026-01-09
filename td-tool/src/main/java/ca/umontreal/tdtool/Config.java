package ca.umontreal.tdtool;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Config {

    // Chemins
    public String input;
    public String output;

    // business|any
    public String mode = "business";

    // Mode RANDOM (si methods est vide)
    public Integer random = 1;

    // Exclusions (optionnel)
    public List<String> exclude = new ArrayList<>();
    public String excludeFile;

    // Mode MANUAL (si non vide)
    public List<MethodCut> methods = new ArrayList<>();

    // Si true : ignore les méthodes demandées mais introuvables
    public Boolean ignoreMissingMethods = false;

    // Options de génération de l'énoncé étudiant
    public String studentHandoutBaseName = "ENONCE_TD";
    public Boolean generateStudentPdf = true;
    public Boolean generateStudentTxt = true;

    // Sous-structure YAML : définition d'une coupure
    public static class MethodCut {

        public String id;

        // "full" ou "partial"
        public String cut = "full";

        // utilisé si cut=partial
        public Integer keepStatements = 1;

        public MethodCut() {}
    }

    // Chargement YAML
    public static Config load(Path path) throws Exception {
        if (path == null) {
            throw new IllegalArgumentException("Config path is null");
        }
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("Config file not found: " + path.toAbsolutePath());
        }

        LoaderOptions opts = new LoaderOptions();
        Yaml yaml = new Yaml(new Constructor(Config.class, opts));

        try (InputStream in = Files.newInputStream(path)) {
            Config cfg = yaml.load(in);
            if (cfg == null) cfg = new Config();

            if (cfg.mode == null || cfg.mode.isBlank()) cfg.mode = "business";
            cfg.mode = cfg.mode.trim();

            if (cfg.random == null) cfg.random = 1;

            if (cfg.exclude == null) cfg.exclude = new ArrayList<>();
            if (cfg.methods == null) cfg.methods = new ArrayList<>();

            if (cfg.ignoreMissingMethods == null) cfg.ignoreMissingMethods = false;

            if (cfg.studentHandoutBaseName == null || cfg.studentHandoutBaseName.isBlank()) {
                cfg.studentHandoutBaseName = "ENONCE_TD";
            } else {
                cfg.studentHandoutBaseName = cfg.studentHandoutBaseName.trim();
            }

            if (cfg.generateStudentPdf == null) cfg.generateStudentPdf = true;
            if (cfg.generateStudentTxt == null) cfg.generateStudentTxt = true;

            if (cfg.input != null) cfg.input = cfg.input.trim();
            if (cfg.output != null) cfg.output = cfg.output.trim();
            if (cfg.excludeFile != null) cfg.excludeFile = cfg.excludeFile.trim();

            cfg.exclude = cleanStringList(cfg.exclude);

            for (MethodCut mc : cfg.methods) {
                if (mc == null) continue;

                if (mc.id != null) mc.id = mc.id.trim();
                if (mc.cut == null || mc.cut.isBlank()) mc.cut = "full";
                mc.cut = mc.cut.trim().toLowerCase();

                if (mc.keepStatements == null) mc.keepStatements = 1;
            }

            return cfg;
        }
    }

    private static List<String> cleanStringList(List<String> in) {
        List<String> out = new ArrayList<>();
        if (in == null) return out;

        for (String s : in) {
            if (s == null) continue;
            String t = s.trim();
            if (t.isEmpty()) continue;
            out.add(t);
        }
        return out;
    }
}
