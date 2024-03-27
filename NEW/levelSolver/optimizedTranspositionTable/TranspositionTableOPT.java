package levelSolver.optimizedTranspositionTable;

public class TranspositionTableOPT {


    private static final int key_size = 64;  // Set the desired key size
    private static final int value_size = 64; // Set the desired value size
    private static final int log_size = 64;    // Set the desired log size

    private static final int size = (int) nextPrime(1 << log_size); // size of the transition table

    private long[] K; // Array to store keys
    private int[] V; // Array to store values

    TranspositionTableOPT() {
        K = new long[size];
        V = new int[size];
        reset();
    }

    /*
     * util functions to compute next prime at compile time
     */
    private static long med(long min, long max) {
        return (min + max) / 2;
    }

    private static boolean hasFactor(long n, long min, long max) {
        return min * min > n ? false :
                min + 1 >= max ? n % min == 0 :
                        hasFactor(n, min, med(min, max)) || hasFactor(n, med(min, max), max);
    }

    // return next prime number greater or equal to n.
    // n must be >= 2
    private static long nextPrime(long n) {
        return hasFactor(n, 2, n) ? nextPrime(n + 1) : n;
    }

    /*
     * Empty the Transition Table.
     */
    void reset() {
        // fill everything with 0, because 0 value means missing data
        for (int i = 0; i < size; i++) {
            K[i] = 0;
            V[i] = 0;
        }
    }

    /**
     * Store a value for a given key
     *
     * @param key:   must be less than key_size bits.
     * @param value: must be less than value_size bits. null (0) value is used to encode missing data
     */
    void put(long key, int value) {
        assert (key >> key_size == 0);
        assert (value >> value_size == 0);
        int pos = index(key);
        K[pos] = key;
        V[pos] = value;
    }

    /**
     * Get the value of a key
     *
     * @param key: must be less than key_size bits.
     * @return value_size bits value associated with the key if present, 0 otherwise.
     */
    long get(long key) {
        assert (key >> key_size == 0);
        int pos = index(key);
        if (K[pos] == key) return V[pos];
        else return 0;
    }

    /**
     * Compute the index for a given key
     *
     * @param key: must be less than key_size bits.
     * @return index for the key
     */
    private int index(long key) {
        return (int) (key % size); // size is a static int of the form 2^n+1, compiler is able to optimize a little modulus
    }
}
