package td.analyzer.analysis;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;

import td.analyzer.model.MethodInfo;
import td.analyzer.scan.ProjectScanner;
import td.analyzer.score.ComplexityScorer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectAnalyzer {

    public List<MethodInfo> analyzeProject(Path projectRoot, String mode) throws Exception {
        ProjectScanner scanner = new ProjectScanner();
        List<ProjectScanner.ScannedMethod> scanned = scanner.scan(projectRoot);

        return scanned.stream()
                .filter(sm -> keepMethod(sm.method(), mode))
                .map(sm -> buildMethodInfo(sm, projectRoot))
                .collect(Collectors.toList());
    }

    private static boolean keepMethod(MethodDeclaration md, String mode) {
        String name = md.getNameAsString();

        if ("business".equalsIgnoreCase(mode)) {
            if (name.startsWith("get") || name.startsWith("set") || name.startsWith("is")) {
                return false;
            }
        }
        return true;
    }

    private static MethodInfo buildMethodInfo(ProjectScanner.ScannedMethod sm, Path projectRoot) {
        MethodDeclaration md = sm.method();

        String classFqn = sm.packageName().isBlank()
                ? sm.className()
                : sm.packageName() + "." + sm.className();

        String paramTypes = md.getParameters().stream()
                .map(p -> p.getType().asString())
                .collect(Collectors.joining(","));

        String id = classFqn + "#" + md.getNameAsString() + "(" + paramTypes + ")";

        MethodInfo mi = new MethodInfo();
        mi.setId(id);
        mi.setClassName(classFqn);
        mi.setMethodName(md.getNameAsString());
        mi.setParamTypes(paramTypes);
        mi.setFilePath(sm.file());

        fillMetrics(mi, md);

        mi.setHeuristicsScore(heuristicsScore(mi));
        mi.setComplexityScore(ComplexityScorer.score(md));
        mi.setDependencyScore(dependencyScore(mi));
        mi.setTestSignalScore(testSignalScore(mi, projectRoot));

        mi.setGlobalScore(globalScore(mi));
        return mi;
    }

    private static void fillMetrics(MethodInfo mi, MethodDeclaration md) {
        BlockStmt body = md.getBody().orElse(null);
        if (body == null) return;

        mi.setStatementCount(body.getStatements().size());

        int branch = md.findAll(IfStmt.class).size()
                + md.findAll(SwitchStmt.class).size()
                + md.findAll(ConditionalExpr.class).size()
                + md.findAll(TryStmt.class).size();
        mi.setBranchCount(branch);

        int loops = md.findAll(ForStmt.class).size()
                + md.findAll(ForEachStmt.class).size()
                + md.findAll(WhileStmt.class).size()
                + md.findAll(DoStmt.class).size();
        mi.setLoopCount(loops);

        mi.setReturnCount(md.findAll(ReturnStmt.class).size());
        mi.setCatchCount(md.findAll(CatchClause.class).size());

        mi.setFanOut(md.findAll(MethodCallExpr.class).size());
        mi.setFanIn(0);
    }

    private static double heuristicsScore(MethodInfo m) {
        double score = 0.0;
        score += clamp01(m.getStatementCount() / 20.0) * 55.0;
        score += clamp01(m.getBranchCount() / 8.0) * 25.0;
        score += clamp01(m.getLoopCount() / 5.0) * 20.0;
        return score;
    }

    private static double dependencyScore(MethodInfo m) {
        double score = 0.0;
        score += clamp01(m.getFanOut() / 20.0) * 80.0;
        score += clamp01(m.getStatementCount() / 25.0) * 20.0;
        return score;
    }

    private static double testSignalScore(MethodInfo m, Path projectRoot) {
        Path testRoot = projectRoot.resolve("src/test/java");
        if (!Files.exists(testRoot)) {
            m.setTestRefs(0);
            return 0.0;
        }

        String needle = m.getMethodName();
        int hits = 0;

        List<Path> tests;
        try (var walk = Files.walk(testRoot)) {
            tests = walk.filter(p -> p.toString().endsWith(".java")).collect(Collectors.toList());
        } catch (IOException e) {
            m.setTestRefs(0);
            return 0.0;
        }

        for (Path t : tests) {
            String txt;
            try {
                txt = Files.readString(t, StandardCharsets.UTF_8);
            } catch (Exception ignored) {
                continue;
            }
            if (txt.contains(needle)) hits++;
        }

        m.setTestRefs(hits);

        if (hits <= 0) return 0.0;
        if (hits == 1) return 60.0;
        return 100.0;
    }

    private static double globalScore(MethodInfo m) {
        double h = m.getHeuristicsScore();
        double c = m.getComplexityScore();
        double d = m.getDependencyScore();
        double t = m.getTestSignalScore();

        return (0.35 * h) + (0.25 * c) + (0.25 * d) + (0.15 * t);
    }

    private static double clamp01(double x) {
        return Math.max(0.0, Math.min(1.0, x));
    }
}
