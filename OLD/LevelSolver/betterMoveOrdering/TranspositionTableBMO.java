package levelSolver.betterMoveOrdering;

import java.util.Arrays;

/**
 * Transposition Table is a simple hash map with fixed storage size.
 * In case of collision, we keep the last entry and override the previous one.
 *
 * We use 56-bit keys and 8-bit non-null values
 */
public class TranspositionTableBMO {
    private static class Entry {
        long key; // use 56-bit keys
        byte val; // use 8-bit values
    }

    private Entry[] T;

    public TranspositionTableBMO(int size) {
        assert size > 0;
        T = new Entry[size];
        for (int i = 0; i < size; i++) {
            T[i] = new Entry();
        }
    }

    /**
     * Empty the Transition Table.
     */
    public void reset() {
        Arrays.fill(T, new Entry());
    }

    /**
     * Store a value for a given key
     *
     * @param key   56-bit key
     * @param value non-null 8-bit value. null (0) value is used to encode missing data.
     */
    public void put(long key, byte val) {
        assert key < (1L << 56);
        int i = index(key); // compute the index position
        T[i].key = key;     // and override any existing value.
        T[i].val = val;
    }

    /**
     * Get the value of a key
     *
     * @param key
     * @return 8-bit value associated with the key if present, 0 otherwise.
     */
    public byte get(long key) {
        assert key < (1L << 56);
        int i = index(key); // compute the index position
        if (T[i].key == key)
            return T[i].val; // and return value if key matches
        else
            return 0; // or 0 if missing entry
    }

    private int index(long key) {
        return (int) (key % T.length);
    }
}
