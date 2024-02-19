package levelSolver.bitboard;


public class SolverBitboard {
    private long nodeCount; // counter of explored nodes.
    private int[] columnOrder; // column exploration order
    
    private int negamax(PositionBitboard P, int alpha, int beta) {
        assert (alpha < beta);
        nodeCount++; // increment counter of explored nodes

        if (P.nbMoves() == PositionBitboard.WIDTH * PositionBitboard.HEIGHT) // check for draw game
            return 0;

        for (int x = 0; x < Position.WIDTH; x++) // check if the current player can win the next move
            if (P.canPlay(x) && P.isWinningMove(x))
                return (PositionBitboard.WIDTH * PositionBitboard.HEIGHT + 1 - P.nbMoves()) / 2;

        int max = (PositionBitboard.WIDTH * PositionBitboard.HEIGHT - 1 - P.nbMoves()) / 2; // upper bound of our score as we cannot win immediately
        if (beta > max) {
            beta = max;                     // there is no need to keep beta above our max possible score.
            if (alpha >= beta) return beta;  // prune the exploration if the [alpha;beta] window is empty.
        }

        for (int x = 0; x < PositionBitboard.WIDTH; x++) // compute the score of all possible next moves and keep the best one
            if (P.canPlay(columnOrder[x])) {
                PositionBitboard P2 = PositionBitboard.copy(P);
                P2.play(columnOrder[x]);               
                int score = -negamax(P2, -beta, -alpha); 

                if (score >= beta) return score;  
                if (score > alpha) alpha = score; 
            }

        return alpha;
    }

    public int solve(PositionBitboard P, boolean weak) {
        nodeCount = 0;
        if (weak)
            return negamax(P, -1, 1);
        else
            return negamax(P, -Position.WIDTH * Position.HEIGHT / 2, Position.WIDTH * Position.HEIGHT / 2);
    }

    public long getNodeCount() {
        return nodeCount;
    }

    // Constructor
    public SolverBitboard() {
        nodeCount = 0;
        columnOrder = new int[Position.WIDTH];
        for (int i = 0; i < Position.WIDTH; i++)
            columnOrder[i] = Position.WIDTH / 2 + (1 - 2 * (i % 2)) * (i + 1) / 2; 
    }

    public static void main(String[] args) {
        SolverBitboard solver = new SolverBitboard();
        boolean weak = false;
    

        String line;
        int l = 1;

        while ((line = System.console().readLine()) != null) {
            PositionBitboard P = new PositionBitboard();
            if (P.play(line) != line.length()) {
                System.err.println("Line " + l + ": Invalid move " + (P.nbMoves() + 1) + " \"" + line + "\"");
            } else {
                long startTime = System.nanoTime();
                int score = solver.solve(P, weak);
                long endTime = System.nanoTime();
                System.out.println(line + " Score :" + score + "; Nb noeuds : " + solver.getNodeCount() + "; Temps : " + (endTime - startTime) / 1000);
            }
            l++;
        }
    }

}

class Position {
    public static final int WIDTH = 7;  // Placeholder value, replace with actual width
    public static final int HEIGHT = 6; // Placeholder value, replace with actual height

    private long current_position;
    private long mask;
    private int moves; // number of moves played since the beginning of the game.

    public boolean canPlay(int col) {
        return (mask & topMask(col)) == 0;
    }

    /**
     * Plays a playable column.
     * This function should not be called on a non-playable column or a column making an alignment.
     *
     * @param col: 0-based index of a playable column.
     */
    public void play(int col) {
        current_position ^= mask;
        mask |= mask + bottomMask(col);
        moves++;
    }

    public int play(String seq) {
        for (int i = 0; i < seq.length(); i++) {
            int col = seq.charAt(i) - '1';
            if (col < 0 || col >= Position.WIDTH || !canPlay(col) || isWinningMove(col)) return i; // invalid move
            play(col);
        }
        return seq.length();
    }

    public boolean isWinningMove(int col) {
        long pos = current_position;
        pos |= (mask + bottomMask(col)) & columnMask(col);
        return alignment(pos);
    }

    /**
     * @return number of moves played from the beginning of the game.
     */
    public int nbMoves() {
        return moves;
    }

    /**
     * @return a compact representation of a position on WIDTH*(HEIGHT+1) bits.
     */
    public long key() {
        return current_position + mask;
    }

    /**
     * Default constructor, build an empty position.
     */
    public Position() {
        current_position = 0;
        mask = 0;
        moves = 0;
    }

    private boolean alignment(long pos) {
        // Implementation of alignment...
        return false; // Placeholder, replace with actual implementation
    }

    // return a bitmask containing a single 1 corresponding to the top cell of a given column
    private long topMask(int col) {
        return (1L << (HEIGHT - 1)) << col * (HEIGHT + 1);
    }

    // return a bitmask containing a single 1 corresponding to the bottom cell of a given column
    private long bottomMask(int col) {
        return 1L << col * (HEIGHT + 1);
    }

    // return a bitmask 1 on all the cells of a given column
    private long columnMask(int col) {
        return ((1L << HEIGHT) - 1) << col * (HEIGHT + 1);
    }
}
