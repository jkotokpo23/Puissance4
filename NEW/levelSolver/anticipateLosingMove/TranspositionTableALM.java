package levelSolver.anticipateLosingMove;

import java.util.Arrays;

public class TranspositionTableALM {

    private static class Entry {
        long key;    // Using 56-bit keys
        byte val;    // Using 8-bit values

        Entry(long key, byte val) {
            this.key = key;
            this.val = val;
        }
    }

    private Entry[] T;

    public TranspositionTableALM(int size) {
        assert (size > 0);
        T = new Entry[size];
        reset();
    }

    /**
     * Clears the Transposition Table.
     */
    public void reset() {
        Arrays.fill(T, new Entry(0, (byte) 0)); // Fill everything with 0, because 0 value means missing data
    }

    /**
     * Stores a value for a given key.
     *
     * @param key   56-bit key.
     * @param value Non-null 8-bit value. Null (0) values are used to encode missing data.
     */
    public void put(long key, byte val) {
        assert key < (1L << 56);
        int i = index(key); // Compute the index position
        T[i].key = key;  // and overwrite any existing value.
        T[i].val = val;
    }

    /**
     * Gets the value of a key.
     *
     * @param key Key.
     * @return 8-bit value associated with the key if present, 0 otherwise.
     */
    public byte get(long key) {
        assert key < (1L << 56);
        int i = index(key);  // Compute the index position
        if (T[i].key == key)
            return T[i].val; // and return value if key matches
        else
            return 0;            // or 0 if missing entry
    }

    private int index(long key) {
        int taille = T.length;

        // Check that the size of the table is greater than zero
        if (taille <= 0) {
            // Handle the error or throw an appropriate exception
            throw new IllegalStateException("La taille de la table de transposition ne peut pas être zéro.");
        }

        // Calculate the index using the modulo
        return (int) (key % taille);
    }
}
