package levelSolver.iterativeDepending;

public class PositionID {

    public static final int WIDTH = 7;
    public static final int HEIGHT = 6;
    public static final int MIN_SCORE = -(WIDTH * HEIGHT) / 2 + 3;
    public static final int MAX_SCORE = (WIDTH * HEIGHT + 1) / 2 - 3;

    private long current_position;
    private long mask;
    private int moves;

    public PositionID() {
        this.current_position = 0;
        this.mask = 0;
        this.moves = 0;
    }

     public static PositionID copy(PositionID p) {
        PositionID pos = new PositionID();
        pos.current_position = p.current_position;
        pos.mask = p.mask;
        pos.moves = p.moves;
        return pos;
    }

    public boolean canPlay(int col) {
        return (mask & topMask(col)) == 0;
    }

    public void play(int col) {
        current_position ^= mask;
        mask |= mask + bottomMask(col);
        moves++;
    }

    public int play(String seq) {
        for (int i = 0; i < seq.length(); i++) {
            int col = seq.charAt(i) - '1';
            if (col < 0 || col >= WIDTH || !canPlay(col) || isWinningMove(col))
                return i;
            play(col);
        }
        return seq.length();
    }

    public boolean isWinningMove(int col) {
        long pos = current_position;
        pos |= (mask + bottomMask(col)) & columnMask(col);
        return alignment(pos);
    }

    public int nbMoves() {
        return moves;
    }

    public long key() {
        return current_position + mask;
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

    private static long topMask(int col) {
        return (1L << (HEIGHT - 1)) << col * (HEIGHT + 1);
    }

    private static long bottomMask(int col) {
        return 1L << col * (HEIGHT + 1);
    }

    private static long columnMask(int col) {
        return ((1L << HEIGHT) - 1) << col * (HEIGHT + 1);
    }
}
