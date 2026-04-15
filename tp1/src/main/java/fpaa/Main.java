package fpaa;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
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
    private static final int[] VALORES_N = {100, 500, 1000, 2000};
    private static final int[] FATORES_M = {2, 4, 8};
    private static final int REPETICOES = 3;
    private static final long SEMENTE_BASE = 12345L;

    public static void main(String[] args) {
        executarBenchmark();
    }

    private static void executarBenchmark() {
        Path saida = Paths.get("results", "dsu_benchmark.csv");
        try {
            Files.createDirectories(saida.getParent());
        } catch (IOException e) {
            throw new RuntimeException("Falha ao criar diretorio de saida", e);
        }

        try (BufferedWriter escritor = Files.newBufferedWriter(saida)) {
            escritor.write("variante,n,m,repeticao,semente,tempo_ns,leituras_pai,escritas_pai,leituras_rank,escritas_rank,total_acessos_memoria,total_mst");
            escritor.newLine();

            for (int n : VALORES_N) {
                int maxArestas = n * (n - 1) / 2;

                for (int fator : FATORES_M) {
                    int m = Math.min(maxArestas, n * fator);

                    for (int repeticao = 1; repeticao <= REPETICOES; repeticao++) {
                        long semente = SEMENTE_BASE + (long) n * 1_000_000L + (long) m * 1_000L + repeticao;
                        Graph grafo = gerarGrafoAleatorioConexo(n, m, semente);

                        escreverLinhaBenchmark(escritor, "Naive", new Naiv(n), grafo, n, m, repeticao, semente);
                        escreverLinhaBenchmark(escritor, "UnionByRank", new UnionByRank(n), grafo, n, m, repeticao, semente);
                        escreverLinhaBenchmark(escritor, "FullTarjan", new FullTarjan(n), grafo, n, m, repeticao, semente);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Falha ao escrever CSV do benchmark", e);
        }

        System.out.println("Benchmark finalizado. CSV gerado em: " + saida.toString());
    }

    private static void escreverLinhaBenchmark(
        BufferedWriter escritor,
        String variante,
        IDsu dsu,
        Graph grafo,
        int n,
        int m,
        int repeticao,
        long semente
    ) throws IOException {
        IDsuMetrics metricas = (IDsuMetrics) dsu;
        metricas.resetarMetricas();

        long inicio = System.nanoTime();
        int mst = Kruskal.kruskal(new ArrayList<>(grafo.arestas), dsu);
        long tempoDecorrido = System.nanoTime() - inicio;

        escritor.write(
            variante + ","
                + n + ","
                + m + ","
                + repeticao + ","
                + semente + ","
                + tempoDecorrido + ","
                + metricas.getLeiturasPai() + ","
                + metricas.getEscritasPai() + ","
                + metricas.getLeiturasRank() + ","
                + metricas.getEscritasRank() + ","
                + metricas.getTotalAcessosMemoria() + ","
                + mst
        );
        escritor.newLine();
    }

    private static Graph gerarGrafoAleatorioConexo(int n, int m, long semente) {
        if (n <= 0) {
            throw new IllegalArgumentException("n deve ser maior que 0");
        }

        int minArestas = n - 1;
        int maxArestas = n * (n - 1) / 2;

        if (m < minArestas || m > maxArestas) {
            throw new IllegalArgumentException("m deve estar em [n-1, n*(n-1)/2]");
        }

        Graph grafo = new Graph(n);
        Random aleatorio = new Random(semente);
        Set<Long> arestasUsadas = new HashSet<>();

        for (int v = 1; v < n; v++) {
            int u = aleatorio.nextInt(v);
            int w = gerarPesoAleatorio(aleatorio);
            grafo.adicionarAresta(u, v, w);
            arestasUsadas.add(chaveAresta(u, v));
        }

        while (grafo.arestas.size() < m) {
            int u = aleatorio.nextInt(n);
            int v = aleatorio.nextInt(n);

            if (u == v) {
                continue;
            }

            long chave = chaveAresta(u, v);
            if (arestasUsadas.contains(chave)) {
                continue;
            }

            int w = gerarPesoAleatorio(aleatorio);
            grafo.adicionarAresta(u, v, w);
            arestasUsadas.add(chave);
        }

        return grafo;
    }

    private static long chaveAresta(int u, int v) {
        int a = Math.min(u, v);
        int b = Math.max(u, v);
        return (((long) a) << 32) | (b & 0xffffffffL);
    }

    private static int gerarPesoAleatorio(Random aleatorio) {
        return aleatorio.nextInt(1000) + 1;
    }
}