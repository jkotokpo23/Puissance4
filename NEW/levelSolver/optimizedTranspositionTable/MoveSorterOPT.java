package levelSolver.optimizedTranspositionTable;

public class MoveSorterOPT {

    private static final int WIDTH = 7; // Remplacez par la largeur du tableau de votre classe Position
    private Entry[] entries;
    private int size;

    public MoveSorterOPT() {
        this.entries = new Entry[WIDTH];
        this.size = 0;
    }

    public void add(long move, int score) {
        int pos = size++;
        while (pos > 0 && entries[pos - 1].score > score) {
            entries[pos] = entries[pos - 1];
            pos--;
        }
        entries[pos] = new Entry(move, score);
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

    private static class Entry {
        long move;
        int score;

        Entry(long move, int score) {
            this.move = move;
            this.score = score;
        }
    }
}