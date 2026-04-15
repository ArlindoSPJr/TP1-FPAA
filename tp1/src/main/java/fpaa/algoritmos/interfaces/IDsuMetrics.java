package fpaa.algoritmos.interfaces;

public interface IDsuMetrics {
    void resetMetrics();

    long getParentReads();

    long getParentWrites();

    long getRankReads();

    long getRankWrites();

    default long getTotalMemoryAccesses() {
        return getParentReads() + getParentWrites() + getRankReads() + getRankWrites();
    }
}