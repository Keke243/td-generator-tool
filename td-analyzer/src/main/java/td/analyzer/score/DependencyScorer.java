package td.analyzer.score;

import td.analyzer.model.MethodInfo;

public class DependencyScorer {

    /**
     * Score basé sur fan-in / fan-out.
     * Intuition: une méthode trop centrale peut être risquée à couper,
     * mais une méthode modérément connectée est souvent intéressante.
     */
    public double score(MethodInfo m) {
        if (m == null) return 0.0;

        int fanIn = Math.max(0, m.getFanIn());
        int fanOut = Math.max(0, m.getFanOut());
        int total = fanIn + fanOut;

        // Courbe simple: max autour de 6~10, baisse si trop faible ou trop forte.
        if (total <= 1) return 0.10;
        if (total <= 3) return 0.35;
        if (total <= 6) return 0.75;
        if (total <= 10) return 1.00;
        if (total <= 15) return 0.70;
        return 0.45;
    }
}
