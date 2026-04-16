# Roteiro de Testes

Este documento descreve o passo a passo para validar se a implementacao esta correta e pronta para gerar os **2 CSVs finais** usados nos graficos do trabalho.

## 1) Pre-requisitos

Executar estes testes na maquina que possui Java e Maven instalados.

1. Java 17 ou superior
2. Maven instalado

Comandos de verificacao:

```bash
java -version
mvn -version
```

---

## 2) Compilacao do projeto

Objetivo: garantir que o codigo compila sem erros.

Passos:

1. Entrar na pasta do modulo Java
2. Compilar o projeto

```bash
cd tp1
mvn -DskipTests compile
```

Resultado esperado:

1. Build finaliza com sucesso
2. Nenhum erro de compilacao nas classes DSU, Kruskal, Graph e Main

---

## 3) Testes funcionais de corretude da DSU

Objetivo: validar comportamento logico de `find` e `union` nas tres variantes.

### Caso 3.1 - Conjuntos simples

Sequencia conceitual:

1. Criar DSU com `n = 5`
2. Executar `union(0,1)`, `union(1,2)`, `union(3,4)`
3. Verificar:
4. `find(0) == find(2)` deve ser verdadeiro
5. `find(0) == find(4)` deve ser falso

Executar o mesmo caso para:

1. Naive
2. UnionByRank
3. FullTarjan

Resultado esperado:

1. As tres variantes retornam respostas equivalentes de conectividade

### Caso 3.2 - Operacoes repetidas

Sequencia conceitual:

1. Repetir `union` em elementos ja conectados
2. Chamar `find` varias vezes no mesmo elemento

Resultado esperado:

1. Sem excecoes indevidas
2. Resultado de conectividade permanece consistente

### Caso 3.3 - Entradas invalidas

Sequencia conceitual:

1. Tentar criar DSU com `n <= 0`
2. Tentar `find` e `union` com indices fora do intervalo

Resultado esperado:

1. Excecoes sao lancadas (validacao funcionando)

---

## 4) Teste de corretude da integracao com Kruskal

Objetivo: validar se Kruskal com DSU gera resultado de MST coerente.

Passos:

1. Montar um grafo pequeno conhecido
2. Rodar Kruskal com cada DSU
3. Comparar o valor total da MST

Resultado esperado:

1. Mesmo valor de MST para as tres variantes
2. Nenhuma variante altera o resultado final do algoritmo

---

## 5) Teste do benchmark e geracao dos CSVs finais

Objetivo: validar a parte experimental exigida pelo enunciado.

Passos:

1. Executar a classe principal

```bash
cd tp1
mvn exec:java "-Dexec.mainClass=fpaa.Main"
```

2. Verificar se os arquivos finais foram criados:

- `results/tempo/tempo_densidade_16.csv`
- `results/memoria/memoria_densidade_16.csv`

3. Inspecionar inicio dos arquivos:

```bash
Get-Content results/tempo/tempo_densidade_16.csv -TotalCount 5
Get-Content results/memoria/memoria_densidade_16.csv -TotalCount 5
```

Resultado esperado:

1. Existem apenas os 2 CSVs finais de saida para o ciclo atual
2. Cabecalhos estao no formato final:
   - Tempo: `variante,n,densidade,media_tempo_ns`
   - Memoria: `variante,n,densidade,media_memoria`
3. Cada arquivo possui 15 linhas de dados (5 tamanhos x 3 variantes)

---

## 6) Testes de consistencia dos dados finais

Objetivo: validar se os valores fazem sentido para analise posterior.

Verificacoes:

1. `media_tempo_ns > 0` em todas as linhas do CSV de tempo
2. `media_memoria > 0` em todas as linhas do CSV de memoria
3. `densidade` deve ser constante e igual a `16`
4. Para cada `n`, devem existir exatamente 3 linhas (Naive, UnionByRank, FullTarjan)
5. Tendencia geral esperada:
   - Naive com maior custo de tempo e memoria
   - UnionByRank e FullTarjan significativamente melhores

Comandos uteis de conferencia:

```bash
$tempo = Import-Csv results/tempo/tempo_densidade_16.csv
$mem = Import-Csv results/memoria/memoria_densidade_16.csv
$tempo.Count
$mem.Count
($tempo | Select-Object -ExpandProperty densidade -Unique)
($mem | Select-Object -ExpandProperty densidade -Unique)
```

---

## 7) Criterio de aprovacao interno (pronto para relatorio)

Considerar a implementacao pronta quando:

1. Compila sem erros
2. Testes funcionais DSU passam nas tres variantes
3. Kruskal retorna MST consistente nas tres variantes
4. Os 2 CSVs finais sao gerados no formato definido
5. Valores dos CSVs finais passam nas verificacoes de consistencia

---

## 8) Proximo passo apos estes testes

Com os CSVs finais validados:

1. Gerar o grafico de tempo comparando as 3 variantes por `n`
2. Gerar o grafico de memoria comparando as 3 variantes por `n`
3. No texto do artigo, informar que a densidade foi fixa (`m = 16n`)
4. Discutir no artigo onde cada abordagem se destaca
5. Registrar ambiente de execucao (CPU, RAM, SO, versao da JVM e flags)
