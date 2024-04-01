package levelSolver.betterMoveOrdering;

import java.util.Arrays;

import levelSolver.iterativeDepending.PositionID;

@SuppressWarnings("unused")
public class PositionBMO {
    public static final int WIDTH = 7; // width of the board
    public static final int HEIGHT = 6; // height of the board
    public static final int MIN_SCORE = -(WIDTH * HEIGHT) / 2 + 3;
    public static final int MAX_SCORE = (WIDTH * HEIGHT + 1) / 2 - 3;

    static {
        assert WIDTH < 10 : "Board's width must be less than 10";
        assert WIDTH * (HEIGHT + 1) <= 64 : "Board does not fit in 64 bits bitboard";
    }

    private long current_position; // bitmap of the current_player stones
    private long mask; // bitmap of all the already played spots
    private int moves; // number of moves played since the beginning of the game.

    /**
     * Plays a possible move given by its bitmap representation
     *
     * @param move: a possible move given by its bitmap representation only one bit of the bitmap should be set to 1
     *              the move should be a valid possible move for the current player
     */
    public void play(long move) {
        current_position ^= mask;
        mask |= move;
        moves++;
    }

    /**
     * Plays a sequence of successive played columns, mainly used to initialize a board.
     *
     * @param seq: a sequence of digits corresponding to the 1-based index of the column played.
     * @return number of played moves. Processing will stop at the first invalid move that can be:
     * - invalid character (non-digit, or digit >= WIDTH)
     * - playing a column that is already full
     * - playing a column that makes an alignment (we only solve non).
     * Caller can check if the move sequence was valid by comparing the number of processed moves to the length of the sequence.
     */
    public int play(String seq) {
        for (int i = 0; i < seq.length(); i++) {
            int col = seq.charAt(i) - '1';
            if (col < 0 || col >= WIDTH || !canPlay(col) || isWinningMove(col)) return i; // invalid move
            playCol(col);
        }
        return seq.length();
    }

    public static PositionBMO copy(PositionBMO p) {
        PositionBMO pos = new PositionBMO();
        pos.current_position = p.current_position;
        pos.mask = p.mask;
        pos.moves = p.moves;
        return pos;
    }

    /**
     * @return true if current player can win next move
     */
    public boolean canWinNext() {
        return (winningPosition() & possible()) != 0;
    }

   public long possibleNonLosingMoves() {
        assert !canWinNext();
        long possibleMask = possible();
        long opponentWin = opponentWinningPosition();
        long forcedMoves = possibleMask & opponentWin;
        if (forcedMoves != 0) {
            if ((forcedMoves & (forcedMoves - 1)) != 0) // check if there is more than one forced move
                return 0;                           // the opponent has two winning moves and you cannot stop him
            else possibleMask = forcedMoves;    // enforce playing the single forced move
        }
        return possibleMask & ~(opponentWin >> 1);  // avoid playing below an opponent winning spot
    }
    
    public int moveScore(long move) {
        return popcount(computeWinningPosition(current_position | move, mask));
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
     * Return true if current player can play a column.
     *
     * @param col: 0-based index of the column to play.
     * @return true if the column is playable, false if the column is already full.
     */
    public boolean canPlay(int col) {
        return (mask & topMaskCol(col)) == 0;
    }

    /**
     * Plays a playable column.
     * This function should not be called on a non-playable column or a column making an alignment.
     *
     * @param col: 0-based index of a playable column.
     */
    public void playCol(int col) {
        play((mask + bottomMaskCol(col)) & columnMask(col));
    }

    /**
     * Indicates whether the current player wins by playing a given column.
     * This function should never be called on a non-playable column.
     *
     * @param col: 0-based index of a playable column.
     * @return true if current player makes an alignment by playing the corresponding column col.
     */
    public boolean isWinningMove(int col) {
        return (winningPosition() & possible() & columnMask(col)) != 0;
    }

    /**
     * Default constructor, build an empty position.
     */
    public PositionBMO() {
        current_position = 0;
        mask = 0;
        moves = 0;
    }

    /*
     * Return a bitmask of the possible winning positions for the current player
     */
    public long winningPosition() {
        return computeWinningPosition(current_position, mask);
    }

    /*
     * Return a bitmask of the possible winning positions for the opponent
     */
    public long opponentWinningPosition() {
        return computeWinningPosition(current_position ^ mask, mask);
    }

    /*
     * Bitmap of the next possible valid moves for the current player
     * Including losing moves.
     */
    public long possible() {
        return (mask + bottomMask) & boardMask;
    }

    /*
     * counts the number of bits set to one in a 64-bits integer
     */
    public static int popcount(long m) {
        int c = 0;
        for (c = 0; m != 0; c++) m &= m - 1;
        return c;
    }

    /*
     * @param position, a bitmap of the player to evaluate the winning pos
     * @param mask, a mask of the already played spots
     *
     * @return a bitmap of all the winning free spots making an alignment
     */
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

    // Static bitmaps
    private static final long bottomMask = bottom(WIDTH, HEIGHT);
    private static final long boardMask = bottomMask * ((1L << HEIGHT) - 1);

    // return a bitmask containing a single 1 corresponding to the top cell of a given column
    public static long topMaskCol(int col) {
        return 1L << ((HEIGHT - 1) + col * (HEIGHT + 1));
    }

    // return a bitmask containing a single 1 corresponding to the bottom cell of a given column
    public static long bottomMaskCol(int col) {
        return 1L << col * (HEIGHT + 1);
    }

    // return a bitmask with 1 on all the cells of a given column
    public static long columnMask(int col) {
        return ((1L << HEIGHT) - 1) << col * (HEIGHT + 1);
    }

    // Generate a bitmask containing one for the bottom slot of each column
    public static long bottom(int width, int height) {
        return width == 0 ? 0 : bottom(width - 1, height) | 1L << (width - 1) * (height + 1);
    }
}
