package levelSolver.lowerBoundTranspositionTable;

public class PositionVF {
    public static int WIDTH = 7;
    public static int HEIGHT = 6;
    public static int MIN_SCORE = -(WIDTH * HEIGHT) / 2 + 3;
    public static int MAX_SCORE = (WIDTH * HEIGHT + 1) / 2 - 3;

    private long currentPosition;
    private long mask;
    private int moves;

    public PositionVF() {
        this.currentPosition = 0;
        this.mask = 0;
        this.moves = 0;
    }

    public static PositionVF copy(PositionVF p) {
        PositionVF pos = new PositionVF();
        pos.currentPosition = p.currentPosition;
        pos.mask = p.mask;
        pos.moves = p.moves;
        return pos;
    }

    public void play(long move) {
        this.currentPosition ^= this.mask;
        this.mask |= move;
        this.moves++;
    }

    public int play(String seq) {
        for (int i = 0; i < seq.length(); i++) {
            int col = Character.getNumericValue(seq.charAt(i)) - 1;
            if (col < 0 || col >= WIDTH || !canPlay(col) || isWinningMove(col)) return i;
            playCol(col);
        }
        return seq.length();
    }

    public boolean canWinNext() {
        return (winningPosition() & possible()) != 0;
    }

    public int nbMoves() {
        return this.moves;
    }

    public long key() {
        return this.currentPosition + this.mask;
    }

    public long possibleNonLosingMoves() {
        assert !canWinNext();
        long possibleMask = possible();
        long opponentWin = opponentWinningPosition();
        long forcedMoves = possibleMask & opponentWin;
        if (forcedMoves != 0) {
            if ((forcedMoves & (forcedMoves - 1)) != 0) return 0;
            else possibleMask = forcedMoves;
        }
        return possibleMask & ~(opponentWin >> 1);
    }

    public int moveScore(long move) {
        return popcount(computeWinningPosition(this.currentPosition | move, this.mask));
    }

    private boolean canPlay(int col) {
        return (this.mask & topMaskCol(col)) == 0;
    }

    private void playCol(int col) {
        play((this.mask + bottomMaskCol(col)) & columnMask(col));
    }

    private boolean isWinningMove(int col) {
        return (winningPosition() & possible() & columnMask(col)) != 0;
    }

    private long winningPosition() {
        return computeWinningPosition(this.currentPosition, this.mask);
    }

    private long opponentWinningPosition() {
        return computeWinningPosition(this.currentPosition ^ this.mask, this.mask);
    }

    private long possible() {
        return (this.mask + bottomMask) & boardMask;
    }

    private int popcount(long m) {
        int c = 0; 
        for (c = 0; m != 0; c++) m &= m - 1;
        return c;
    }

    private static long computeWinningPosition(long position, long mask) {
        long r = (position << 1) & (position << 2) & (position << 3);
        long p = (position << (HEIGHT + 1)) & (position << (2 * (HEIGHT + 1)));
        r |= p & (position << (3 * (HEIGHT + 1)));
        r |= p & (position >> (HEIGHT + 1));
        p = (position >> (HEIGHT + 1)) & (position >> (2 * (HEIGHT + 1)));
        r |= p & (position << (HEIGHT + 1));
        r |= p & (position >> (3 * (HEIGHT + 1)));
        p = (position << HEIGHT) & (position << (2 * HEIGHT));
        r |= p & (position << (3 * HEIGHT));
        r |= p & (position >> HEIGHT);
        p = (position >> HEIGHT) & (position >> (2 * HEIGHT));
        r |= p & (position << HEIGHT);
        r |= p & (position >> (3 * HEIGHT));
        p = (position << (HEIGHT + 2)) & (position << (2 * (HEIGHT + 2)));
        r |= p & (position << (3 * (HEIGHT + 2)));
        r |= p & (position >> (HEIGHT + 2));
        p = (position >> (HEIGHT + 2)) & (position >> (2 * (HEIGHT + 2)));
        r |= p & (position << (HEIGHT + 2));
        r |= p & (position >> (3 * (HEIGHT + 2)));
        return r & (boardMask ^ mask);
    }

    private static final long bottomMask = bottom(WIDTH, HEIGHT);
    private static final long boardMask = bottomMask * ((1L << HEIGHT) - 1);

    private static long bottom(int width, int height) {
        return width == 0 ? 0 : bottom(width - 1, height) | 1L << (width - 1) * (height + 1);
    }

    private static long topMaskCol(int col) {
        return 1L << ((HEIGHT - 1) + col * (HEIGHT + 1));
    }

    private static long bottomMaskCol(int col) {
        return 1L << col * (HEIGHT + 1);
    }

    static long columnMask(int col) {
        return ((1L << HEIGHT) - 1) << col * (HEIGHT + 1);
    }
}
