package levelSolver.optimizedTranspositionTable;

public class PositionOPT {

    public static final int WIDTH = 7;
    public static final int HEIGHT = 6;
    public static final int MIN_SCORE = -(WIDTH * HEIGHT) / 2 + 3;
    public static final int MAX_SCORE = (WIDTH * HEIGHT + 1) / 2 - 3;

    static final int bottom(int width, int height) {
        return width == 0 ? 0 : bottom(width - 1, height) | 1 << (width - 1) * (height + 1);
    }

    private long current_position;
    private long mask;
    private int moves;

    public void play(long move) {
        current_position ^= mask;
        mask |= move;
        moves++;
    }

    public static PositionOPT copy(PositionOPT p) {
        PositionOPT pos = new PositionOPT();
        pos.current_position = p.current_position;
        pos.mask = p.mask;
        pos.moves = p.moves;
        return pos;
    }

    public int play(String seq) {
        for (int i = 0; i < seq.length(); i++) {
            int col = seq.charAt(i) - '1';
            if (col < 0 || col >= PositionOPT.WIDTH || !canPlay(col) || isWinningMove(col))
                return i;
            playCol(col);
        }
        return seq.length();
    }

    public boolean canWinNext() {
        return (winningPosition() & possible()) != 0;
    }

    public int nbMoves() {
        return moves;
    }

    public long key() {
        return current_position + mask;
    }

    public long possibleNonLosingMoves() {
        assert !canWinNext();
        long possibleMask = possible();
        long opponentWin = opponentWinningPosition();
        long forcedMoves = possibleMask & opponentWin;
        if (forcedMoves != 0) {
            if ((forcedMoves & (forcedMoves - 1)) != 0) {
                return 0;
            } else {
                possibleMask = forcedMoves;
            }
        }
        return possibleMask & ~(opponentWin >> 1);
    }

    public int moveScore(long move) {
        return popcount(computeWinningPosition(current_position | move, mask));
    }

    public PositionOPT() {
        current_position = 0;
        mask = 0;
        moves = 0;
    }

    private boolean canPlay(int col) {
        return (mask & topMaskCol(col)) == 0;
    }

    private void playCol(int col) {
        play((mask + bottomMaskCol(col)) & columnMask(col));
    }

    private boolean isWinningMove(int col) {
        return (winningPosition() & possible() & columnMask(col)) != 0;
    }

    private long winningPosition() {
        return computeWinningPosition(current_position, mask);
    }

    private long opponentWinningPosition() {
        return computeWinningPosition(current_position ^ mask, mask);
    }

    private long possible() {
        return (mask + bottomMask) & boardMask;
    }

    private static int popcount(long m) {
        int c = 0;
        for (c = 0; m != 0; c++)
            m &= m - 1;
        return c;
    }

    private static long computeWinningPosition(long position, long mask) {
        long r = (position << 1) & (position << 2) & (position << 3);

        long p = (position << (HEIGHT + 1)) & (position << 2 * (HEIGHT + 1));
        r |= p & (position << 3 * (HEIGHT + 1));
        r |= p & (position >> (HEIGHT + 1));
        p = (position >> (HEIGHT + 1)) & (position >> 2 * (HEIGHT + 1));
        r |= p & (position << (HEIGHT + 1));
        r |= p & (position >> 3 * (HEIGHT + 1));

        p = (position << HEIGHT) & (position << 2 * HEIGHT);
        r |= p & (position << 3 * HEIGHT);
        r |= p & (position >> HEIGHT);
        p = (position >> HEIGHT) & (position >> 2 * HEIGHT);
        r |= p & (position << HEIGHT);
        r |= p & (position >> 3 * HEIGHT);

        p = (position << (HEIGHT + 2)) & (position << 2 * (HEIGHT + 2));
        r |= p & (position << 3 * (HEIGHT + 2));
        r |= p & (position >> (HEIGHT + 2));
        p = (position >> (HEIGHT + 2)) & (position >> 2 * (HEIGHT + 2));
        r |= p & (position << (HEIGHT + 2));
        r |= p & (position >> 3 * (HEIGHT + 2));

        return r & (boardMask ^ mask);
    }

    private static final long bottomMask = bottom(WIDTH, HEIGHT);
    private static final long boardMask = bottomMask * ((1L << HEIGHT) - 1);

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