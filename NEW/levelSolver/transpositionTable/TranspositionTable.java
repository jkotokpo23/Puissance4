package levelSolver.transpositionTable;

import java.util.ArrayList;

/**
 * The Transposition Table is a simple hash map with fixed storage size.
 * In case of collision, we keep the last entry and overwrite the previous one.
 *
 * We use 56-bit keys and 8-bit non-null values.
 */
public class TranspositionTable {

    private ArrayList<Entry> T;
    private int size;

    private static class Entry {
        long key;    // Using 56-bit keys
        byte val;    // Using 8-bit values

        Entry(long key, byte val) {
            this.key = key;
            this.val = val;
        }
    }

    public TranspositionTable(int size) {
        assert (size > 0);
        this.size = size;
        T = new ArrayList<>(size);
        reset();
    }

    /**
     * Clears the Transposition Table.
     */
    public void reset() {
    for (int i = 0; i < this.size; i++) {
            T.add(new Entry(0, (byte) 0)); // Fill everything with 0, because 0 value means missing data
        }
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
        T.get(i).key = key;  // and overwrite any existing value.
        T.get(i).val = val;
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
        if (T.get(i).key == key)
            return T.get(i).val; // and return value if key matches
        else
            return 0;            // or 0 if missing entry
    }

    private int index(long key) {
        int taille = T.size();
        
        // Vérifiez que la taille de la table est supérieure à zéro
        if (size <= 0) {
            // Gérer l'erreur ou lever une exception appropriée
            throw new IllegalStateException("La taille de la table de transposition ne peut pas être zéro.");
        }
        
        // Calculez l'indice en utilisant le modulo
        return (int) (key % taille);
    }
}
