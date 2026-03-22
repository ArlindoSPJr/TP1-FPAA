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
