package levelSolver.finalVersion;

public class Solver {
    public long nodeCount; // compteur des nœuds explorés
    public int[] columnOrder; // ordre d'exploration des colonnes
    public TranspositionTable transTable; // table de transposition

    public int negamax(Position P, int alpha, int beta) {
        assert(alpha < beta);
        assert(!P.canWinNext());

        nodeCount++;

        long possible = P.possibleNonLosingMoves();
        if (possible == 0)
            return -(Position.WIDTH * Position.HEIGHT - P.nbMoves()) / 2;

        if (P.nbMoves() >= Position.WIDTH * Position.HEIGHT - 2)
            return 0;

        int min = -(Position.WIDTH * Position.HEIGHT - 2 - P.nbMoves()) / 2;
        if (alpha < min) {
            alpha = min;
            if (alpha >= beta)
                return alpha;
        }

        int max = (Position.WIDTH * Position.HEIGHT - 1 - P.nbMoves()) / 2;
        if (beta > max) {
            beta = max;
            if (alpha >= beta)
                return beta;
        }

        long key = P.key();
        int val = transTable.get(key);
        if (val != 0) {
            if (val > Position.MAX_SCORE - Position.MIN_SCORE + 1) {
                min = val + 2 * Position.MIN_SCORE - Position.MAX_SCORE - 2;
                if (alpha < min) {
                    alpha = min;
                    if (alpha >= beta)
                        return alpha;
                }
            } else {
                max = val + Position.MIN_SCORE - 1;
                if (beta > max) {
                    beta = max;
                    if (alpha >= beta)
                        return beta;
                }
            }
        }

        MoveSorter moves = new MoveSorter();

        for (int i = Position.WIDTH - 1; i >= 0; i--) {
            long move = possible & Position.columnMask(columnOrder[i]);
            if (move != 0)
                moves.add(move, P.moveScore(move));
        }

        while (true) {
            long next = moves.getNext();
            if (next == 0)
                break;

            Position P2 = Position.copy(P);
            P2.play(next);
            int score = -negamax(P2, -beta, -alpha);

            if (score >= beta) {
                transTable.put(key, (int) (score + Position.MAX_SCORE - 2 * Position.MIN_SCORE + 2));
                return score;
            }
            if (score > alpha)
                alpha = score;
        }

        transTable.put(key, (byte) (alpha - Position.MIN_SCORE + 1));
        return alpha;
    }

    public int solve(Position P, boolean weak) {
        if (P.canWinNext())
            return (Position.WIDTH * Position.HEIGHT + 1 - P.nbMoves()) / 2;

        int min = -(Position.WIDTH * Position.HEIGHT - P.nbMoves()) / 2;
        int max = (Position.WIDTH * Position.HEIGHT + 1 - P.nbMoves()) / 2;

        if (weak) {
            min = -1;
            max = 1;
        }

        while(min < max) {                    // iteratively narrow the min-max exploration window
            int med = min + (max - min)/2;
            if(med <= 0 && min/2 < med) med = min/2;
            else if(med >= 0 && max/2 > med) med = max/2;
            int r = negamax(P, med, med + 1);   // use a null depth window to know if the actual score is greater or smaller than med
            if(r <= med) max = r;
            else min = r;
          }
          return min;
    }

    public long getNodeCount() {
        return nodeCount;
    }

    public void reset() {
        nodeCount = 0;
        transTable = new TranspositionTable(Position.WIDTH * (Position.HEIGHT + 1),
                TranspositionTable.log2(Position.MAX_SCORE - Position.MIN_SCORE + 1) + 2, 23);

        columnOrder = new int[Position.WIDTH];
        for (int i = 0; i < Position.WIDTH; i++)
            columnOrder[i] = Position.WIDTH / 2 + (1 - 2 * (i % 2)) * (i + 1) / 2;
    }
}
