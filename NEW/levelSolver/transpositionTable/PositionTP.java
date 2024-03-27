package levelSolver.transpositionTable;

public class PositionTP {

    public static final int WIDTH = 7;
    public static final int HEIGHT = 6;
    public static final int MIN_SCORE = -(WIDTH * HEIGHT) / 2 + 3;
    public static final int MAX_SCORE = (WIDTH * HEIGHT + 1) / 2 - 3;

    private long currentPosition;
    private long mask;
    private int moves;

    public static final int getWidth() {
        return WIDTH;
    }

    public static final int getHeight() {
        return HEIGHT;
    }

    public static final int getMinScore() {
        return MIN_SCORE;
    }

    public static final int getMaxScore() {
        return MAX_SCORE;
    }

    public boolean canPlay(int col) {
        return (mask & topMask(col)) == 0;
    }

    public void play(int col) {
        currentPosition ^= mask;
        mask |= mask + bottomMask(col);
        moves++;
    }

    public static PositionTP copy(PositionTP p){
        PositionTP pos = new PositionTP();
        pos.currentPosition = p.currentPosition;
        pos.mask = p.mask;
        pos.moves = p.moves;
        return pos;
    }

    public int play(String seq) {
        for (int i = 0; i < seq.length(); i++) {
            int col = seq.charAt(i) - '1';
            if (col < 0 || col >= PositionTP.WIDTH || !canPlay(col) || isWinningMove(col)) {
                return i; // invalid move
            }
            play(col);
        }
        return seq.length();
    }

    public boolean isWinningMove(int col) {
        long pos = currentPosition;
        pos |= (mask + bottomMask(col)) & columnMask(col);
        return alignment(pos);
    }

    public int nbMoves() {
        return moves;
    }

    public long key() {
        return currentPosition + mask;
    }

    public PositionTP() {
        this.currentPosition = 0;
        this.mask = 0;
        this.moves = 0;
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
