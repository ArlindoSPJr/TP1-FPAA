package fpaa.algoritmos.dsu;

import fpaa.algoritmos.interfaces.IDsu;

public class UnionByRank implements IDsu {

    private final int[] parent;
    private final int[] rank;

    public UnionByRank(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }

        this.parent = new int[n];
        this.rank = new int[n];

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
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

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }

    @Override
    public int find(int x) {
        validateIndex(x);

        int current = x;
        while (parent[current] != current) {
            current = parent[current];
        }

        return current;
    }

    private void validateIndex(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + x);
        }
    }
}
