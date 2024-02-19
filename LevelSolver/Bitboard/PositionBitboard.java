package levelSolver.bitboard;

 public class PositionBitboard {
    public static final int WIDTH = 7;  // width of the board
    public static final int HEIGHT = 6; // height of the board

    private long current_position;
    private long mask;
    private int moves; // number of moves played since the beginning of the game.

    /**
     * Indicates whether a column is playable.
     * @param col: 0-based index of column to play
     * @return true if the column is playable, false if the column is already full.
     */
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

    public static PositionBitboard copy(PositionBitboard p) {
        PositionBitboard pos = new PositionBitboard();
        pos.current_position = p.current_position;
        pos.mask = p.mask;
        pos.moves = p.moves;
        return pos;
    }


    public int play(String seq) {
        for (int i = 0; i < seq.length(); i++) {
            int col = seq.charAt(i) - '1';
            if (col < 0 || col >= WIDTH || !canPlay(col) || isWinningMove(col)) return i; // invalid move
            play(col);
        }
        return seq.length();
    }

    /**
     * Indicates whether the current player wins by playing a given column.
     * This function should never be called on a non-playable column.
     * @param col: 0-based index of a playable column.
     * @return true if the current player makes an alignment by playing the corresponding column col.
     */
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
    public PositionBitboard() {
        current_position = 0;
        mask = 0;
        moves = 0;
    }

    private boolean alignment(long pos) {
        // horizontal
        long m = pos & (pos >> (HEIGHT + 1));
        if ((m & (m >> (2 * (HEIGHT + 1)))) != 0) return true;

        // diagonal 1
        m = pos & (pos >> HEIGHT);
        if ((m & (m >> (2 * HEIGHT))) != 0) return true;

        // diagonal 2
        m = pos & (pos >> (HEIGHT + 2));
        if ((m & (m >> (2 * (HEIGHT + 2)))) != 0) return true;

        // vertical;
        m = pos & (pos >> 1);
        return (m & (m >> 2)) != 0;
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
