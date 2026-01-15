package td.analyzer.score;

import td.analyzer.model.MethodInfo;

public class HeuristicScorer {

    /**
     * Heuristiques "pédagogiques" simples :
     * - nombre d'instructions
     * - présence de branches/boucles/catch
     * On favorise les méthodes ni triviales, ni énormes.
     */
    public double score(MethodInfo m) {
        if (m == null) return 0.0;

        int st = Math.max(0, m.getStatementCount());
        int branches = Math.max(0, m.getBranchCount());
        int loops = Math.max(0, m.getLoopCount());
        int catches = Math.max(0, m.getCatchCount());

        double base;
        if (st == 0) base = 0.0;
        else if (st <= 3) base = 0.25;
        else if (st <= 8) base = 0.70;
        else if (st <= 20) base = 1.00;
        else if (st <= 40) base = 0.70;
        else base = 0.45;

        double bonus = 0.0;
        if (branches > 0) bonus += 0.10;
        if (loops > 0) bonus += 0.10;
        if (catches > 0) bonus += 0.05;

        double s = base + bonus;
        return Math.min(1.0, Math.max(0.0, s));
    }
}
