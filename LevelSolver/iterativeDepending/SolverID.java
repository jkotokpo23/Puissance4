package levelSolver.iterativeDepending;


public class SolverID {

    private long nodeCount; // counter of explored nodes.

    private final int[] columnOrder; // column exploration order

    private final TranspositionTableID transTable;

    private int negamax(PositionID P, int alpha, int beta) {
        assert alpha < beta;
        nodeCount++; // increment the counter of explored nodes

        if (P.nbMoves() == PositionID.WIDTH * PositionID.HEIGHT) // check for a draw game
            return 0;

        for (int x = 0; x < PositionID.WIDTH; x++) // check if the current player can win the next move
            if (P.canPlay(x) && P.isWinningMove(x))
                return (PositionID.WIDTH * PositionID.HEIGHT + 1 - P.nbMoves()) / 2;

        int max = (PositionID.WIDTH * PositionID.HEIGHT - 1 - P.nbMoves()) / 2; // upper bound of our score as we cannot win immediately
        int val = transTable.get(P.key());
        if (val != 0)
            max = val + PositionID.MIN_SCORE - 1;

        if (beta > max) {
            beta = max; // there is no need to keep beta above our max possible score.
            if (alpha >= beta) return beta; // prune the exploration if the [alpha;beta] window is empty.
        }

        for (int x = 0; x < PositionID.WIDTH; x++)
            if (P.canPlay(columnOrder[x])) {
                PositionID P2 = PositionID.copy(P);
                P2.play(columnOrder[x]); 
                int score = -negamax(P2, -beta, -alpha); 

                if (score >= beta) return score; 
                if (score > alpha) alpha = score; 
            }

        transTable.put(P.key(), (byte) (alpha - PositionID.MIN_SCORE + 1)); // save the upper bound of the position
        return alpha;
    }

    public int solve(PositionID P, boolean weak) {
        int min = -(PositionID.WIDTH * PositionID.HEIGHT - P.nbMoves()) / 2;
        int max = (PositionID.WIDTH * PositionID.HEIGHT + 1 - P.nbMoves()) / 2;
        if (weak) {
            min = -1;
            max = 1;
        }

        while (min < max) { // iteratively narrow the min-max exploration window
            int med = min + (max - min) / 2;
            if (med <= 0 && min / 2 < med) med = min / 2;
            else if (med >= 0 && max / 2 > med) med = max / 2;
            int r = negamax(P, med, med + 1); // use a null-depth window to know if the actual score is greater or smaller than med
            if (r <= med) max = r;
            else min = r;
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

    // Constructor
    public SolverID() {
        nodeCount = 0;
        transTable = new TranspositionTableID(8388593); // 8388593 prime = 64MB of transposition table
        reset();
        columnOrder = new int[PositionID.WIDTH];
        for (int i = 0; i < PositionID.WIDTH; i++)
            columnOrder[i] = PositionID.WIDTH / 2 + (1 - 2 * (i % 2)) * (i + 1) / 2;
       
    }

    public static void main(String[] args) {
        SolverID solver = new SolverID();

        boolean weak = false;
        if (args.length > 1 && args[1].charAt(0) == '-' && args[1].charAt(1) == 'w') weak = true;

        int l = 1;
        String line;
        while ((line = System.console().readLine()) != null) {
            PositionID P = new PositionID();
            if (P.play(line) != line.length()) {
                System.err.println("Line " + l + ": Invalid move " + (P.nbMoves() + 1) + " \"" + line + "\"");
            } else {
                solver.reset();
                long start_time = System.nanoTime();
                int score = solver.solve(P, weak);
                long end_time = System.nanoTime();
                System.out.println(line + "; Score : " + score + "; Nb noeud : " + solver.getNodeCount() + "; Temps : " + (end_time - start_time) / 1000000);
            }
            l++;
        }
    }

    private static long getTimeMicrosec() {
        long now = System.currentTimeMillis() * 1000L;
        return now;
    }
}



