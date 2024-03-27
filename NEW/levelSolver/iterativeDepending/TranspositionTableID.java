package levelSolver.iterativeDepending;

import java.util.ArrayList;

public class TranspositionTableID {
    private final ArrayList<Entry> T;

    private static class Entry {
        long key; // use 56-bit keys
        byte val; // use 8-bit values
    }

    private int index(long key) {
        return (int) (key % T.size());
    }

    public TranspositionTableID(int size) {
        assert size > 0;
        T = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            T.add(new Entry());
        }
    }

    /**
     * Empty the Transition Table.
     */
    public void reset() {
        for (Entry entry : T) {
            entry.key = 0;
            entry.val = 0;
        }
    }

    /**
     * Store a value for a given key.
     *
     * @param key:   56-bit key
     * @param value: non-null 8-bit value. null (0) values are used to encode missing data.
     */
    public void put(long key, byte val) {
        assert key < (1L << 56);
        int i = index(key); // compute the index position
        T.get(i).key = key; // and override any existing value.
        T.get(i).val = val;
    }

    /**
     * Get the value of a key.
     *
     * @param key
     * @return 8-bit value associated with the key if present, 0 otherwise.
     */
    public byte get(long key) {
        assert key < (1L << 56);
        int i = index(key); // compute the index position
        return (T.get(i).key == key) ? T.get(i).val : 0;
    }
}