package fpaa.algoritmos.interfaces;

public interface IDsuMetrics {
    void resetarMetricas();

    long getLeiturasPai();

    long getEscritasPai();

    long getLeiturasRank();

    long getEscritasRank();

    default long getTotalAcessosMemoria() {
        return getLeiturasPai() + getEscritasPai() + getLeiturasRank() + getEscritasRank();
    }
}