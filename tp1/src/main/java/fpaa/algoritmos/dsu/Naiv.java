package fpaa.algoritmos.dsu;

import fpaa.algoritmos.interfaces.IDsu;

public class Naiv implements IDsu {

    private final int[] parent;

    public Naiv(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }

        this.parent = new int[n];

        for (int i = 0; i < n; i++) {
            parent[i] = i;
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

        parent[rootX] = rootY;
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
