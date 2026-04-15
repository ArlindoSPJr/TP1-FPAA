package fpaa.grafo.kruskal;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fpaa.algoritmos.interfaces.IDsu;
import fpaa.grafo.model.Graph.Aresta;

public class Kruskal {
    public static int kruskal(List<Aresta> arestas, IDsu dsu) {
        Collections.sort(arestas, Comparator.comparingInt(e -> e.peso));

        int total = 0;

        for (Aresta e : arestas) {
            if (dsu.find(e.origem) != dsu.find(e.destino)) {
                dsu.union(e.origem, e.destino);
                total += e.peso;
            }
        }

        return total;
    }
}
