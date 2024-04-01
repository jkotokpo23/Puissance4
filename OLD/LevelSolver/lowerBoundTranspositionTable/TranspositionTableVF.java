package levelSolver.lowerBoundTranspositionTable;

public class TranspositionTableVF {
    private int keySize = 64;
    private int valueSize = 64;
    private int logSize = 64;

    private int size = nextPrime(1 << logSize);
    //private static final int size = 8306069;

    private long[] K;
    private int[] V;

    public TranspositionTableVF(int key_size, int value_size, int log_size) {
        this.keySize = key_size;
        this.valueSize = value_size;
        this.logSize = log_size;

        K = new long[size];
        V = new int[size];
        reset();
    }

    public void reset() {
        for (int i = 0; i < size; i++) {
            K[i] = 0;
            V[i] = 0;
        }
    }

    public void put(long key, int value) {
        assert (key >> keySize == 0);
        assert (value >> valueSize == 0);
        int pos = index(key);
        K[pos] = key;
        V[pos] = value;
    }

    public int get(long key) {
        assert (key >> keySize == 0);
        int pos = index(key);
        if (K[pos] == key) return V[pos];
        else return 0;
    }

    private int index(long key) {
        return (int) (key % size);
    }

    private static int nextPrime(int n) {
        while (!isPrime(n)) n++;
        return n;
    }

    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (int i = 5; i * i <= n; i = i + 6)
            if (n % i == 0 || n % (i + 2) == 0) return false;
        return true;
    }
}
