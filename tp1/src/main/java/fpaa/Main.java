package fpaa;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import fpaa.algoritmos.dsu.FullTarjan;
import fpaa.algoritmos.dsu.Naiv;
import fpaa.algoritmos.dsu.UnionByRank;
import fpaa.algoritmos.interfaces.IDsu;
import fpaa.algoritmos.interfaces.IDsuMetrics;
import fpaa.grafo.kruskal.Kruskal;
import fpaa.grafo.model.Graph;

public class Main {
    private static final int[] N_VALUES = {100, 500, 1000, 2000};
    private static final int[] M_FACTORS = {2, 4, 8};
    private static final int REPETITIONS = 3;
    private static final long BASE_SEED = 12345L;

    public static void main(String[] args) {
        runBenchmark();
    }

    private static void runBenchmark() {
        Path output = Paths.get("results", "dsu_benchmark.csv");
        try {
            Files.createDirectories(output.getParent());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create output directory", e);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            writer.write("variant,n,m,rep,seed,time_ns,parent_reads,parent_writes,rank_reads,rank_writes,total_memory_accesses,mst_total");
            writer.newLine();

            for (int n : N_VALUES) {
                int maxEdges = n * (n - 1) / 2;

                for (int factor : M_FACTORS) {
                    int m = Math.min(maxEdges, n * factor);

                    for (int rep = 1; rep <= REPETITIONS; rep++) {
                        long seed = BASE_SEED + (long) n * 1_000_000L + (long) m * 1_000L + rep;
                        Graph graph = generateRandomConnectedGraph(n, m, seed);

                        writeBenchmarkRow(writer, "Naive", new Naiv(n), graph, n, m, rep, seed);
                        writeBenchmarkRow(writer, "UnionByRank", new UnionByRank(n), graph, n, m, rep, seed);
                        writeBenchmarkRow(writer, "FullTarjan", new FullTarjan(n), graph, n, m, rep, seed);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write benchmark CSV", e);
        }

        System.out.println("Benchmark finished. CSV generated at: " + output.toString());
    }

    private static void writeBenchmarkRow(
        BufferedWriter writer,
        String variant,
        IDsu dsu,
        Graph graph,
        int n,
        int m,
        int rep,
        long seed
    ) throws IOException {
        IDsuMetrics metrics = (IDsuMetrics) dsu;
        metrics.resetMetrics();

        long start = System.nanoTime();
        int mst = Kruskal.kruskal(new ArrayList<>(graph.arestas), dsu);
        long elapsed = System.nanoTime() - start;

        writer.write(
            variant + ","
                + n + ","
                + m + ","
                + rep + ","
                + seed + ","
                + elapsed + ","
                + metrics.getParentReads() + ","
                + metrics.getParentWrites() + ","
                + metrics.getRankReads() + ","
                + metrics.getRankWrites() + ","
                + metrics.getTotalMemoryAccesses() + ","
                + mst
        );
        writer.newLine();
    }

    private static Graph generateRandomConnectedGraph(int n, int m, long seed) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }

        int minEdges = n - 1;
        int maxEdges = n * (n - 1) / 2;

        if (m < minEdges || m > maxEdges) {
            throw new IllegalArgumentException("m must be in [n-1, n*(n-1)/2]");
        }

        Graph graph = new Graph(n);
        Random random = new Random(seed);
        Set<Long> usedEdges = new HashSet<>();

        for (int v = 1; v < n; v++) {
            int u = random.nextInt(v);
            int w = randomWeight(random);
            graph.adicionarAresta(u, v, w);
            usedEdges.add(edgeKey(u, v));
        }

        while (graph.arestas.size() < m) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);

            if (u == v) {
                continue;
            }

            long key = edgeKey(u, v);
            if (usedEdges.contains(key)) {
                continue;
            }

            int w = randomWeight(random);
            graph.adicionarAresta(u, v, w);
            usedEdges.add(key);
        }

        return graph;
    }

    private static long edgeKey(int u, int v) {
        int a = Math.min(u, v);
        int b = Math.max(u, v);
        return (((long) a) << 32) | (b & 0xffffffffL);
    }

    private static int randomWeight(Random random) {
        return random.nextInt(1000) + 1;
    }
}