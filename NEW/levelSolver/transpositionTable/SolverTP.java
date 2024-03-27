package levelSolver.transpositionTable;

public class SolverTP {

    private long nodeCount; // counter of explored nodes.

    private int[] columnOrder; // column exploration order

    private TranspositionTable transTable;

    private int negamax(PositionTP P, int alpha, int beta) {
        assert alpha < beta;

        nodeCount++; 

        if (P.nbMoves() == PositionTP.WIDTH * PositionTP.HEIGHT) 
            return 0;

        for (int x = 0; x < PositionTP.WIDTH; x++) 
            if (P.canPlay(x) && P.isWinningMove(x))
                return (PositionTP.WIDTH * PositionTP.HEIGHT + 1 - P.nbMoves()) / 2;

        int max = (PositionTP.WIDTH * PositionTP.HEIGHT - 1 - P.nbMoves()) / 2; 
        int val = transTable.get(P.key());
        if (val != 0)
            max = val + PositionTP.MIN_SCORE - 1;

        if (beta > max) {
            beta = max; 
            if (alpha >= beta) return beta; 
        }

        for (int x = 0; x < PositionTP.WIDTH; x++) 
            if (P.canPlay(columnOrder[x])) {
                PositionTP P2 = PositionTP.copy(P);
                P2.play(columnOrder[x]); 
                int score = -negamax(P2, -beta, -alpha); 

                if (score >= beta) return score; // prune the exploration if we find a possible move better than what we were looking for.
                if (score > alpha) alpha = score; // reduce the [alpha;beta] window for the next exploration, as we only
                // need to search for a position that is better than the best so far.
            }
        
        long key = P.key();
        byte value = (byte)(alpha - PositionTP.MIN_SCORE + 1);
        transTable.put(key, value); // save the upper bound of the position
        return alpha;
    }

    public int solve(PositionTP P, boolean weak) {
        if (weak)
            return negamax(P, -1, 1);
        else
            return negamax(P, -PositionTP.WIDTH * PositionTP.HEIGHT / 2, PositionTP.WIDTH * PositionTP.HEIGHT / 2);
    }

    public long getNodeCount() {
        return nodeCount;
    }

    public void reset() {
        nodeCount = 0;
        transTable.reset();
    }

    // Constructor
    public SolverTP() {
        nodeCount = 0;
        transTable = new TranspositionTable(8388593); // 8388593 prime = 64MB of transposition table
        reset();
        columnOrder = new int[PositionTP.WIDTH];
        for (int i = 0; i < PositionTP.WIDTH; i++)
            columnOrder[i] = PositionTP.WIDTH / 2 + (1 - 2 * (i % 2)) * (i + 1) / 2;
    }

    public static long getTimeMicrosec() {
        long now = System.currentTimeMillis();
        return now * 1000;
    }

    public static void main(String[] args) {
        SolverTP solver = new SolverTP();
        boolean weak = false;

        if (args.length > 0 && args[0].equals("-w")) weak = true;

        int l = 1;
        String line;

        java.util.Scanner scanner = new java.util.Scanner(System.in);

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            PositionTP P = new PositionTP();
            int moves = P.play(line);
            if (moves != line.length()) {
                System.err.println("Line " + l + ": Invalid move " + (P.nbMoves() + 1) + " \"" + line + "\"");
            } else {
                solver.reset();
                long start_time = System.nanoTime();
                int score = solver.solve(P, weak);
                long end_time = System.nanoTime();
                System.out.println(line + " Score :" + score + "; Nb noeuds : " + solver.getNodeCount() + "; Temps " + (end_time - start_time) / 1000000);
            }
            l++;
        }
        scanner.close();
    }
}
