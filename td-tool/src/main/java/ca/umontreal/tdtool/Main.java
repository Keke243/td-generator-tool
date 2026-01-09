package ca.umontreal.tdtool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private enum CutLevel { FULL, PARTIAL }

    private static class MethodHandle {
        final Path file;
        final String prettyId;
        final String methodName;
        final String paramTypes;

        MethodHandle(Path file, String prettyId, String methodName, String paramTypes) {
            this.file = file;
            this.prettyId = prettyId;
            this.methodName = methodName;
            this.paramTypes = paramTypes;
        }
    }

    private static class ChosenCut {
        final MethodHandle method;
        final CutLevel level;
        final int keepStatements;

        ChosenCut(MethodHandle method, CutLevel level, int keepStatements) {
            this.method = method;
            this.level = level;
            this.keepStatements = keepStatements;
        }
    }

    public static void main(String[] args) throws Exception {

        String configPath = null;

        String input = null;
        String output = null;
        Integer randomCount = null;
        String mode = null;

        boolean listOnly = false;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--config" -> { if (i + 1 < args.length) configPath = args[++i]; }
                case "--input" -> { if (i + 1 < args.length) input = args[++i]; }
                case "--output" -> { if (i + 1 < args.length) output = args[++i]; }
                case "--random" -> { if (i + 1 < args.length) randomCount = Integer.parseInt(args[++i]); }
                case "--mode" -> { if (i + 1 < args.length) mode = args[++i]; }
                case "--list" -> listOnly = true;
            }
        }

        Config cfg = (configPath != null) ? Config.load(Paths.get(configPath)) : new Config();

        if (input != null) cfg.input = input;
        if (output != null) cfg.output = output;
        if (randomCount != null) cfg.random = randomCount;
        if (mode != null) cfg.mode = mode;

        if (cfg.input == null || cfg.output == null) {
            printUsageAndExit();
        }

        cfg.mode = (cfg.mode == null) ? "" : cfg.mode.trim();
        if (!"business".equalsIgnoreCase(cfg.mode) && !"any".equalsIgnoreCase(cfg.mode)) {
            throw new IllegalArgumentException("--mode must be 'business' or 'any'");
        }

        boolean manualMode = (cfg.methods != null && !cfg.methods.isEmpty());

        if (!manualMode && !listOnly) {
            if (cfg.random == null || cfg.random <= 0) {
                throw new IllegalArgumentException("In random mode, 'random' must be >= 1");
            }
        }

        Set<String> excluded = new HashSet<>();
        if (cfg.exclude != null) excluded.addAll(cfg.exclude);

        if (cfg.excludeFile != null && !cfg.excludeFile.isBlank()) {
            Path exFile = Paths.get(cfg.excludeFile.trim());
            if (Files.exists(exFile)) {
                List<String> lines = Files.readAllLines(exFile, StandardCharsets.UTF_8);
                for (String line : lines) {
                    String s = (line == null) ? "" : line.trim();
                    if (s.isEmpty() || s.startsWith("#")) continue;
                    excluded.add(s);
                }
            } else {
                System.err.println("Warning: excludeFile not found: " + exFile.toAbsolutePath());
            }
        }

        Path in = Paths.get(cfg.input).toAbsolutePath().normalize();
        Path out = Paths.get(cfg.output).toAbsolutePath().normalize();

        System.out.println("Copying reference project from: " + in);
        deleteIfExists(out);
        copyDirectory(in, out);

        Path srcRoot = out.resolve("src/main/java");
        if (!Files.exists(srcRoot)) {
            throw new IllegalStateException("src/main/java not found in output project: " + srcRoot);
        }

        List<MethodHandle> allCandidates = collectCandidateMethods(srcRoot, cfg.mode);
        if (allCandidates.isEmpty()) {
            throw new IllegalStateException("No stubbable methods found under: " + srcRoot + " (mode=" + cfg.mode + ")");
        }

        if (listOnly) {
            printDetectedMethods(allCandidates);
            return;
        }

        Map<String, MethodHandle> byId = allCandidates.stream()
                .collect(Collectors.toMap(m -> m.prettyId, m -> m, (a, b) -> a));

        List<MethodHandle> randomCandidates = allCandidates;
        if (!manualMode && !excluded.isEmpty()) {
            randomCandidates = allCandidates.stream()
                    .filter(m -> !excluded.contains(m.prettyId))
                    .collect(Collectors.toList());
        }

        if (!manualMode && randomCandidates.isEmpty()) {
            throw new IllegalStateException("Random mode: after exclusions, no stubbable methods remain (mode=" + cfg.mode + ")");
        }

        List<ChosenCut> chosenCuts = new ArrayList<>();
        Long seedUsed = null;

        if (manualMode) {
            System.out.println("Selection mode: MANUAL (" + cfg.methods.size() + " method(s) requested)");

            for (Config.MethodCut req : cfg.methods) {
                if (req == null || req.id == null || req.id.isBlank()) continue;

                String reqId = req.id.trim();
                MethodHandle mh = byId.get(reqId);
                if (mh == null) {
                    String msg = "Method not found in project: " + reqId;
                    if (Boolean.TRUE.equals(cfg.ignoreMissingMethods)) {
                        System.err.println("Warning: " + msg);
                        continue;
                    }
                    throw new IllegalArgumentException(msg);
                }

                CutLevel level = parseCutLevel(req.cut);
                int keep = Math.max(0, (req.keepStatements == null ? 1 : req.keepStatements));

                chosenCuts.add(new ChosenCut(mh, level, keep));
            }

            if (chosenCuts.isEmpty()) {
                throw new IllegalStateException("Manual mode: no valid methods selected (check config.yml 'methods')");
            }

        } else {
            System.out.println("Selection mode: RANDOM");

            int n = Math.min(cfg.random, randomCandidates.size());

            seedUsed = new SecureRandom().nextLong();
            Random rng = new Random(seedUsed);
            Collections.shuffle(randomCandidates, rng);

            List<MethodHandle> picked = randomCandidates.subList(0, n);
            for (MethodHandle mh : picked) {
                chosenCuts.add(new ChosenCut(mh, CutLevel.FULL, 0));
            }

            System.out.println("Selected " + n + " method(s) to stub (seed=" + seedUsed + ")");
        }

        for (ChosenCut cc : chosenCuts) {
            System.out.println("  - " + cc.method.prettyId + "  [" + cc.level
                    + (cc.level == CutLevel.PARTIAL ? (", keep=" + cc.keepStatements) : "") + "]");
        }

        Map<Path, List<ChosenCut>> byFileCuts = chosenCuts.stream()
                .collect(Collectors.groupingBy(cc -> cc.method.file));

        for (Map.Entry<Path, List<ChosenCut>> entry : byFileCuts.entrySet()) {
            applyCutsInFile(entry.getKey(), entry.getValue());
        }

        List<StudentHandout.StubbedMethod> stubbed = chosenCuts.stream()
                .map(cc -> new StudentHandout.StubbedMethod(
                        cc.method.prettyId,
                        cc.level.toString().toLowerCase(),
                        cc.level == CutLevel.PARTIAL ? cc.keepStatements : null
                ))
                .collect(Collectors.toList());

        StudentHandout.generate(out, cfg, stubbed);

        System.out.println("Done. Generated exercise at: " + out);

        String base = (cfg.studentHandoutBaseName == null || cfg.studentHandoutBaseName.isBlank())
                ? "ENONCE_TD"
                : cfg.studentHandoutBaseName;

        if (Boolean.TRUE.equals(cfg.generateStudentTxt)) {
            System.out.println("Handout TXT: " + out.resolve(base + ".txt"));
        }
        if (Boolean.TRUE.equals(cfg.generateStudentPdf)) {
            System.out.println("Handout PDF: " + out.resolve(base + ".pdf"));
        }

        System.out.println("Next: cd \"" + out + "\" && mvn test");
    }

    private static void printUsageAndExit() {
        System.err.println("Usage:");
        System.err.println("  java -jar td-tool.jar --config <file.yaml> [--list]");
        System.err.println("  OR");
        System.err.println("  java -jar td-tool.jar --input <path> --output <path> --random <N> [--mode business|any] [--list]");
        System.err.println("");
        System.err.println("Options:");
        System.err.println("  --list   List method IDs detected in the project, then exit.");
        System.exit(2);
    }

    private static void printDetectedMethods(List<MethodHandle> allCandidates) {
        System.out.println("=== Methods detected by td-tool (IDs to copy/paste into YAML) ===");
        allCandidates.stream()
                .map(m -> m.prettyId)
                .sorted()
                .forEach(System.out::println);
        System.out.println("=== Total: " + allCandidates.size() + " ===");
        System.out.println("Note: if a method is not found, check parameter types (e.g., int vs Integer), overloads,");
        System.out.println("or whether the file is under src/main/java.");
    }

    // --------------------- Cut logic ---------------------

    private static CutLevel parseCutLevel(String s) {
        if (s == null) return CutLevel.FULL;
        return switch (s.trim().toLowerCase()) {
            case "partial" -> CutLevel.PARTIAL;
            case "full" -> CutLevel.FULL;
            default -> CutLevel.FULL;
        };
    }

    private static void applyCutsInFile(Path file, List<ChosenCut> cuts) throws IOException {
        CompilationUnit cu = StaticJavaParser.parse(file);

        Map<String, ChosenCut> wanted = new HashMap<>();
        for (ChosenCut cc : cuts) {
            String key = cc.method.methodName + "|" + cc.method.paramTypes;
            wanted.put(key, cc);
        }

        for (MethodDeclaration md : cu.findAll(MethodDeclaration.class)) {
            if (md.getBody().isEmpty()) continue;

            String name = md.getNameAsString();
            String paramTypes = md.getParameters().stream()
                    .map(pr -> pr.getType().asString())
                    .collect(Collectors.joining(","));

            String key = name + "|" + paramTypes;
            ChosenCut cc = wanted.get(key);
            if (cc == null) continue;

            if (cc.level == CutLevel.FULL) {
                md.setBody(fullStubBody());
            } else {
                md.setBody(partialStubBody(md.getBody().get(), cc.keepStatements));
            }
        }

        Files.writeString(file, cu.toString(), StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static BlockStmt fullStubBody() {
        BlockStmt stub = new BlockStmt();
        ObjectCreationExpr ex = new ObjectCreationExpr()
                .setType("UnsupportedOperationException")
                .addArgument(new StringLiteralExpr("TODO"));
        stub.addStatement(new ThrowStmt(ex));
        return stub;
    }

    /**
     * Partial cut:
     * keeps up to N non-terminal statements, then appends a TODO exception.
     * This avoids generating unreachable code after return/throw/break/continue.
     */
    private static BlockStmt partialStubBody(BlockStmt original, int keepStatements) {
        BlockStmt stub = new BlockStmt();

        if (keepStatements > 0) {
            int kept = 0;
            for (Statement st : original.getStatements()) {
                if (kept >= keepStatements) break;
                if (isTerminalStatement(st)) break;
                stub.addStatement(st.clone());
                kept++;
            }
        }

        ObjectCreationExpr ex = new ObjectCreationExpr()
                .setType("UnsupportedOperationException")
                .addArgument(new StringLiteralExpr("TODO"));
        stub.addStatement(new ThrowStmt(ex));

        return stub;
    }

    private static boolean isTerminalStatement(Statement st) {
        return st.isReturnStmt()
                || st.isThrowStmt()
                || st.isBreakStmt()
                || st.isContinueStmt();
    }

    // --------------------- JavaParser scanning ---------------------

    private static List<MethodHandle> collectCandidateMethods(Path srcRoot, String mode) throws IOException {
        List<MethodHandle> methods = new ArrayList<>();

        Files.walk(srcRoot)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> {
                    try {
                        CompilationUnit cu = StaticJavaParser.parse(p);

                        String pkg = cu.getPackageDeclaration()
                                .map(pd -> pd.getNameAsString())
                                .orElse("");

                        for (MethodDeclaration md : cu.findAll(MethodDeclaration.class)) {
                            if (md.getBody().isEmpty()) continue;
                            if (md.isNative()) continue;
                            if (isMainMethod(md)) continue;

                            if ("business".equalsIgnoreCase(mode)) {
                                String name = md.getNameAsString();
                                if (name.startsWith("get") || name.startsWith("set") || name.startsWith("is")) {
                                    continue;
                                }
                            }

                            Optional<ClassOrInterfaceDeclaration> parentClass =
                                    md.findAncestor(ClassOrInterfaceDeclaration.class);
                            if (parentClass.isEmpty()) continue;

                            String className = parentClass.get().getNameAsString();
                            String fqn = pkg.isBlank() ? className : (pkg + "." + className);

                            String paramTypes = md.getParameters().stream()
                                    .map(pr -> pr.getType().asString())
                                    .collect(Collectors.joining(","));

                            String prettyId = fqn + "#" + md.getNameAsString() + "(" + paramTypes + ")";

                            methods.add(new MethodHandle(p, prettyId, md.getNameAsString(), paramTypes));
                        }
                    } catch (Exception e) {
                        System.err.println("Warning: could not parse " + p + " (" + e.getMessage() + ")");
                    }
                });

        return methods;
    }

    private static boolean isMainMethod(MethodDeclaration md) {
        if (!md.getNameAsString().equals("main")) return false;
        if (!md.isStatic()) return false;
        if (!md.getType().asString().equals("void")) return false;
        if (md.getParameters().size() != 1) return false;
        String t = md.getParameter(0).getType().asString().replace(" ", "");
        return t.equals("String[]") || t.equals("String...");
    }

    // --------------------- filesystem helpers ---------------------

    private static void deleteIfExists(Path path) throws IOException {
        if (!Files.exists(path)) return;

        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void copyDirectory(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path rel = source.relativize(dir);
                Files.createDirectories(target.resolve(rel));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path rel = source.relativize(file);
                Files.copy(file, target.resolve(rel), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
