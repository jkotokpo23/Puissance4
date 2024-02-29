package levelSolver.lowerBoundTranspositionTable;

public class PositionVF {

    public static final int WIDTH = 7;
    public static final int HEIGHT = 6;
    public static final int MIN_SCORE = -(WIDTH * HEIGHT) / 2 + 3;
    public static final int MAX_SCORE = (WIDTH * HEIGHT + 1) / 2 - 3;

    private long currentPosition; // bitmap of the current_player stones
    private long mask;            // bitmap of all the already played spots
    private int moves;             // number of moves played since the beginning of the game.

    /**
     * Plays a possible move given by its bitmap representation.
     *
     * @param move: a possible move given by its bitmap representation.
     *        only one bit of the bitmap should be set to 1.
     *        the move should be a valid possible move for the current player.
     */
    public void play(long move) {
        currentPosition ^= mask;
        mask |= move;
        moves++;
    }

    public static PositionVF copy(PositionVF p){
        PositionVF pos = new PositionVF();
        pos.currentPosition = p.currentPosition;
        pos.mask = p.mask;
        pos.moves = p.moves;
        return pos;
    }
    public int play(String seq) {
        for (int i = 0; i < seq.length(); i++) {
            int col = Character.getNumericValue(seq.charAt(i)) - 1;
            if (col < 0 || col >= WIDTH || !canPlay(col) || isWinningMove(col)) {
                return i; // invalid move
            }
            playCol(col);
        }
        return seq.length();
    }

    /*
     * Return true if the current player can win next move.
     */
    public boolean canWinNext() {
        return (winningPosition() & possible()) != 0;
    }

    /**
     * @return number of moves played from the beginning of the game.
     */
    public int nbMoves() {
        return moves;
    }

    /**
     * @return a compact representation of a position on WIDTH * (HEIGHT + 1) bits.
     */
    public long key() {
        return currentPosition + mask;
    }

    /*
     * Return a bitmap of all the possible next moves that do not lose in one turn.
     * A losing move is a move leaving the possibility for the opponent to win directly.
     *
     * Warning: this function is intended to test position where you cannot win in one turn.
     * If you have a winning move, this function can miss it and prefer to prevent the opponent
     * from making an alignment.
     */
    public long possibleNonLosingMoves() {
        if (!canWinNext()) {
            long possibleMask = possible();
            long opponentWin = opponentWinningPosition();
            long forcedMoves = possibleMask & opponentWin;
            if (forcedMoves != 0) {
                if ((forcedMoves & (forcedMoves - 1)) != 0) {
                    return 0; // the opponent has two winning moves and you cannot stop him
                } else {
                    return forcedMoves; // enforce playing the single forced move
                }
            }
            return possibleMask & ~(opponentWin >> 1);  // avoid playing below an opponent winning spot
        }
        return 0;
    }

    /**
     * Score a possible move.
     *
     * @param move, a possible move given in a bitmap format.
     *
     * The score we are using is the number of winning spots
     * the current player has after playing the move.
     */
    public int moveScore(long move) {
        return popcount(computeWinningPosition(currentPosition | move, mask));
    }

    /**
     * Default constructor, build an empty position.
     */
    public PositionVF() {
        this.currentPosition = 0;
        this.mask = 0;
        this.moves = 0;
    }

    /**
     * Indicates whether a column is playable.
     *
     * @param col: 0-based index of the column to play.
     * @return true if the column is playable, false if the column is already full.
     */
    private boolean canPlay(int col) {
        return (mask & topMaskCol(col)) == 0;
    }

    private void playCol(int col) {
        play((mask + bottomMaskCol(col)) & columnMask(col));
    }

    private boolean isWinningMove(int col) {
        return (winningPosition() & possible() & columnMask(col)) != 0;
    }

    /*
     * Return a bitmask of the possible winning positions for the current player.
     */
    private long winningPosition() {
        return computeWinningPosition(currentPosition, mask);
    }

    /*
     * Return a bitmask of the possible winning positions for the opponent.
     */
    private long opponentWinningPosition() {
        return computeWinningPosition(currentPosition ^ mask, mask);
    }

    /*
     * Bitmap of the next possible valid moves for the current player.
     * Including losing moves.
     */
    private long possible() {
        return (mask + bottomMask) & boardMask;
    }

    /*
     * Counts the number of bits set to one in a 64-bit integer.
     */
    private static int popcount(long m) {
        int c = 0;
        while (m != 0) {
            m &= m - 1;
            c++;
        }
        return c;
    }

    /*
     * @parmam position, a bitmap of the player to evaluate the winning positions.
     * @param mask, a mask of the already played spots.
     *
     * @return a bitmap of all the winning free spots making an alignment.
     */
    private static long computeWinningPosition(long position, long mask) {
        // vertical;
        long r = (position << 1) & (position << 2) & (position << 3);

        // horizontal
        long p = (position << (HEIGHT + 1)) & (position << 2 * (HEIGHT + 1));
        r |= p & (position << 3 * (HEIGHT + 1));
        r |= p & (position >> (HEIGHT + 1));
        p = (position >> (HEIGHT + 1)) & (position >> 2 * (HEIGHT + 1));
        r |= p & (position << (HEIGHT + 1));
        r |= p & (position >> 3 * (HEIGHT + 1));

        // diagonal 1
        p = (position << HEIGHT) & (position << 2 * HEIGHT);
        r |= p & (position << 3 * HEIGHT);
        r |= p & (position >> HEIGHT);
        p = (position >> HEIGHT) & (position >> 2 * HEIGHT);
        r |= p & (position << HEIGHT);
        r |= p & (position >> 3 * HEIGHT);

        // diagonal 2
        p = (position << (HEIGHT + 2)) & (position << 2 * (HEIGHT + 2));
        r |= p & (position << 3 * (HEIGHT + 2));
        r |= p & (position >> (HEIGHT + 2));
        p = (position >> (HEIGHT + 2)) & (position >> 2 * (HEIGHT + 2));
        r |= p & (position << (HEIGHT + 2));
        r |= p & (position >> 3 * (HEIGHT + 2));

        return r & (boardMask ^ mask);
    }

    // Static bitmaps

    private static final long bottomMask = bottom(WIDTH, HEIGHT);
    private static final long boardMask = bottomMask * ((1L << HEIGHT) - 1);

    // return a bitmask containing a single 1 corresponding to the top cell of a given column
    private static long topMaskCol(int col) {
        return 1L << ((HEIGHT - 1) + col * (HEIGHT + 1));
    }

    // return a bitmask containing a single 1 corresponding to the bottom cell of a given column
    private static long bottomMaskCol(int col) {
        return 1L << col * (HEIGHT + 1);
    }

    // return a bitmask 1 on all the cells of a given column
    static long columnMask(int col) {
        return ((1L << HEIGHT) - 1) << col * (HEIGHT + 1);
    }

    /*
     * Generate a bitmask containing one for the bottom slot of each column.
     * must be defined outside of the class definition to be available at compile time for bottomMask
     */
    private static long bottom(int width, int height) {
        return (width == 0) ? 0 : bottom(width - 1, height) | (1L << (width - 1) * (height + 1));
    }
}

