package levelSolver.finalVersion;

import java.util.Arrays;

public class TranspositionTable {
    public static int MAX_BITS = 64;

    public interface UIntType {
        int getSize();
    }

    public static class UInt8 implements UIntType {
        public int getSize() {
            return 8;
        }
    }

    public static class UInt16 implements UIntType {
        public int getSize() {
            return 16;
        }
    }

    public static class UInt32 implements UIntType {
        public int getSize() {
            return 32;
        }
    }

    public static class UInt64 implements UIntType {
        public int getSize() {
            return 64;
        }
    }

    public static UIntType[] UINT_TYPES = {new UInt8(), new UInt16(), new UInt32(), new UInt64()};

    public static long med(long min, long max) {
        return (min + max) / 2;
    }

    public static boolean hasFactor(long n, long min, long max) {
        return min * min > n ? false :
                min + 1 >= max ? n % min == 0 :
                        hasFactor(n, min, med(min, max)) || hasFactor(n, med(min, max), max);
    }

    public static long nextPrime(long n) {
        return hasFactor(n, 2, n) ? nextPrime(n + 1) : n;
    }

    public static int log2(int n) {
        return n <= 1 ? 0 : log2(n / 2) + 1;
    }

    public static int log2(long n) {
        return n <= 1 ? 0 : log2((int) (n / 2)) + 1;
    }

    public static <T extends UIntType> T findUIntType(int size) {
        for (UIntType type : UINT_TYPES) {
            if (type.getSize() >= size) {
                return (T) type;
            }
        }
        return (T) new UInt64(); // Default to UInt64 if size is larger than the maximum supported size
    }

    public static class TranspositionTableEntry {
        long key;
        int value;
    }

    public int keySize;
    public int valueSize;
    public int logSize;

    public UIntType keyUIntType;
    public UIntType valueUIntType;

    public long size;
    public TranspositionTableEntry[] entries;

    public TranspositionTable(int keySize, int valueSize, int logSize) {
        this.keySize = keySize;
        this.valueSize = valueSize;
        this.logSize = logSize;

        this.keyUIntType = findUIntType(keySize);
        this.valueUIntType = findUIntType(valueSize);

        this.size =  nextPrime(1 << logSize);
        this.entries = new TranspositionTableEntry[(int)this.size];
        Arrays.fill(entries, new TranspositionTableEntry());
    }

    public int index(long key) {
        return (int) (key % size);
    }

    public void reset() {
        for (TranspositionTableEntry entry : entries) {
            entry.key = 0;
            entry.value = 0;
        }
    }

    public void put(long key, int value) {
        int pos = index(key);
        entries[pos].key = key;
        entries[pos].value = value;
    }

    public int get(long key) {
        int pos = index(key);
        TranspositionTableEntry entry = entries[pos];
        return (entry.key == key) ? entry.value : 0;
    }
}
