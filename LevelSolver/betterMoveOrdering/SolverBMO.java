package levelSolver.betterMoveOrdering;



public class SolverBMO {
    private long nodeCount;
    private int[] columnOrder;
    private TranspositionTableBMO transTable;
    private int negamax(PositionBMO P, int alpha, int beta) {
        assert alpha < beta;
        assert !P.canWinNext();
        nodeCount++;
        long next = P.possibleNonLosingMoves();
        if (next == 0)
            return -(PositionBMO.WIDTH * PositionBMO.HEIGHT - P.nbMoves()) / 2;
        if (P.nbMoves() >= PositionBMO.WIDTH * PositionBMO.HEIGHT - 2)
            return 0;
        int min = -(PositionBMO.WIDTH * PositionBMO.HEIGHT - 2 - P.nbMoves()) / 2;
        if (alpha < min) {
            alpha = min;
            if (alpha >= beta) return alpha;
        }
        int max = (PositionBMO.WIDTH * PositionBMO.HEIGHT - 1 - P.nbMoves()) / 2;
        int val = transTable.get(P.key());
        if (val != 0)
            max = val + PositionBMO.MIN_SCORE - 1;
        if (beta > max) {
            beta = max;
            if (alpha >= beta) return beta;
        }
        MoveSorter moves = new MoveSorter();
        for (int i = PositionBMO.WIDTH - 1; i >= 0; i--) {
            long move = next & PositionBMO.columnMask(columnOrder[i]);
            if (move != 0)
                moves.add(move, P.moveScore(move));
        }
        while (true) {
            long nextMove = moves.getNext();
            if (nextMove == 0)
                break;
            PositionBMO P2 = PositionBMO.copy(P);
            P2.play(nextMove);
            int score = -negamax(P2, -beta, -alpha);
            if (score >= beta) return score;
            if (score > alpha) alpha = score;
        }
        transTable.put(P.key(), (byte)(alpha - PositionBMO.MIN_SCORE + 1));
        return alpha;
    }
    public int solve(PositionBMO P, boolean weak) {
        if (P.canWinNext())
            return (PositionBMO.WIDTH * PositionBMO.HEIGHT + 1 - P.nbMoves()) / 2;
        int min = -(PositionBMO.WIDTH * PositionBMO.HEIGHT - P.nbMoves()) / 2;
        int max = (PositionBMO.WIDTH * PositionBMO.HEIGHT + 1 - P.nbMoves()) / 2;
        if (weak) {
            min = -1;
            max = 1;
        }
        while (min < max) {
            int med = min + (max - min) / 2;
            if (med <= 0 && min / 2 < med) med = min / 2;
            else if (med >= 0 && max / 2 > med) med = max / 2;
            int r = negamax(P, med, med + 1);
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
    public SolverBMO() {
        nodeCount = 0;
        transTable = new TranspositionTableBMO(8388593);
        reset();
        columnOrder = new int[PositionBMO.WIDTH];
        for (int i = 0; i < PositionBMO.WIDTH; i++)
            columnOrder[i] = PositionBMO.WIDTH / 2 + (1 - 2 * (i % 2)) * (i + 1) / 2;
    }

    public static void main(String[] args) {
        SolverBMO solver = new SolverBMO();

        boolean weak = false;
        if (args.length > 0 && args[0].equals("-w"))
            weak = true;

        int lineNumber = 1;
            
        String line = "624261";
        PositionBMO P = new PositionBMO();
        int playedMoves = P.play(line);
        if (playedMoves != line.length()) {
            System.err.println("Line " + lineNumber + ": Invalid move " + (P.nbMoves() + 1) + " \"" + line + "\"");
        } else {
            solver.reset();
            long startTime = System.currentTimeMillis();
            int score = solver.solve(P, weak);
            long endTime = System.currentTimeMillis();
            System.out.println(line + "; Score : " + score + "; Nb noeud : " + solver.getNodeCount() + "; Temps : " + (endTime - startTime));
        }
    }
}