package levelSolver.lowerBoundTranspositionTable;


public class SolverVF {

    private long nodeCount; // counter of explored nodes.
    private int[] columnOrder; // column exploration order

    private TranspositionTableVF transTable = new TranspositionTableVF(
        (PositionVF.WIDTH * (PositionVF.HEIGHT + 1)),
        (int)(Math.log(PositionVF.MAX_SCORE - PositionVF.MIN_SCORE + 1 + 2) / Math.log(2)),
        23);
             
    private int negamax(PositionVF P, int alpha, int beta) {
        assert (alpha < beta);
        assert (!P.canWinNext());

        nodeCount++; // increment counter of explored nodes

        long possible = P.possibleNonLosingMoves();
        if (possible == 0) // if no possible non losing move, opponent wins next move
            return -(PositionVF.WIDTH * PositionVF.HEIGHT - P.nbMoves()) / 2;

        if (P.nbMoves() >= PositionVF.WIDTH * PositionVF.HEIGHT - 2) // check for draw game
            return 0;

        int min = -(PositionVF.WIDTH * PositionVF.HEIGHT - 2 - P.nbMoves()) / 2; // lower bound of score as opponent cannot win next move
        if (alpha < min) {
            alpha = min;                     // there is no need to keep beta above our max possible score.
            if (alpha >= beta) return alpha;  // prune the exploration if the [alpha;beta] window is empty.
        }

        int max = (PositionVF.WIDTH * PositionVF.HEIGHT - 1 - P.nbMoves()) / 2; // upper bound of our score as we cannot win immediately
        if (beta > max) {
            beta = max;                     // there is no need to keep beta above our max possible score.
            if (alpha >= beta) return beta;  // prune the exploration if the [alpha;beta] window is empty.
        }

        long key = P.key();
        int val = transTable.get(key);
        if (val != 0) {
            if (val > PositionVF.MAX_SCORE - PositionVF.MIN_SCORE + 1) { 
                min = (val + 2 * PositionVF.MIN_SCORE - PositionVF.MAX_SCORE - 2);
                if (alpha < min) {
                    alpha = min;                     
                    if (alpha >= beta) return alpha;  
                }
            } else { // we have an upper bound
                max = (val + PositionVF.MIN_SCORE - 1);
                if (beta > max) {
                    beta = max;                     
                    if (alpha >= beta) return beta; 
                }
            }
        }

        MoveSorterVF moves = new MoveSorterVF();

        for (int i = PositionVF.WIDTH - 1; i >= 0; i--) {
            long move = possible & PositionVF.columnMask(columnOrder[i]);
            if (move != 0) {
                moves.add(move, P.moveScore(move));
            }
        }

        long next = moves.getNext();
        while (next != 0) {
            PositionVF P2 = PositionVF.copy(P);
            P2.play(next); 
            int score = -negamax(P2, -beta, -alpha); 

            if (score >= beta) {
                
                transTable.put(key, (score + PositionVF.MAX_SCORE - (2 * PositionVF.MIN_SCORE) + 2));
                return score; 
            }
            if (score > alpha) alpha = score; 
            next = moves.getNext();
        }

        transTable.put(key, (alpha - PositionVF.MIN_SCORE + 1)); // save the upper bound of the PositionVF
        return alpha;
    }

    public int solve(PositionVF P, boolean weak) {
        if (P.canWinNext()) // check if win in one move as the Negamax function does not support this case.
            return (PositionVF.WIDTH * PositionVF.HEIGHT + 1 - P.nbMoves()) / 2;
        int min = -(PositionVF.WIDTH * PositionVF.HEIGHT - P.nbMoves()) / 2;
        int max = (PositionVF.WIDTH * PositionVF.HEIGHT + 1 - P.nbMoves()) / 2;
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

    // Constructor
    public SolverVF() {
        reset();
        columnOrder = new int[PositionVF.WIDTH];
        for(int i = 0; i < PositionVF.WIDTH; i++)
            columnOrder[i] = PositionVF.WIDTH/2 + (1-2*(i%2))*(i+1)/2;
        // initialize the column exploration order, starting with center columns
        // example for WIDTH=7: columnOrder = {3, 4, 2, 5, 1, 6, 0}
        
    }

    public long getNodeCount() {
        return nodeCount;
    }

    public void reset() {
        nodeCount = 0;
        transTable.reset();
    }
}

