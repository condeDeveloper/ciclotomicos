# ciclotomicos

Fatore `x^n - 1` em pedaços que não dão para quebrar mais, com coeficientes inteiros.
Para `n = 3` dá `(x - 1)(x² + x + 1)`. Para `n = 12` dá cinco pedaços. Os pedaços têm
nome: são os **polinômios ciclotômicos**, um para cada divisor de `n`.

Olhe os coeficientes deles. `1`. `-1`. `0`. `1`. `-1`. Sempre. Calcule cem deles e a
impressão é de lei da natureza: coeficiente de polinômio ciclotômico é `-1`, `0` ou `1`.

Não é lei nenhuma. Em **n = 105** aparece um `-2`, e a partir dali o tamanho dos
coeficientes não tem teto. Este repositório varre os polinômios um por um, calcula cada
um por **dois caminhos que não se falam**, e mede exatamente onde e por que a impressão
quebra.

```bash
mvn -B -ntp verify                              # 42 testes
mvn -B -ntp exec:java -Dexec.args="recordes"    # onde a altura bate recorde
mvn -B -ntp exec:java -Dexec.args="fatores"     # altura por número de primos ímpares
mvn -B -ntp exec:java -Dexec.args="juiz"        # as três conferências
mvn -B -ntp exec:java -Dexec.args="exemplos"    # os polinômios pequenos e o 105
mvn -B -ntp exec:java -Dexec.args="fundo"       # varredura até 6.000 (job próprio)
```

Java 21, sem dependência fora de JUnit e AssertJ.

## Os dois caminhos

**Pela recursão.** A identidade que define tudo é `x^n - 1 = produto de Φ_d(x)` para todo
`d` que divide `n`. Isolando o termo `d = n`, sobra `Φ_n(x) = (x^n - 1)` dividido pelo
produto dos anteriores. É divisão exata de polinômio, e o código levanta exceção se
sobrar resto.

**Pela inversão de Möbius.** A mesma identidade, invertida, dá
`Φ_n(x) = produto de (x^(n/d) - 1) elevado a μ(d)`. Como `μ` só vale `-1`, `0` ou `1`,
isso é uma fração de dois produtos de binômios. Nenhuma recursão, nenhuma memória, nenhuma
linha em comum com o primeiro caminho.

E um terceiro teste, que não calcula nada novo: multiplicar `Φ_d` sobre **todos** os
divisores de `n` tem que devolver `x^n - 1` de volta, coeficiente por coeficiente.

```
produto de Φ_d sobre os divisores = x^n - 1 : 1000 de 1000
recursão x inversão de Möbius concordam      : 1000 de 1000
grau de Φ_n = totiente de n                  : 1000 de 1000
```

## Onde a altura bate recorde

A **altura** é o maior valor absoluto entre os coeficientes. Altura 1 quer dizer que só
aparecem `-1`, `0` e `1`.

```
altura   n       grau   primos ímpares distintos
1        1       1      0
2        105     48     3
3        385     240    3
4        1365    576    4
5        1785    768    4
6        2805    1280   4
7        3135    1440   4
```

As três primeiras linhas saem da varredura até mil, que roda em todo CI. As quatro de
baixo saem da varredura até 6.000, que leva 28 segundos e roda num job separado.

Entre `n = 1` e `n = 104` não existe uma única exceção: **942 dos mil primeiros** têm
altura 1, ou seja, 94,20%.

## Por que exatamente 105

```
primos ímpares   quantos n   altura 1   maior altura   primeiro com altura > 1
0                10          10         1              nenhum
1                431         431        1              nenhum
2                482         482        1              nenhum
3                77          19         3              105
```

A tabela conta a história inteira. Enquanto `n` tem **no máximo dois** primos ímpares
distintos, a altura é 1 — e não é "quase sempre", são **923 valores de n e zero exceções**.
É o teorema de Migotti, de 1883, e a varredura concorda com ele caso a caso.

E `105 = 3 × 5 × 7` é o **menor** número com três primos ímpares distintos. O `-2` não
aparece em 105 por acaso nem por tamanho: aparece porque 105 é o primeiro lugar onde ele
*pode* aparecer. Um teste varre todo `n < 105` e confirma que nenhum deles tem três
fatores ímpares.

Em `Φ_105`, de grau 48, os coeficientes fora da regra são exatamente dois, e são iguais:

```
x^7   coeficiente -2
x^41  coeficiente -2
```

## O que as medidas me corrigiram

**Achei que três fatores ímpares bastassem.** A tabela acima me desmentiu na mesma rodada:
dos 77 valores de `n` até mil com três primos ímpares distintos, **19 continuam com altura
1**. Ter três fatores é necessário e não é suficiente. Eu ia escrever "a partir de três
fatores os coeficientes crescem", que é falso para um quarto dos casos.

**Esperei o segundo recorde logo depois do primeiro.** Como 105 chega cedo, imaginei
altura 3 na casa das centenas baixas. Está em **385**, quase quatro vezes depois — e 385 é
`5 × 7 × 11`, que nem tem o fator 3 do 105.

**O juiz quase não compilou, e o erro era meu.** Na inversão de Möbius eu divido dois
produtos de binômios, e a divisão exata do `Polinomio` exige divisor mônico. Espelhei os
coeficientes dos dois lados para tornar o divisor mônico — e esqueci do sinal: cada fator
`(x^k - 1)` contribui com `-1` no termo constante, então um número **ímpar** de fatores
embaixo deixa o espelhado com coeficiente líder `-1`, e não `+1`. O caso mais simples que
quebra é `n` primo, onde o denominador é um binômio só. Hoje há um teste chamado
`denominadorComSinalNegativo` que existe só por causa disso.

**Deixei o `long` reclamar em vez de confiar.** Toda multiplicação e soma de coeficiente
passa por `Math.multiplyExact` e `Math.addExact`. Num problema cujo assunto *é* o tamanho
dos coeficientes, um estouro silencioso entregaria uma altura menor que a verdadeira e a
varredura inteira passaria verde.

## Quatro achados

1. O primeiro coeficiente fora de `-1`, `0` e `1` aparece em **n = 105**, são **dois**, e
   os dois valem `-2`, nos expoentes `x^7` e `x^41` de um polinômio de grau 48.
2. **923 valores de n e nenhuma exceção:** enquanto `n` tem no máximo dois primos ímpares
   distintos, a altura é 1. A exceção precisa de três fatores, e 105 é o menor número que
   tem três.
3. Três fatores ímpares **não bastam**: `19 dos 77` valores de `n` até mil com três primos
   ímpares distintos continuam com altura 1.
4. Os dois caminhos independentes — recursão e inversão de Möbius — concordaram em
   **6.000 de 6.000** polinômios, e o produto sobre os divisores devolveu `x^n - 1` nos
   6.000 casos.
