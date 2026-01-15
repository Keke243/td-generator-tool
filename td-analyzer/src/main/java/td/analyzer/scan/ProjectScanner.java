package td.analyzer.scan;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectScanner {

    public record ScannedMethod(
            Path file,
            String packageName,
            String className,
            MethodDeclaration method
    ) {}

    public List<ScannedMethod> scan(Path projectRoot) throws Exception {
        Path srcRoot = projectRoot.resolve("src/main/java");
        List<ScannedMethod> result = new ArrayList<>();

        if (!Files.exists(srcRoot)) {
            return result;
        }

        try (var walk = Files.walk(srcRoot)) {
            walk.filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> {
                    CompilationUnit cu;
                    try {
                        cu = StaticJavaParser.parse(p);
                    } catch (Exception e) {
                        return;
                    }

                    String pkg = cu.getPackageDeclaration()
                            .map(pd -> pd.getNameAsString())
                            .orElse("");

                    for (MethodDeclaration md : cu.findAll(MethodDeclaration.class)) {
                        if (md.getBody().isEmpty()) continue;
                        if (md.isNative()) continue;
                        if (isMainMethod(md)) continue;

                        Optional<ClassOrInterfaceDeclaration> parent =
                                md.findAncestor(ClassOrInterfaceDeclaration.class);
                        if (parent.isEmpty()) continue;

                        result.add(new ScannedMethod(
                                p,
                                pkg,
                                parent.get().getNameAsString(),
                                md
                        ));
                    }
                });
        }

        return result;
    }

    private static boolean isMainMethod(MethodDeclaration md) {
        if (!md.getNameAsString().equals("main")) return false;
        if (!md.isStatic()) return false;
        if (!md.getType().asString().equals("void")) return false;
        if (md.getParameters().size() != 1) return false;
        String t = md.getParameter(0).getType().asString().replace(" ", "");
        return t.equals("String[]") || t.equals("String...");
    }
}
