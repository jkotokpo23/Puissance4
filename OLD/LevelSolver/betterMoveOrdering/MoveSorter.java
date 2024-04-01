package levelSolver.betterMoveOrdering;


public class MoveSorter {
    private static final int WIDTH = 7; // Assume Position has a WIDTH field

    private class Entry {
        long move;
        int score;
    }

    private int size;
    private Entry[] entries;

    /**
     * Add a move in the container with its score.
     * You cannot add more than Position.WIDTH moves
     */
    public void add(long move, int score) {
        int pos = size++;
        for (; pos > 0 && entries[pos - 1].score > score; --pos) {
            entries[pos] = entries[pos - 1];
        }
        entries[pos] = new Entry();
        entries[pos].move = move;
        entries[pos].score = score;
    }

    /**
     * Get next move 
     * @return next remaining move with max score and remove it from the container.
     * If no more move is available return 0
     */
    public long getNext() {
        if (size > 0) {
            return entries[--size].move;
        } else {
            return 0;
        }
    }

    /**
     * Reset (empty) the container
     */
    public void reset() {
        size = 0;
    }

    /**
     * Build an empty container
     */
    public MoveSorter() {
        size = 0;
        entries = new Entry[WIDTH];
        for (int i = 0; i < WIDTH; i++) {
            entries[i] = new Entry();
        }
    }
}
