## Introdução

Em diversas situações em que é necessário o gerenciamento dinâmico de conjuntos de elementos, como no processamento de imagens, redes de computadores e bioinformática, o problema fundamental consiste em manter uma partição de um conjunto finito de n elementos em subconjuntos disjuntos, permitindo identificar rapidamente a qual grupo um elemento pertence e unir dois grupos distintos. Entretanto, com o aumento da complexidade computacional, não basta apenas resolver o problema, sendo necessário fazê-lo da forma mais eficiente possível, ou seja, utilizando menos recursos e com menor tempo de execução. Nesse contexto, utilizam-se técnicas avançadas de estruturas de dados, introduzidas por Robert Tarjan, com o objetivo de analisar suas diferentes complexidades, aplicando-as diretamente no algoritmo de Kruskal.

## Justificativa de Modularizacao da Implementacao Union by Rank

Para atender ao enunciado, a implementacao do DSU foi organizada em modulos com responsabilidade unica:

- Interface comum: `IDsu` define apenas as operacoes essenciais (`find` e `union`), sem expor detalhes internos da estrutura.
- Implementacao concreta: `UnionByRank` concentra exclusivamente a heuristica de uniao por rank (altura aproximada), sem path compression.
- Separacao por pacote: as variacoes de DSU permanecem em `fpaa.algoritmos.dsu`, enquanto o contrato comum fica em `fpaa.algoritmos.interfaces`.

Essa organizacao traz tres ganhos diretos:

1. Extensibilidade

Novas estrategias (por exemplo, Naive e Full Tarjan) podem ser adicionadas como novas classes que implementam `IDsu`, sem alterar o codigo cliente que consome a estrutura. Isso reduz acoplamento e facilita evolucao incremental.

2. Comparabilidade experimental

Como todas as versoes compartilham o mesmo contrato, os testes comparativos executam o mesmo fluxo de chamadas (`find` e `union`) mudando apenas a classe instanciada. Isso melhora a isonomia dos experimentos e reduz vies de implementacao no benchmark.

3. Manutenibilidade e validacao

A logica da heuristica de rank fica isolada em um unico ponto (`UnionByRank`), facilitando revisao, depuracao e validacao de complexidade esperada `O(log n)` nesta variante.

### Decisoes especificas da classe UnionByRank

- Estruturas internas: vetores `parent` e `rank`.
- Regra de uniao: a raiz de menor rank aponta para a de maior rank.
- Empate de rank: escolhe-se uma raiz e incrementa-se seu rank em 1.
- `find` sem compressao de caminho: preserva a caracterizacao da versao Union by Rank solicitada no enunciado.

Com isso, a implementacao respeita a restricao de nao utilizar bibliotecas prontas para a logica central do DSU e mantem a arquitetura necessaria para extensao e comparacao entre abordagens.

## Justificativa de Modularizacao da Implementacao DSU Naive

A implementacao `Naiv` representa a abordagem mais simples do DSU, sem qualquer heuristica de otimizacao:

- Estrutura interna: apenas o vetor `parent`, onde cada elemento inicialmente aponta para si mesmo.
- `find` sem compressao de caminho: percorre os ponteiros de pai ate atingir a raiz, resultando em complexidade `O(n)` no pior caso.
- `union` sem rank: localiza as raizes dos dois elementos e faz a raiz de `x` apontar para a raiz de `y`, sem considerar alturas ou tamanhos das arvores.

### Decisoes especificas da classe Naiv

- Ausencia de vetor `rank`: desnecessario na abordagem naive, onde a uniao e feita sem criterio de balanceamento.
- Complexidade esperada: `O(n)` para `find` e `union` no pior caso, caracterizando a versao baseline para comparacao com as demais abordagens.
- Validacao de indices: mantida identica ao `UnionByRank` para consistencia e seguranca na integracao com o algoritmo de Kruskal.

Essa implementacao serve como referencia de desempenho inferior, evidenciando o ganho trazido pelas heuristicas de union by rank e path compression nas demais variantes.
