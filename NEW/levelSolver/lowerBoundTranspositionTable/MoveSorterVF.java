package levelSolver.lowerBoundTranspositionTable;

public class MoveSorterVF {

    private static final int WIDTH = 7; 

    private int size;
    private Entry[] entries;

    public MoveSorterVF() {
        size = 0;
        entries = new Entry[WIDTH];
        for (int i = 0; i < WIDTH; i++) {
            entries[i] = new Entry();
        }
    }

    /**
     * Add a move in the container with its score.
     * You cannot add more than WIDTH moves.
     */
    public void add(long move, int score) {
        int pos = size++;
        while (pos != 0 && entries[pos - 1].score > score) {
            entries[pos] = entries[pos - 1];
            pos--;
        }
        entries[pos].move = move;
        entries[pos].score = score;
    }


    public long getNext() {
        if (size != 0) {
            size--;
            return entries[size].move;
        } else {
            return 0;
        }
    }

    /**
     * Reset (empty) the container.
     */
    public void reset() {
        size = 0;
    }

    private static class Entry {
        long move;
        int score;
    }
}

