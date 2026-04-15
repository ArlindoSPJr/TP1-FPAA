package fpaa.algoritmos.dsu;

import fpaa.algoritmos.interfaces.IDsu;
import fpaa.algoritmos.interfaces.IDsuMetrics;

public class UnionByRank implements IDsu, IDsuMetrics {

    private final int[] parent;
    private final int[] rank;
    private long parentReads;
    private long parentWrites;
    private long rankReads;
    private long rankWrites;

    public UnionByRank(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }

        this.parent = new int[n];
        this.rank = new int[n];

        for (int i = 0; i < n; i++) {
            writeParent(i, i);
            writeRank(i, 0);
        }
        resetMetrics();
    }

    @Override
    public void union(int x, int y) {
        validateIndex(x);
        validateIndex(y);

        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return;
        }

        int rankX = readRank(rootX);
        int rankY = readRank(rootY);

        if (rankX < rankY) {
            writeParent(rootX, rootY);
        } else if (rankX > rankY) {
            writeParent(rootY, rootX);
        } else {
            writeParent(rootY, rootX);
            incrementRank(rootX);
        }
    }

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
        rankReads = 0;
        rankWrites = 0;
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
        return rankReads;
    }

    @Override
    public long getRankWrites() {
        return rankWrites;
    }

    private int readParent(int index) {
        parentReads++;
        return parent[index];
    }

    private void writeParent(int index, int value) {
        parentWrites++;
        parent[index] = value;
    }

    private int readRank(int index) {
        rankReads++;
        return rank[index];
    }

    private void writeRank(int index, int value) {
        rankWrites++;
        rank[index] = value;
    }

    private void incrementRank(int index) {
        int current = readRank(index);
        writeRank(index, current + 1);
    }

    private void validateIndex(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + x);
        }
    }
}
