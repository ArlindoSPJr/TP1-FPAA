package fpaa;

import fpaa.algoritmos.dsu.UnionByRank;
import fpaa.algoritmos.interfaces.IDsu;
import fpaa.grafo.kruskal.Kruskal;
import fpaa.grafo.model.Graph;

public class Main {
    public static void main(String[] args) {
        Graph g = new Graph(5); 
 
        g.adicionarAresta(0, 1, 4);
        g.adicionarAresta(0, 2, 2);
        g.adicionarAresta(1, 3, 5);
        g.adicionarAresta(2, 3, 8);
        g.adicionarAresta(3, 4, 3);
 
        g.exibir();

        // IDsu union = new UnionByRank(g.arestas.size());
        // int result = Kruskal.kruskal(4, g.arestas , union);
        // System.out.println(result);
    }
}