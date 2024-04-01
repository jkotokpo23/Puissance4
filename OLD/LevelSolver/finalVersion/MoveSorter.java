package levelSolver.finalVersion;

public class MoveSorter {
    private static final int WIDTH = 7; // Modify as needed

    private int size;
    public MoveEntry[] entries;

    public MoveSorter() {
        this.size = 0;
        this.entries = new MoveEntry[WIDTH];
    }

    public void add(long move, int score) {
        int pos = size++;
        while (pos > 0 && entries[pos - 1].score > score) {
            entries[pos] = entries[pos - 1];
            pos--;
        }
        entries[pos] = new MoveEntry(move, score);
    }
    

    public long getNext() {
        if (size > 0) {
            return entries[--size].move;
        } else {
            return 0;
        }
    }

    public void reset() {
        size = 0;
    }

    private static class MoveEntry {
        private final long move;
        private final int score;

        public MoveEntry(long move, int score) {
            this.move = move;
            this.score = score;
        }
    }
}
