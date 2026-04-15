# Contexto do Trabalho

## 1) O que estamos fazendo neste trabalho

Este trabalho implementa e compara tres variantes da estrutura Disjoint Set Union (DSU), tambem chamada Union-Find:

1. Naive (sem heuristicas)
2. Union by Rank
3. Full Tarjan (Union by Rank + Path Compression)

O objetivo principal e mostrar, na pratica, como as otimizações mudam o custo das operacoes `find` e `union` quando o tamanho do problema cresce.

Para atender ao enunciado, escolhemos usar a DSU no contexto do algoritmo de Kruskal (arvore geradora minima), que e uma aplicacao classica de DSU.

---

## 2) O que significa benchmark neste trabalho

Benchmark e uma execucao controlada para medir desempenho.

Neste projeto, benchmark significa:

1. Gerar varios grafos com tamanhos controlados (`n` vertices e `m` arestas)
2. Rodar o Kruskal com cada variante de DSU nas mesmas entradas
3. Medir tempo de execucao
4. Medir acessos a memoria internos da DSU
5. Salvar tudo em CSV para gerar graficos comparativos depois

Em resumo: benchmark nao e um algoritmo novo. E o procedimento de experimento para comparar algoritmos em condicoes equivalentes.

---

## 3) O que estamos medindo exatamente

Medimos duas familias de metricas:

1. Tempo de execucao (`tempo_ns`)
2. Acessos a memoria da DSU

Os acessos a memoria sao contados por contadores internos nas estruturas:

1. `leituras_pai`
2. `escritas_pai`
3. `leituras_rank`
4. `escritas_rank`
5. `total_acessos_memoria` (soma dos quatro)

Por que isso esta alinhado ao enunciado?

Porque o enunciado pede explicitamente tempo e numero de acessos a memoria (ou operacoes de ponteiros). Em DSU, os acessos relevantes ocorrem justamente nos vetores internos `pai` e `rank`.

---

## 4) Escolhas de implementacao e justificativas

### 4.1 Escolha do cenario: Kruskal

Escolhemos Kruskal porque:

1. E permitido pelo enunciado
2. E uma aplicacao direta e classica de DSU
3. Permite comparar as tres variantes em um problema real de grafos

### 4.2 Modularizacao

A modularizacao adotada foi:

1. Interface comum para DSU (`IDsu`)
2. Tres implementacoes intercambiaveis (`Naiv`, `UnionByRank`, `FullTarjan`)
3. Algoritmo de Kruskal desacoplado da implementacao concreta de DSU
4. Modelo de grafo separado (`Graph`)
5. Execucao de benchmark centralizada em `Main`

Beneficio: conseguimos trocar a DSU sem alterar o Kruskal, o que facilita comparacoes justas e extensoes futuras.

### 4.3 Coleta de metricas

A coleta foi implementada por interface especifica (`IDsuMetrics`) com contadores nas classes DSU.

Beneficio: metricas ficam padronizadas entre variantes e o benchmark consegue extrair os valores de forma uniforme.

---

## 5) Como o experimento esta organizado

No benchmark atual:

1. Variamos `n` (tamanho do conjunto de vertices)
2. Variamos `m` (numero de arestas)
3. Repetimos cada cenario para reduzir ruido
4. Mantemos semente pseudoaleatoria para reprodutibilidade
5. Gravamos uma linha por execucao no CSV

Colunas do CSV:

1. `variante`
2. `n`
3. `m`
4. `repeticao`
5. `semente`
6. `tempo_ns`
7. `leituras_pai`
8. `escritas_pai`
9. `leituras_rank`
10. `escritas_rank`
11. `total_acessos_memoria`
12. `total_mst`

---

## 6) Atendimento item a item do enunciado

### Item 4 - Implementacoes requeridas

Atendido.

1. Naive implementada
2. Union by Rank implementada
3. Full Tarjan implementada com compressao de caminho

### Item 5 - Restricoes e modularizacao

Atendido no codigo.

1. Linguagem Java
2. Sem uso de biblioteca pronta de DSU
3. Projeto modular com interface + implementacoes + algoritmo consumidor

Observacao: descricao detalhada do ambiente (hardware/JVM/flags) deve ser colocada no relatorio apos executar na maquina final.

### Item 6 - Analise experimental

Atendido na parte de codigo.

1. Medicao de tempo implementada
2. Medicao de acessos a memoria implementada
3. Variacao de `n` e `m` implementada
4. Exportacao para CSV implementada

Observacao: os graficos comparativos ainda precisam ser gerados fora do codigo a partir do CSV (como pede o enunciado).

### Itens 8 e 9 - Relatorio e avaliacao

Parcialmente fora do escopo do codigo-fonte.

1. O codigo ja gera os dados necessarios
2. Falta consolidar no artigo: metodologia, ambiente, graficos, discussao e conclusao

---

## 7) Resumo final (visao didatica)

Nosso trabalho implementa tres DSUs, aplica as tres no Kruskal, mede tempo e custo de memoria, e salva resultados em CSV para analise comparativa.

Isso permite demonstrar, de forma empirica e reproduzivel, a diferenca de desempenho entre:

1. abordagem ingenua
2. heuristica por rank
3. heuristica por rank + compressao de caminho

Assim, atendemos o nucleo tecnico do enunciado e deixamos pronta a base para os graficos e a discussao final no artigo.
