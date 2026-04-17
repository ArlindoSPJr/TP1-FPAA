# Contexto do Trabalho

## 1) Objetivo pratico desta versao

O projeto implementa e compara tres variantes de DSU (Union-Find) no contexto do Kruskal:

1. Naive
2. UnionByRank
3. FullTarjan (UnionByRank + Path Compression)

O foco desta abordagem e gerar dados limpos para **dois graficos finais**:

1. Grafico de tempo
2. Grafico de memoria

---

## 2) Metodologia experimental atual

Para simplificar a montagem dos graficos e reduzir erros manuais, o benchmark foi padronizado com:

1. Densidade fixa: `densidade = 16` (isto e, `m = 16n`)
2. Tamanhos de entrada: `n = {2500, 5000, 10000, 20000, 40000}`
3. Repeticoes por cenario: `3`

As repeticoes sao agregadas por media no proprio benchmark.

---

## 3) Arquivos de saida finais

A execucao atual gera **somente 2 CSVs finais**:

1. `tp1/results/tempo/tempo_densidade_16.csv`
2. `tp1/results/memoria/memoria_densidade_16.csv`

Os CSVs antigos na pasta `results/tempo` e `results/memoria` sao removidos automaticamente antes de cada nova execucao.

---

## 4) Campos de cada CSV

### CSV de tempo

`variante,n,densidade,media_tempo_ns`

### CSV de memoria

`variante,n,densidade,media_memoria`

Cada CSV possui:

1. 5 valores de `n`
2. 3 variantes

Total: `15` linhas de dados por arquivo.

### Legenda dos campos

1. `variante`: nome da implementacao DSU usada no experimento (`Naive`, `UnionByRank`, `FullTarjan`).
2. `n`: tamanho do grafo em numero de vertices.
3. `densidade`: fator fixo usado na geracao das arestas. Neste trabalho, `densidade = 16`, logo `m = 16n`.
4. `media_tempo_ns`: media do tempo de execucao (em nanossegundos) nas repeticoes do mesmo cenario.
5. `media_memoria`: media de acessos a memoria da DSU nas repeticoes do mesmo cenario.

Observacao:

1. `m` (numero de arestas) nao aparece nos CSVs finais porque ele e derivado diretamente de `n` e `densidade` (`m = densidade x n`).

---

## 5) Coerencia com o enunciado

No escopo de implementacao de codigo, a versao atual permanece alinhada:

1. Implementa as 3 variantes exigidas de DSU
2. Aplica DSU no Kruskal
3. Mede tempo e acessos de memoria
4. Entrega dados comparativos para os dois graficos

Observacao importante:

1. O trabalho completo ainda exige a parte externa ao codigo (graficos no artigo, discussao teorica e descricao de ambiente de execucao), conforme o enunciado.

---

## 6) Leitura rapida dos resultados

Com os CSVs finais, a comparacao por `n` fica direta:

1. Naive tende a escalar pior em tempo e memoria
2. UnionByRank e FullTarjan ficam significativamente melhores
3. FullTarjan tende a usar menos memoria e competir fortemente em tempo

Isso facilita mostrar, na discussao do artigo, o impacto das heuristicas de balanceamento e compressao de caminho.
