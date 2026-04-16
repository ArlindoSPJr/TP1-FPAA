# Roteiro de Testes

Este documento descreve o passo a passo para validar se a implementacao esta correta e pronta para gerar os dados do trabalho.

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

1. Naiv
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

## 5) Teste do benchmark e geracao do CSV

Objetivo: validar a parte experimental exigida pelo enunciado.

Passos:

1. Executar a classe principal

```bash
cd tp1
mvn exec:java -Dexec.mainClass=fpaa.Main
```

2. Verificar se o arquivo foi criado:

```bash
ls -l results/dsu_benchmark.csv
```

3. Inspecionar inicio do arquivo:

```bash
head -n 5 results/dsu_benchmark.csv
```

Resultado esperado:

1. Arquivo `results/dsu_benchmark.csv` existe
2. Cabecalho contem as colunas esperadas
3. Ha linhas de dados para as tres variantes

---

## 6) Testes de consistencia dos dados do CSV

Objetivo: validar se os valores fazem sentido para analise posterior.

Verificacoes:

1. `tempo_ns > 0` em todas as linhas
2. `total_acessos_memoria = leituras_pai + escritas_pai + leituras_rank + escritas_rank`
3. Naive tende a ter `leituras_rank = 0` e `escritas_rank = 0`
4. `total_mst` para o mesmo grafo (mesma semente, n, m, repeticao) deve ser igual entre variantes

Comandos uteis (opcional):

```bash
wc -l results/dsu_benchmark.csv
```

---

## 7) Criterio de aprovacao interno (pronto para relatorio)

Considerar a implementacao pronta quando:

1. Compila sem erros
2. Testes funcionais DSU passam nas tres variantes
3. Kruskal retorna MST consistente nas tres variantes
4. CSV e gerado com todas as colunas
5. Valores do CSV passam nas verificacoes de consistencia

---

## 8) Proximo passo apos estes testes

Com o CSV validado:

1. Gerar graficos comparando tempo e acessos de memoria por `n` e `m`
2. Discutir no artigo onde cada abordagem se destaca
3. Registrar ambiente de execucao (CPU, RAM, SO, versao da JVM e flags)

