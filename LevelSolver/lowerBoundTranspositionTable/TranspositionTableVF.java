package levelSolver.lowerBoundTranspositionTable;

import java.util.Arrays;

public class TranspositionTableVF {

    private static final int MAX_KEY_SIZE = 64;
    private static final int MAX_VALUE_SIZE = 64;

    private final int size;
    private final int[] K; // Array to store truncated version of keys
    private final long[] V; // Array to store values

    public TranspositionTableVF(int keySize, int valueSize, int logSize) {
        assert keySize <= MAX_KEY_SIZE : "keySize is too large";
        assert valueSize <= MAX_VALUE_SIZE : "valueSize is too large";
        assert logSize <= MAX_KEY_SIZE : "logSize is too large";

        // size of the transition table. Have to be odd to be prime with 2^sizeof(key_t)
        // using a prime number reduces collisions
        this.size = (int) nextPrime(1 << logSize);

        this.K = new int[size];
        this.V = new long[size];
        reset();
    }

    /*
     * Empty the Transition Table.
     */
    public void reset() {
        // fill everything with 0, because 0 value means missing data
        Arrays.fill(K, 0);
        Arrays.fill(V, 0);
    }

    /**
     * Store a value for a given key
     *
     * @param key:   must be less than keySize bits.
     * @param value: must be less than valueSize bits. null (0) value is used to encode missing data
     */
    public void put(long key, long value) {
        assert (key >> MAX_KEY_SIZE == 0);
        assert (value >> MAX_VALUE_SIZE == 0);
        int pos = index(key);
        K[pos] = (int) key; // key is possibly truncated as key_t is possibly less than keySize bits.
        V[pos] = value;
    }

    /**
     * Get the value of a key
     *
     * @param key: must be less than keySize bits.
     * @return valueSize bits value associated with the key if present, 0 otherwise.
     */
    public long get(long key) {
        assert (key >> MAX_KEY_SIZE == 0);
        int pos = index(key);
        if (K[pos] == (int) key) {
            return V[pos]; // need to cast to int because key may be truncated due to size of key_t
        } else {
            return 0;
        }
    }

    private int index(long key) {
        return (int) (key % size);
    }

    /**
     * Util functions to compute the next prime at compile time
     */
    private static long med(long min, long max) {
        return (min + max) / 2;
    }

    /**
     * Tells if an integer n has a divisor between min (inclusive) and max (exclusive)
     */
    private static boolean hasFactor(long n, long min, long max) {
        return min * min > n ? false : // do not search for factor above sqrt(n)
                min + 1 >= max ? n % min == 0 :
                        hasFactor(n, min, med(min, max)) || hasFactor(n, med(min, max), max);
    }

    /**
     * Return next prime number greater or equal to n.
     * n must be >= 2
     */
    private static long nextPrime(long n) {
        return hasFactor(n, 2, n) ? nextPrime(n + 1) : n;
    }

    /**
     * log2(1) = 0; log2(2) = 1; log2(3) = 1; log2(4) = 2; log2(8) = 3
     */
    public static int log2(int n) {
        return n <= 1 ? 0 : log2(n / 2) + 1;
    }
}

