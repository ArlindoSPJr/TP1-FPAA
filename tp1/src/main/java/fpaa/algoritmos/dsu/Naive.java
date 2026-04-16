package fpaa.algoritmos.dsu;

import fpaa.algoritmos.interfaces.IDsu;
import fpaa.algoritmos.interfaces.IDsuMetrics;

public class Naive implements IDsu, IDsuMetrics {
    private final int[] pai;
    private long leiturasPai;
    private long escritasPai;

    public Naive(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n deve ser maior que 0");
        }
        this.pai = new int[n];
        for (int i = 0; i < n; i++) {
            escreverPai(i, i);
        }
        resetarMetricas();
    }

    // Naive: faz um apontar para o outro, sem balanceamento
    @Override
    public void union(int x, int y) {
        validateIndex(x);
        validateIndex(y);
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) return;
        escreverPai(rootY, rootX);
    }

    // Naive: vai seguindo os pais até achar a raiz
    @Override
    public int find(int x) {
        validateIndex(x);
        int current = x;
        while (lerPai(current) != current) {
            current = lerPai(current);
        }
        return current;
    }

    @Override
    public void resetarMetricas() {
        leiturasPai = 0;
        escritasPai = 0;
    }

    @Override
    public long getLeiturasPai() {
        return leiturasPai;
    }

    @Override
    public long getEscritasPai() {
        return escritasPai;
    }

    @Override
    public long getLeiturasRank() {
        return 0;
    }

    @Override
    public long getEscritasRank() {
        return 0;
    }

    private int lerPai(int indice) {
        leiturasPai++;
        return pai[indice];
    }

    private void escreverPai(int indice, int valor) {
        escritasPai++;
        pai[indice] = valor;
    }

    private void validateIndex(int x) {
        if (x < 0 || x >= pai.length) {
            throw new IndexOutOfBoundsException("Indice fora dos limites: " + x);
        }
    }
}
