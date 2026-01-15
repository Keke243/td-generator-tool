package td.analyzer.score;

import td.analyzer.model.MethodInfo;

public class TestSignalScorer {

    /**
     * Score basé sur la présence de tests qui référencent la méthode.
     * Plus il y a de références dans src/test/java, plus c'est intéressant.
     */
    public double score(MethodInfo m) {
        if (m == null) return 0.0;

        int refs = Math.max(0, m.getTestRefs());

        if (refs == 0) return 0.05;
        if (refs == 1) return 0.35;
        if (refs <= 3) return 0.70;
        if (refs <= 6) return 0.90;
        return 1.00;
    }
}
