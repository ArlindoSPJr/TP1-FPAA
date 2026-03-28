package fpaa.grafo.model;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    public static class Aresta  {
        public int origem, destino, peso;
 
        Aresta(int origem, int destino, int peso) {
            this.origem = origem;
            this.destino = destino;
            this.peso = peso;
        }
 
        @Override
        public String toString() {
            return origem + " -- " + destino + "  (peso: " + peso + ")";
        }

    }
 
    int numVertices;
    public List<Aresta> arestas;

    public Graph(int numVertices) {
        this.numVertices = numVertices;
        this.arestas = new ArrayList<>();
    }

    public void adicionarAresta(int origem, int destino, int peso) {
        arestas.add(new Aresta(origem, destino, peso));
    }

    public void exibir() {
        System.out.println("=== Grafo (Lista de Arestas) ===");
        System.out.println("Vértices: " + numVertices);
        System.out.println("Arestas:  " + arestas.size());
        for (Aresta a : arestas) {
            System.out.println("  " + a);
        }
    }

}
