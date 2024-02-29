package levelSolver.optimizedTranspositionTable;

public class SolverOPT {

    
        private long nodeCount;
        private int[] columnOrder;
        private TranspositionTableOPT transTable;

        public SolverOPT() {
            this.nodeCount = 0;
            this.transTable = new TranspositionTableOPT();
            this.columnOrder = new int[PositionOPT.WIDTH];
            resetColumnOrder();
        }

        public int solve(PositionOPT P, boolean weak) {
            if (P.canWinNext()) {
                return (PositionOPT.WIDTH * PositionOPT.HEIGHT + 1 - P.nbMoves()) / 2;
            }
            int min = -(PositionOPT.WIDTH * PositionOPT.HEIGHT - P.nbMoves()) / 2;
            int max = (PositionOPT.WIDTH * PositionOPT.HEIGHT + 1 - P.nbMoves()) / 2;
            if (weak) {
                min = -1;
                max = 1;
            }

            while (min < max) {
                int med = min + (max - min) / 2;
                if (med <= 0 && min / 2 < med) {
                    med = min / 2;
                } else if (med >= 0 && max / 2 > med) {
                    med = max / 2;
                }
                int r = negamax(P, med, med + 1);
                if (r <= med) {
                    max = r;
                } else {
                    min = r;
                }
            }
            return min;
        }

        private int negamax(PositionOPT P, int alpha, int beta) {
            assert (alpha < beta);
            assert (!P.canWinNext());

            nodeCount++;

            long next = P.possibleNonLosingMoves();
            if (next == 0) {
                return -(PositionOPT.WIDTH * PositionOPT.HEIGHT - P.nbMoves()) / 2;
            }

            if (P.nbMoves() >= PositionOPT.WIDTH * PositionOPT.HEIGHT - 2) {
                return 0;
            }

            int min = -(PositionOPT.WIDTH * PositionOPT.HEIGHT - 2 - P.nbMoves()) / 2;
            if (alpha < min) {
                alpha = min;
                if (alpha >= beta) return alpha;
            }

            int max = (PositionOPT.WIDTH * PositionOPT.HEIGHT - 1 - P.nbMoves()) / 2;
            int val = (int) transTable.get(P.key());
            if (val != 0) {
                max = val + PositionOPT.MIN_SCORE - 1;
            }

            if (beta > max) {
                beta = max;
                if (alpha >= beta) return beta;
            }

            MoveSorterOPT moves = new MoveSorterOPT();

            for (int i = PositionOPT.WIDTH; i-- > 0; ) {
                long move = next & PositionOPT.columnMask(columnOrder[i]);
                if (move != 0) {
                    moves.add(move, P.moveScore(move));
                }
            }

            while (true) {
                long nextMove = moves.getNext();
                if (nextMove == 0) {
                    break;
                }

                PositionOPT P2 = PositionOPT.copy(P);
                P2.play(nextMove);
                int score = -negamax(P2, -beta, -alpha);

                if (score >= beta) return score;
                if (score > alpha) alpha = score;
            }

            transTable.put(P.key(), (byte)(alpha - PositionOPT.MIN_SCORE + 1));
            return alpha;
        }

        public long getNodeCount() {
            return nodeCount;
        }

        public void reset() {
            nodeCount = 0;
            transTable.reset();
        }

        private void resetColumnOrder() {
            for (int i = 0; i < PositionOPT.WIDTH; i++) {
                columnOrder[i] = PositionOPT.WIDTH / 2 + (1 - 2 * (i % 2)) * (i + 1) / 2;
            }
        }
    

    public static void main(String[] args) {
        SolverOPT solver = new SolverOPT();
        boolean weak = false;

        if (args.length > 0 && args[0].equals("-w")) {
            weak = true;
        }

        String line;
        int lineNumber = 1;

       
        try {
            line = System.console().readLine();
            PositionOPT P = new PositionOPT();
            int movesProcessed = P.play(line);
            if (movesProcessed != line.length()) {
                System.err.println("Line " + lineNumber + ": Invalid move " + (P.nbMoves() + 1) + " \"" + line + "\"");
            } else {
                solver.reset();
                long startTime = System.currentTimeMillis();
                int score = solver.solve(P, weak);
                long endTime = System.currentTimeMillis();
                System.out.println(line + " Score : " + score + "; Nb noeuds : " + solver.getNodeCount() + "; Temps " + (endTime - startTime));
            }
            lineNumber++;
        } catch (Exception e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    
    }
}
