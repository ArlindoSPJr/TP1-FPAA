package fpaa.algoritmos.dsu;

import fpaa.algoritmos.interfaces.IDsu;
import fpaa.algoritmos.interfaces.IDsuMetrics;

public class Naiv implements IDsu, IDsuMetrics {
    private final int[] parent;
    private long parentReads;
    private long parentWrites;

    public Naiv(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        this.parent = new int[n];
        for (int i = 0; i < n; i++) {
            writeParent(i, i);
        }
        resetMetrics();
    }

    // Naive: faz um apontar para o outro, sem balanceamento
    @Override
    public void union(int x, int y) {
        validateIndex(x);
        validateIndex(y);
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) return;
        writeParent(rootY, rootX);
    }

    // Naive: vai seguindo os pais até achar a raiz
    @Override
    public int find(int x) {
        validateIndex(x);
        int current = x;
        while (readParent(current) != current) {
            current = readParent(current);
        }
        return current;
    }

    @Override
    public void resetMetrics() {
        parentReads = 0;
        parentWrites = 0;
    }

    @Override
    public long getParentReads() {
        return parentReads;
    }

    @Override
    public long getParentWrites() {
        return parentWrites;
    }

    @Override
    public long getRankReads() {
        return 0;
    }

    @Override
    public long getRankWrites() {
        return 0;
    }

    private int readParent(int index) {
        parentReads++;
        return parent[index];
    }

    private void writeParent(int index, int value) {
        parentWrites++;
        parent[index] = value;
    }

    private void validateIndex(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + x);
        }
    }
}
