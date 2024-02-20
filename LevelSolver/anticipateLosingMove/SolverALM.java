package levelSolver.anticipateLosingMove;



public class SolverALM {

    private long nodeCount;
    private int[] columnOrder;
    private TranspositionTableALM transTable;

    private int negamax(PositionALM P, int alpha, int beta) {
        assert (alpha < beta);
        assert (!P.canWinNext());

        nodeCount++;

        long next = P.possibleNonLosingMoves();
        if (next == 0)
            return -(PositionALM.WIDTH * PositionALM.HEIGHT - P.nbMoves()) / 2;

        if (P.nbMoves() >= PositionALM.WIDTH * PositionALM.HEIGHT - 2)
            return 0;

        int min = -(PositionALM.WIDTH * PositionALM.HEIGHT - 2 - P.nbMoves()) / 2;
        if (alpha < min) {
            alpha = min;
            if (alpha >= beta)
                return alpha;
        }

        int max = (PositionALM.WIDTH * PositionALM.HEIGHT - 1 - P.nbMoves()) / 2;
        int val = transTable.get(P.key());
        if (val != 0)
            max = val + PositionALM.MIN_SCORE - 1;

        if (beta > max) {
            beta = max;
            if (alpha >= beta)
                return beta;
        }

        for (int x = 0; x < PositionALM.WIDTH; x++)
            if ((next & PositionALM.columnMask(columnOrder[x])) != 0) {
                PositionALM P2 = PositionALM.copy(P);
                P2.play(columnOrder[x]);
                int score = -negamax(P2, -beta, -alpha);

                if (score >= beta)
                    return score;
                if (score > alpha)
                    alpha = score;
            }

        transTable.put(P.key(), (byte) (alpha - PositionALM.MIN_SCORE + 1));
        return alpha;
    }

    public int solve(PositionALM P, boolean weak) {
        if (P.canWinNext())
            return (PositionALM.WIDTH * PositionALM.HEIGHT + 1 - P.nbMoves()) / 2;
        int min = -(PositionALM.WIDTH * PositionALM.HEIGHT - P.nbMoves()) / 2;
        int max = (PositionALM.WIDTH * PositionALM.HEIGHT + 1 - P.nbMoves()) / 2;
        if (weak) {
            min = -1;
            max = 1;
        }

        while (min < max) {
            int med = min + (max - min) / 2;
            if (med <= 0 && min / 2 < med)
                med = min / 2;
            else if (med >= 0 && max / 2 > med)
                med = max / 2;
            int r = negamax(P, med, med + 1);
            if (r <= med)
                max = r;
            else
                min = r;
        }
        return min;
    }

    public long getNodeCount() {
        return nodeCount;
    }

    public void reset() {
        nodeCount = 0;
        transTable.reset();
    }

    public SolverALM() {
        nodeCount = 0;
        transTable = new TranspositionTableALM(8388593);

        reset();

        columnOrder = new int[PositionALM.WIDTH];
        for (int i = 0; i < PositionALM.WIDTH; i++)
            columnOrder[i] = PositionALM.WIDTH / 2 + (1 - 2 * (i % 2)) * (i + 1) / 2;
    }

    public static void main(String[] args) {
        SolverALM solver = new SolverALM();
        boolean weak = false;

        if (args.length > 0 && args[0].equals("-w")) {
            weak = true;
        }

        String line;
        int l = 1;

        while ((line = System.console().readLine()) != null) {
            PositionALM P = new PositionALM();

            int moves = P.play(line);
            if (moves != line.length()) {
                System.err.println("Line " + l + ": Invalid move " + (P.nbMoves() + 1) + " \"" + line + "\"");
            } else {
                solver.reset();
                long startTime = System.nanoTime();
                int score = solver.solve(P, weak);
                long endTime = System.nanoTime();
                System.out.println(line + " Scrore : " + score + " ; Nb noeud : " + solver.getNodeCount() + " ; Temps : " + (endTime - startTime) / 1000);
            }
            l++;
        }
    }


}
