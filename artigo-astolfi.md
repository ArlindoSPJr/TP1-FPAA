## 1. Resumo

Este trabalho apresenta uma análise empírica comparativa de três variantes da estrutura Disjoint Set Union (DSU), Naive, Union by Rank e Full Tarjan (Union by Rank combinado com Path Compression), aplicadas ao algoritmo de Kruskal para a obtenção da Árvore Geradora Mínima. Os experimentos foram conduzidos em Java, utilizando grafos conexos gerados de forma determinística, com tamanhos variando entre 2.500 e 40.000 vértices e densidade fixa de 16 arestas por vértice. Como métricas de avaliação, foram considerados o tempo de execução, medido em nanossegundos, e o número total de acessos à memória realizados pela estrutura DSU. Os resultados evidenciam de forma clara a transição entre as classes de complexidade O(n), O(log n) e O(α(n)), enquanto a variante Naive apresenta crescimento explosivo desde a primeira métrica, atingindo cerca de 11,8 bilhões de acessos à memória para n = 40.000, a variante Full Tarjan mantém desempenho praticamente constante, com aproximadamente 4 milhões de acessos no mesmo cenário. Conclui-se que a combinação das heurísticas propostas por Tarjan reduz drasticamente o custo das operações, comprovando empiricamente a relevância da escolha adequada de estruturas de dados em algoritmos de larga escala.

## 2. Introdução

Em diversas situações em que é necessário o gerenciamento dinâmico de conjuntos de elementos, como no processamento de imagens, redes de computadores e bioinformática, o problema fundamental consiste em manter uma partição de um conjunto finito de n elementos em subconjuntos disjuntos, permitindo identificar rapidamente a qual grupo um elemento pertence e unir dois grupos distintos. Entretanto, com o aumento da complexidade computacional, não basta apenas resolver o problema, sendo necessário fazê-lo da forma mais eficiente possível, ou seja, utilizando menos recursos e com menor tempo de execução. Nesse contexto, utilizam-se técnicas avançadas de estruturas de dados, introduzidas por Robert Tarjan, com o objetivo de analisar suas diferentes complexidades, aplicando-as diretamente no algoritmo de Kruskal.

## 3. Metodologia 

Este capítulo descreve o conjunto experimental, incluindo o método de geração de grafos e as escolhas de implementação adotadas para analisar as variantes da estrutura DSU. O foco foi garantir um ambiente controlado e reprodutível que permitisse observar a transição das classes de complexidade de forma empírica

3.1. Estrutura do Código e Modularização

O projeto foi desenvolvido em Java, utilizando uma abordagem modular. A lógica do DSU foi isolada de forma que as três variantes pudessem ser testadas sob a mesma carga de dados:

- Variante Naive: Implementação direta sem critérios de balanceamento ou compressão.
- Variante Union by Rank: Implementação focada na redução da altura da árvore através do controle de rank.
- Variante Full Tarjan: Versão otimizada integrando Union by Rank e Path Compression.

Essa organização foi estratégica para garantir a integridade dos testes comparativos, permitindo que a única variável alterada fosse a estratégia de otimização da estrutura de dados.

3.2. Ambiente de Execução

Para minimizar interferências externas, os testes foram conduzidos em um ambiente com as seguintes especificações:

- Hardware: Processador [Intel(R) Core(TM) i5-9300H], [20] GB de Memória RAM.
- Software: Sistema Operacional [Windows], compilador [java version "21.0.9" 2025-10-21 LTS].

3.3. Procedimento Experimental

A coleta de dados consistiu em submeter as três implementações a sequências crescentes de M operações sobre N elementos.

- Escalabilidade: Os testes variaram de 2.500 a 40.000 elementos para evidenciar o impacto do crescimento assintótico.

- Métricas: Para cada cenário, foram mensuradas duas métricas principais. O tempo de execução foi capturado em nanossegundos utilizando o método `System.nanoTime()`. Adicionalmente, o número total de acessos à memória pela estrutura DSU foi contabilizado. Cada cenário foi executado 3 vezes, e a média aritmética dos resultados foi calculada para mitigar variações pontuais do sistema operacional.

3.4. Geração dos Grafos de Teste

Para garantir a validade e a consistência dos experimentos, a geração de grafos seguiu um método determinístico em duas fases, implementado na função `gerarGrafoAleatorioConexo`.

3.4.1  **Garantia de Conectividade:** Primeiramente, uma árvore geradora é construída para assegurar que o grafo resultante seja sempre conexo. Isso é feito iterativamente: para cada vértice `v` (de 1 a `n-1`), uma aresta é criada conectando-o a um vértice `u` aleatório já existente no grafo (`u < v`). Este passo é crucial, pois garante que o algoritmo de Kruskal sempre possa encontrar uma árvore geradora mínima que abranja todos os vértices.

3.4.2  **Preenchimento Aleatório de Arestas:** Após a criação da árvore inicial com `n-1` arestas, o algoritmo continua a adicionar as `m - (n - 1)` arestas restantes. As novas arestas são inseridas entre pares de vértices aleatórios, com a verificação de que não sejam arestas duplicadas (paralelas). Os pesos de todas as arestas são definidos com valores inteiros aleatórios entre 1 e 1000.

O uso de uma semente (`seed`) no gerador de números aleatórios assegura que, para um mesmo `n`, o grafo gerado seja idêntico em todas as repetições e para todas as variantes do DSU, isolando a performance da estrutura de dados como a única variável do experimento.

## 4. Fundamentação Teórica 

A estrutura Disjoint Set Union (DSU) é avaliada não pelo custo de uma operação isolada, mas pelo custo total de uma sequência de M operações, o que chamamos de análise amortizada. A implementação otimizada proposta por Tarjan é famosa por atingir uma complexidade que beira o limite teórico da computação.

4.1. Heurísticas e a Estrutura de Tarjan

Sem otimizações, a operação Find pode percorrer até (n-1) arestas. Para reduzir esse custo, Tarjan propôs a integração de duas heurísticas:

- Union by Rank: Garante que a altura da árvore cresça apenas quando duas árvores de mesmo rank são unidas, limitando a altura máxima a O(log n).
- Path Compression: Transforma o caminho percorrido em um "feixe" de conexões diretas com a raiz.

A "estrutura de Tarjan" refere-se ao uso simultâneo de ambas. A grande sacada teórica é que a compressão de caminho torna as buscas futuras mais baratas, enquanto o rank impede que a compressão tenha que lidar com caminhos longos demais frequentemente.

4.2. Explicação Formal da Complexidade O(α(n))

A prova formal de Tarjan para a complexidade O(α(n)) baseia-se na análise de como o rank dos nós aumenta e como as arestas são "eliminadas" ou encurtadas pela compressão de caminho.

O rank de um nó define uma hierarquia. Tarjan provou que, ao aplicar a compressão, o custo de percorrer a árvore pode ser distribuído (amortizado) ao longo das operações. A análise envolve dividir os nós em grupos baseados em seus ranks, onde o tamanho de cada grupo é definido pela aplicação sucessiva da função logaritmo (o logaritmo iterado, log n).

Ao generalizar essa partição de grupos para níveis de crescimento ainda mais explosivos que o logaritmo, chega-se à Função de Ackermann. A complexidade O(α(n)) surge porque cada operação de busca pode ser vista como o custo de subir por esses "níveis de rank", e o número de níveis necessários para cobrir qualquer N prático é definido pela inversa dessa função.

4.3. Interpretação Prática e Crescimento da Função

Para entender o quão lenta é a função inversa de Ackermann O(α(n)), é preciso contrastá-la com a função de Ackermann original, A(m, n).

- A função A(m, n) cresce mais rápido que qualquer função exponencial ou fatorial. Por exemplo, A(4, 2) já resulta em um número com milhares de dígitos.
- Consequentemente, sua inversa α(n)  cresce de forma quase imperceptível.
- Na prática, para qualquer valor de N que represente dados reais no planeta (como o número de bytes processados por todos os computadores do mundo ou o número de átomos no universo), o valor de α(n) não ultrapassa 5.

Conclusão Prática: Do ponto de vista da engenharia de software, isso significa que a estrutura é "efetivamente constante". Enquanto um algoritmo α(n) ainda apresenta um aumento perceptível de tempo quando passamos de mil para um bilhão de elementos, o DSU com Tarjan mantém praticamente o mesmo desempenho, tornando-o ideal para sistemas de alta performance e processamento de grandes volumes de dados.