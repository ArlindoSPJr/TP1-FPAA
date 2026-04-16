# 1 Contextualização e Motivação

Em diversos domínios da computação, como o processamento de imagens, redes de computadores e bioinformática, surge a necessidade de gerenciar grupos de elementos que se fundem ao longo do tempo. O problema fundamental consiste em manter uma partição de um conjunto finito de *n* elementos em subconjuntos disjuntos, permitindo identificar rapidamente a qual grupo um elemento pertence e unir dois grupos distintos.

Embora uma implementação ingênua utilizando listas ou árvores simples pareça suficiente, o desempenho degrada drasticamente conforme o número de operações aumenta. O desafio proposto por este trabalho é investigar como técnicas avançadas de estruturação de dados, especificamente aquelas introduzidas por Robert Tarjan, podem reduzir a complexidade das operações para um tempo amortizado quase constante. Esse ganho de eficiência transforma a viabilidade de algoritmos fundamentais em larga escala, como o Algoritmo de Kruskal para Árvores Geradoras Mínimas (MST).

---

# 2 O Problema da Conectividade Dinâmica

A estrutura *Disjoint Set Union* (DSU), também conhecida como *Union-Find*, busca solucionar o problema da **Conectividade Dinâmica**. A estrutura deve gerenciar um conjunto de *n* elementos e processar eficientemente uma sequência de *m* operações de dois tipos principais:

- **Find(i):** Retorna um representante (ou "pai") do conjunto ao qual o elemento *i* pertence. Duas variáveis, *i* e *j*, estão no mesmo componente se, e somente se, `Find(i) == Find(j)`.

- **Union(i, j):** Conecta os conjuntos que contêm *i* e *j*, mesclando-os em um único subconjunto disjunto.

O objetivo central é minimizar o custo dessas operações. Neste trabalho, vocês devem aplicar essa estrutura no contexto do **Algoritmo de Kruskal** para encontrar a Árvore Geradora Mínima de um grafo, ou em um problema clássico de **Conectividade Dinâmica** em larga escala (o qual deve ser devidamente justificado e explicado no relatório).

---

# 3 Objetivo

O objetivo deste trabalho prático é analisar o impacto de estruturas de dados otimizadas na complexidade assintótica e no desempenho prático de algoritmos. Os alunos deverão implementar o DSU em diferentes níveis de otimização e avaliar como as heurísticas de **Union by Rank** e **Path Compression** alteram a classe de complexidade computacional das operações.

---

# 4 Implementações Requeridas

Devem ser desenvolvidas, no mínimo, três variantes da estrutura:

1. **Naive (Ingênua):** Implementação básica sem heurísticas de balanceamento ou compressão.

2. **Union by Rank:** Implementação utilizando a heurística de união pela altura (ou tamanho) da árvore, garantindo complexidade *O*(log *n*).

3. **Full Tarjan:** Implementação utilizando *Union by Rank* em conjunto com **Path Compression**, atingindo a complexidade amortizada de *O*(α(*n*)), onde α é a inversa da função de Ackermann.

---

# 5 Restrições de Implementação e Tecnologias

- **Linguagens Permitidas:** O código deve ser desenvolvido obrigatoriamente em **C**, **C++** ou **Java**. O uso de bibliotecas prontas para a lógica central do DSU é proibido.

- **Modularização:** O projeto deve ser organizado de forma modular. No artigo, o grupo deve incluir uma seção justificando as **escolhas de modularização** adotadas e como essa organização facilitou a extensibilidade e a realização dos testes comparativos.

- **Ambiente de Testes:** O relatório deve especificar as configurações do ambiente (processador, memória, sistema operacional e versão do compilador/JVM) e as *flags* de otimização utilizadas.

---

# 6 Análise Experimental

Vocês devem realizar experimentos empíricos para medir o tempo de execução e o número de acessos à memória (ou operações de ponteiros) conforme o número de operações *m* e elementos *n* cresce. É obrigatória a criação de gráficos comparativos que demonstrem claramente a transição entre as classes de complexidade *O*(*n*), *O*(log *n*) e *O*(α(*n*)) para as diferentes abordagens.

---

# 7 Integridade Acadêmica e Ética

**Atenção:** Este trabalho possui caráter avaliativo estritamente autoral.

- Qualquer indício de plágio ou cópia (total ou parcial) entre grupos ou de fontes externas resultará na **anulação imediata (nota zero)** do trabalho para todos os envolvidos.

- O uso de ferramentas de **Inteligência Artificial** para a geração do código-fonte ou redação do artigo é terminantemente proibido.

- Casos de fraude acadêmica serão formalmente encaminhados à **Coordenação de Curso** para a aplicação das sanções administrativas e disciplinares cabíveis.

---

# 8 Entrega e Formato do Relatório

O trabalho pode ser realizado individualmente ou em **grupos de até 4 pessoas**. A entrega consiste no código-fonte documentado e em um artigo técnico. O artigo deve, obrigatoriamente, seguir o modelo da **SBC (Sociedade Brasileira de Computação)**.

> **Link para o template SBC:** https://www.overleaf.com/read/whbqnhxmjwjd#176a5a
>
> *[Instruções: Copie o projeto acessando Menu → Actions → Copy Project, renomeie o arquivo e faça as edições necessárias para a elaboração do trabalho.]*

O artigo deve conter:

- **Resumo:** Descrição concisa do experimento e dos principais achados.

- **Introdução:** Contextualização do problema de conjuntos disjuntos e motivação.

- **Metodologia:** Descrição detalhada do método de implementação, dos cenários de teste e do ambiente de execução (hardware/software).

- **Fundamentação Teórica:** Explicação formal detalhando como a estrutura de Tarjan atinge a complexidade *O*(α(*n*)), e a interpretação prática de uma função de crescimento tão lento.

- **Discussão de Resultados:** Gráficos de desempenho e análise crítica apontando em quais cenários cada versão se sobressai ou degrada.

- **Conclusão:** Reflexão sobre a importância e o impacto da escolha adequada de estruturas de dados no projeto de algoritmos reais.

---

# 9 Critérios de Avaliação

- Correção e eficiência das três implementações exigidas.

- Qualidade da análise experimental e clareza visual dos gráficos.

- Profundidade da discussão teórica sobre a complexidade amortizada.

- Rigor acadêmico, clareza textual e formatação estrita conforme o modelo SBC.