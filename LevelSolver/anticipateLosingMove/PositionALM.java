package levelSolver.anticipateLosingMove;

import levelSolver.iterativeDepending.PositionID;

public class PositionALM {
    public static final int WIDTH = 7;  // width of the board
    public static final int HEIGHT = 6; // height of the board
    public static final int MIN_SCORE = -(WIDTH * HEIGHT) / 2 + 3;
    public static final int MAX_SCORE = (WIDTH * (HEIGHT + 1)) / 2 - 3;

    private static final int bottomMask = (int)bottom(WIDTH, HEIGHT);
    private static final int boardMask = (int)(bottomMask * ((1L << HEIGHT) - 1));

    private long currentPosition;
    private long mask;
    private int moves; 
  
    public void play(int col) {
        currentPosition ^= mask;
        mask |= mask + bottomMaskCol(col);
        moves++;
    }

    public static PositionALM copy(PositionALM p) {
        PositionALM pos = new PositionALM();
        pos.currentPosition = p.currentPosition;
        pos.mask = p.mask;
        pos.moves = p.moves;
        return pos;
    }
   
    public int play(String seq) {
        for (int i = 0; i < seq.length(); i++) {
            int col = seq.charAt(i) - '1';
            if (col < 0 || col >= PositionALM.WIDTH || !canPlay(col) || isWinningMove(col)) return i; // invalid move
            play(col);
        }
        return seq.length();
    }

    /*
     * return true if the current player can win next move
     */
    public boolean canWinNext() {
        return winningPosition() != 0 & possible() != 0;
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
     * Warning, this function is intended to test positions where you cannot win in one turn.
     * If you have a winning move, this function can miss it and prefer to prevent the opponent
     * from making an alignment.
     */
    public long possibleNonLosingMoves() {
        assert !canWinNext();
        long possibleMask = possible();
        long opponentWin = opponentWinningPosition();
        long forcedMoves = possibleMask & opponentWin;
        if (forcedMoves != 0) {
            if (forcedMoves != 0 & (forcedMoves - 1) != 0) // check if there is more than one forced move
                return 0;                           // the opponent has two winning moves and you cannot stop him
            else possibleMask = forcedMoves;    // enforce playing the single forced move
        }
        return possibleMask & ~(opponentWin >> 1);  // avoid playing below an opponent winning spot
    }

    /**
     * Default constructor, builds an empty position.
     */
    public PositionALM() {
        currentPosition = 0;
        mask = 0;
        moves = 0;
    }

    /**
     * Indicates whether a column is playable.
     *
     * @param col: 0-based index of the column to play
     * @return true if the column is playable, false if the column is already full.
     */
    private boolean canPlay(int col) {
        return (mask & topMaskCol(col)) == 0;
    }

    /**
     * Indicates whether the current player wins by playing a given column.
     * This function should never be called on a non-playable column.
     *
     * @param col: 0-based index of a playable column.
     * @return true if the current player makes an alignment by playing the corresponding column col.
     */
    private boolean isWinningMove(int col) {
        return winningPosition() != 0 & possible() != 0 & columnMask(col) != 0;
    }

    /*
     * Return a bitmask of the possible winning positions for the current player
     */
    private long winningPosition() {
        return computeWinningPosition(currentPosition, mask);
    }

    /*
     * Return a bitmask of the possible winning positions for the opponent
     */
    private long opponentWinningPosition() {
        return computeWinningPosition(currentPosition ^ mask, mask);
    }

    private long possible() {
        return (mask + bottomMask) & boardMask;
    }

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
    private static long bottom(int width, int height) {
        return (width == 0) ? 0 : bottom(width - 1, height) | (1L << ((width - 1) * (height + 1)));
    }

    @SuppressWarnings("unused")
    private static long bottomMask(int width, int height) {
        return width == 0 ? 0 : bottomMask(width - 1, height) | 1L << (width - 1) * (height + 1);
    }

    private static long bottomMaskCol(int col) {
        return 1L << col * (HEIGHT + 1);
    }

    static long columnMask(int col) {
        return ((1L << HEIGHT) - 1) << col * (HEIGHT + 1);
    }

    private static long topMaskCol(int col) {
        return 1L << ((HEIGHT - 1) + col * (HEIGHT + 1));
    }
}
