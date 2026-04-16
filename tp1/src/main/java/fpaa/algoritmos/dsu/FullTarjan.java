package fpaa.algoritmos.dsu;

import fpaa.algoritmos.interfaces.IDsu;
import fpaa.algoritmos.interfaces.IDsuMetrics;

public class FullTarjan implements IDsu, IDsuMetrics {
    private final int[] pai;
    private final int[] rank;
    private long leiturasPai;
    private long escritasPai;
    private long leiturasRank;
    private long escritasRank;

    public FullTarjan(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n deve ser maior que 0");
        }

        this.pai = new int[n];
        this.rank = new int[n];

        for (int i = 0; i < n; i++) {
            escreverPai(i, i);
            escreverRank(i, 0);
        }
        resetarMetricas();
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

        int rankX = lerRank(rootX);
        int rankY = lerRank(rootY);

        if (rankX < rankY) {
            escreverPai(rootX, rootY);
        } else if (rankX > rankY) {
            escreverPai(rootY, rootX);
        } else {
            escreverPai(rootY, rootX);
            incrementarRank(rootX);
        }
    }

    @Override
    public int find(int x) {
        validateIndex(x);

        int paiAtual = lerPai(x);
        if (paiAtual != x) {
            int raiz = find(paiAtual);
            escreverPai(x, raiz);
            return raiz;
        }

        return paiAtual;
    }

    @Override
    public void resetarMetricas() {
        leiturasPai = 0;
        escritasPai = 0;
        leiturasRank = 0;
        escritasRank = 0;
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
        return leiturasRank;
    }

    @Override
    public long getEscritasRank() {
        return escritasRank;
    }

    private int lerPai(int indice) {
        leiturasPai++;
        return pai[indice];
    }

    private void escreverPai(int indice, int valor) {
        escritasPai++;
        pai[indice] = valor;
    }

    private int lerRank(int indice) {
        leiturasRank++;
        return rank[indice];
    }

    private void escreverRank(int indice, int valor) {
        escritasRank++;
        rank[indice] = valor;
    }

    private void incrementarRank(int indice) {
        int atual = lerRank(indice);
        escreverRank(indice, atual + 1);
    }

    private void validateIndex(int x) {
        if (x < 0 || x >= pai.length) {
            throw new IndexOutOfBoundsException("Indice fora dos limites: " + x);
        }
    }
}
