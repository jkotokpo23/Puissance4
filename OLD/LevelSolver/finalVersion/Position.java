package levelSolver.finalVersion;


public class Position {
    public static final int WIDTH = 7;  // width of the board
    public static final int HEIGHT = 6; // height of the board
    public static final int MIN_SCORE = -(WIDTH * HEIGHT) / 2 + 3;
    public static final int MAX_SCORE = (WIDTH * (HEIGHT + 1)) / 2 - 3;

    public long current_position; // bitmap of the current_player stones
    public long mask;             // bitmap of all the already played spots
    public int moves;             // number of moves played since the beginning of the game.

    /**
     * Plays a possible move given by its bitmap representation.
     *
     * @param move a possible move given by its bitmap representation
     *             only one bit of the bitmap should be set to 1
     *             the move should be a valid possible move for the current player
     */
    public void play(long move) {
        current_position ^= mask;
        mask |= move;
        moves++;
    }

     public static Position copy(Position p) {
        Position pos = new Position();
        pos.current_position = p.current_position;
        pos.mask = p.mask;
        pos.moves = p.moves;
        return pos;
    }

    /**
     * Plays a sequence of successive played columns, mainly used to initialize a board.
     *
     * @param seq a sequence of digits corresponding to the 1-based index of the column played.
     * @return number of played moves. Processing will stop at first invalid move that can be:
     * - invalid character (non-digit, or digit >= WIDTH)
     * - playing a column that is already full
     * - playing a column that makes an alignment (we only solve non).
     * Caller can check if the move sequence was valid by comparing the number of
     * processed moves to the length of the sequence.
     */
    public int play(String seq) {
        for (int i = 0; i < seq.length(); i++) {
            int col = Character.getNumericValue(seq.charAt(i)) - 1;
            if(col < 0 || col >= Position.WIDTH || !canPlay(col) || isWinningMove(col)) return i; // invalid move
            playCol(col);
        }
        return seq.length();
    }

    /**
     * Returns true if the current player can win next move.
     *
     * @return true if the current player can win next move
     */
    public boolean canWinNext() {
        return (winningPosition() & possible()) != 0;
    }

    /**
     * Returns the number of moves played from the beginning of the game.
     *
     * @return number of moves played from the beginning of the game.
     */
    public int nbMoves() {
        return moves;
    }

    /**
     * Returns a compact representation of a position on WIDTH * (HEIGHT + 1) bits.
     *
     * @return a compact representation of a position on WIDTH * (HEIGHT + 1) bits.
     */
    public long key() {
        return current_position + mask;
    }

    /**
     * Returns a bitmask of all the possible next moves that do not lose in one turn.
     * A losing move is a move leaving the possibility for the opponent to win directly.
     *
     * Warning: This function is intended to test positions where you cannot win in one turn.
     * If you have a winning move, this function can miss it and prefer to prevent the opponent
     * to make an alignment.
     *
     * @return a bitmask of all the possible next moves that do not lose in one turn.
     */
    public long possibleNonLosingMoves() {
        assert !canWinNext();
        long possibleMask = possible();
        long opponentWin = opponentWinningPosition();
        long forcedMoves = possibleMask & opponentWin;
        if (forcedMoves != 0) {
            if ((forcedMoves & (forcedMoves - 1)) != 0) // check if there is more than one forced move
                return 0;                                 // the opponent has two winning moves and you cannot stop him
            else possibleMask = forcedMoves;             // enforce playing the single forced move
        }
        return possibleMask & ~(opponentWin >> 1);  // avoid playing below an opponent winning spot
    }

    /**
     * Scores a possible move.
     *
     * @param move a possible move given in a bitmap format.
     * @return the score, which is the number of winning spots the current player has after playing the move.
     */
    public int moveScore(long move) {
        return popcount(computeWinningPosition(current_position | move, mask));
    }

    /**
     * Default constructor, builds an empty position.
     */
    public Position() {
        this.current_position = 0;
        this.mask = 0;
        this.moves = 0;
    }

    /**
     * Indicates whether a column is playable.
     *
     * @param col 0-based index of the column to play
     * @return true if the column is playable, false if the column is already full.
     */
    public boolean canPlay(int col) {
        return (mask & topMaskCol(col)) == 0;
    }

    public void playCol(int col) {
        play((mask + bottomMaskCol(col)) & columnMask(col));
    }

    public boolean isWinningMove(int col) {
        return (winningPosition() & possible() & columnMask(col)) != 0;
    }

    public long winningPosition() {
        return computeWinningPosition(current_position, mask);
    }

    public long opponentWinningPosition() {
        return computeWinningPosition(current_position ^ mask, mask);
    }

    public long possible() {
        return (mask + bottomMask) & boardMask;
    }

    public static int popcount(long m) {
        int c = 0;
        while (m != 0) {
            m &= m - 1;
            c++;
        }
        return c;
    }

    public static long computeWinningPosition(long position, long mask) {
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

    public static final long bottomMask = bottom(WIDTH, HEIGHT);
    public static final long boardMask = bottomMask * ((1L << HEIGHT) - 1);

    public static final long bottom(int width, int height) {
        return (width == 0) ? 0 : bottom(width - 1, height) | 1L << (width - 1) * (height + 1);
    }

    public static final long topMaskCol(int col) {
        return 1L << ((HEIGHT - 1) + col * (HEIGHT + 1));
    }

    public static final long bottomMaskCol(int col) {
        return 1L << col * (HEIGHT + 1);
    }

    public static final long columnMask(int col) {
        return ((1L << HEIGHT) - 1) << col * (HEIGHT + 1);
    }
}
