package fpaa;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import fpaa.algoritmos.dsu.FullTarjan;
import fpaa.algoritmos.dsu.Naive;
import fpaa.algoritmos.dsu.UnionByRank;
import fpaa.algoritmos.interfaces.IDsu;
import fpaa.algoritmos.interfaces.IDsuMetrics;
import fpaa.grafo.kruskal.Kruskal;
import fpaa.grafo.model.Graph;

public class Main {
    private static final int[] VALORES_N = { 2500, 5000, 10000, 20000, 40000 };
    private static final int[] FATORES_M = { 16 };
    private static final int REPETICOES = 3;
    private static final long SEMENTE_BASE = 12345L;
    private static final String[] VARIANTES = { "Naive", "UnionByRank", "FullTarjan" };

    public static void main(String[] args) {
        executarBenchmark();
    }

    private static void executarBenchmark() {
        if (FATORES_M.length != 1) {
            throw new IllegalStateException("Este perfil exige exatamente uma densidade fixa");
        }

        int densidade = FATORES_M[0];
        Path pastaTempo = Paths.get("results", "tempo");
        Path pastaMemoria = Paths.get("results", "memoria");
        long[][] somaTempo = new long[VALORES_N.length][VARIANTES.length];
        long[][] somaMemoria = new long[VALORES_N.length][VARIANTES.length];

        try {
            Files.createDirectories(pastaTempo);
            Files.createDirectories(pastaMemoria);
            limparCsvsAntigos(pastaTempo);
            limparCsvsAntigos(pastaMemoria);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao criar diretorio de saida", e);
        }

        try {
            for (int i = 0; i < VALORES_N.length; i++) {
                int n = VALORES_N[i];
                int maxArestas = n * (n - 1) / 2;
                int m = Math.min(maxArestas, n * densidade);

                for (int repeticao = 1; repeticao <= REPETICOES; repeticao++) {
                    long semente = SEMENTE_BASE + (long) n * 1_000_000L + (long) m * 1_000L + repeticao;
                    Graph grafo = gerarGrafoAleatorioConexo(n, m, semente);

                    Medicao medicaoNaive = medirExecucao(new Naive(n), grafo);
                    somaTempo[i][0] += medicaoNaive.tempoNs;
                    somaMemoria[i][0] += medicaoNaive.totalMemoria;

                    Medicao medicaoUnionByRank = medirExecucao(new UnionByRank(n), grafo);
                    somaTempo[i][1] += medicaoUnionByRank.tempoNs;
                    somaMemoria[i][1] += medicaoUnionByRank.totalMemoria;

                    Medicao medicaoFullTarjan = medirExecucao(new FullTarjan(n), grafo);
                    somaTempo[i][2] += medicaoFullTarjan.tempoNs;
                    somaMemoria[i][2] += medicaoFullTarjan.totalMemoria;
                }
            }

            escreverCsvTempo(pastaTempo.resolve("tempo_densidade_" + densidade + ".csv"), densidade, somaTempo);
            escreverCsvMemoria(pastaMemoria.resolve("memoria_densidade_" + densidade + ".csv"), densidade, somaMemoria);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao escrever CSVs do benchmark", e);
        }

        System.out.println("Benchmark finalizado. CSVs finais gerados em: results/tempo e results/memoria");
    }

    private static void limparCsvsAntigos(Path pasta) throws IOException {
        try (var stream = Files.list(pasta)) {
            for (Path arquivo : (Iterable<Path>) stream::iterator) {
                if (Files.isRegularFile(arquivo) && arquivo.toString().endsWith(".csv")) {
                    Files.delete(arquivo);
                }
            }
        }
    }

    private static Medicao medirExecucao(IDsu dsu, Graph grafo) {
        IDsuMetrics metricas = (IDsuMetrics) dsu;
        metricas.resetarMetricas();

        long inicio = System.nanoTime();
        Kruskal.kruskal(new ArrayList<>(grafo.arestas), dsu);
        long tempoDecorrido = System.nanoTime() - inicio;

        return new Medicao(tempoDecorrido, metricas.getTotalAcessosMemoria());
    }

    private static void escreverCsvTempo(Path arquivo, int densidade, long[][] somaTempo) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(arquivo)) {
            escritor.write("variante,n,densidade,media_tempo_ns");
            escritor.newLine();

            for (int i = 0; i < VALORES_N.length; i++) {
                for (int j = 0; j < VARIANTES.length; j++) {
                    double media = somaTempo[i][j] / (double) REPETICOES;
                    escritor.write(
                            VARIANTES[j] + ","
                                    + VALORES_N[i] + ","
                                    + densidade + ","
                                    + String.format(Locale.US, "%.2f", media));
                    escritor.newLine();
                }
            }
        }
    }

    private static void escreverCsvMemoria(Path arquivo, int densidade, long[][] somaMemoria) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(arquivo)) {
            escritor.write("variante,n,densidade,media_memoria");
            escritor.newLine();

            for (int i = 0; i < VALORES_N.length; i++) {
                for (int j = 0; j < VARIANTES.length; j++) {
                    double media = somaMemoria[i][j] / (double) REPETICOES;
                    escritor.write(
                            VARIANTES[j] + ","
                                    + VALORES_N[i] + ","
                                    + densidade + ","
                                    + String.format(Locale.US, "%.2f", media));
                    escritor.newLine();
                }
            }
        }
    }

    private static class Medicao {
        private final long tempoNs;
        private final long totalMemoria;

        private Medicao(long tempoNs, long totalMemoria) {
            this.tempoNs = tempoNs;
            this.totalMemoria = totalMemoria;
        }
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