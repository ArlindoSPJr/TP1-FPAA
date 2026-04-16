package fpaa.grafo.model;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    public static class Aresta {
        public int origem;
        public int destino;
        public int peso;
 
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
        validateVertex(origem);
        validateVertex(destino);
        arestas.add(new Aresta(origem, destino, peso));
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= numVertices) {
            throw new IllegalArgumentException("Vertice invalido: " + v);
        }
    }

    public int getNumVertices() {
        return numVertices;
    }

}
