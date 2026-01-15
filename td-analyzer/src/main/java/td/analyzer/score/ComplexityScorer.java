package td.analyzer.score;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;

public class ComplexityScorer {

    public static double score(MethodDeclaration md) {
        if (md == null || md.getBody().isEmpty()) return 0.0;

        int branches = md.findAll(IfStmt.class).size()
                + md.findAll(SwitchStmt.class).size()
                + md.findAll(ConditionalExpr.class).size();

        int loops = md.findAll(ForStmt.class).size()
                + md.findAll(ForEachStmt.class).size()
                + md.findAll(WhileStmt.class).size()
                + md.findAll(DoStmt.class).size();

        int returns = md.findAll(ReturnStmt.class).size();
        int catches = md.findAll(CatchClause.class).size();
        int trys = md.findAll(TryStmt.class).size();
        int throwsStmts = md.findAll(ThrowStmt.class).size();
        int breaks = md.findAll(BreakStmt.class).size();
        int continues = md.findAll(ContinueStmt.class).size();

        double s = 0.0;
        s += clamp01(branches / 10.0) * 45.0;
        s += clamp01(loops / 6.0) * 25.0;
        s += clamp01(returns / 6.0) * 12.0;
        s += clamp01((catches + trys) / 5.0) * 10.0;
        s += clamp01((throwsStmts + breaks + continues) / 8.0) * 8.0;

        return round2(s);
    }

    private static double clamp01(double x) {
        return Math.max(0.0, Math.min(1.0, x));
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
