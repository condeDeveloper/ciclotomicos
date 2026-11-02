package br.com.conde.ciclotomicos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PolinomioTest {

    @Test
    @DisplayName("o indice do vetor e o expoente")
    void indiceEhExpoente() {
        Polinomio p = Polinomio.de(-1, 0, 1);
        assertThat(p.grau()).isEqualTo(2);
        assertThat(p.coeficiente(0)).isEqualTo(-1);
        assertThat(p.coeficiente(1)).isZero();
        assertThat(p.coeficiente(2)).isEqualTo(1);
        assertThat(p.toString()).isEqualTo("x^2 - 1");
    }

    @Test
    @DisplayName("coeficiente acima do grau vale zero, sem estourar o vetor")
    void foraDoGrauEhZero() {
        Polinomio p = Polinomio.de(1, 1);
        assertThat(p.coeficiente(9)).isZero();
        assertThat(p.coeficiente(-3)).isZero();
    }

    @Test
    @DisplayName("zeros no fim somem: o grau e o verdadeiro")
    void aparaOsZeros() {
        assertThat(Polinomio.de(1, 2, 0, 0).grau()).isEqualTo(1);
        assertThat(Polinomio.de(0, 0, 0).ehZero()).isTrue();
        assertThat(Polinomio.de(0, 0, 0).grau()).isZero();
    }

    @Test
    @DisplayName("x^n - 1 tem exatamente dois termos")
    void binomio() {
        Polinomio p = Polinomio.xElevadoMenosUm(5);
        assertThat(p.grau()).isEqualTo(5);
        assertThat(p.coeficiente(5)).isEqualTo(1);
        assertThat(p.coeficiente(0)).isEqualTo(-1);
        for (int i = 1; i < 5; i++) {
            assertThat(p.coeficiente(i)).isZero();
        }
    }

    @Test
    @DisplayName("(x - 1)(x + 1) = x^2 - 1")
    void multiplicacao() {
        Polinomio produto = Polinomio.de(-1, 1).vezes(Polinomio.de(1, 1));
        assertThat(produto).isEqualTo(Polinomio.de(-1, 0, 1));
    }

    @Test
    @DisplayName("multiplicar por zero da zero, e nao um vetor de zeros")
    void multiplicarPorZero() {
        assertThat(Polinomio.de(3, 2, 1).vezes(Polinomio.ZERO).ehZero()).isTrue();
    }

    @Test
    @DisplayName("a divisao exata desfaz a multiplicacao")
    void divisaoDesfazMultiplicacao() {
        Polinomio a = Polinomio.de(-1, 0, 0, 1);
        Polinomio b = Polinomio.de(-1, 1);
        assertThat(a.vezes(b).dividido(b)).isEqualTo(a);
    }

    @Test
    @DisplayName("x^3 - 1 dividido por x - 1 da x^2 + x + 1")
    void divisaoConhecida() {
        Polinomio q = Polinomio.xElevadoMenosUm(3).dividido(Polinomio.de(-1, 1));
        assertThat(q).isEqualTo(Polinomio.de(1, 1, 1));
    }

    @Test
    @DisplayName("divisao que deixa resto levanta excecao, em vez de devolver lixo")
    void divisaoComRestoReclama() {
        assertThatThrownBy(() -> Polinomio.de(1, 1, 1).dividido(Polinomio.de(1, 0, 1)))
                .isInstanceOf(ArithmeticException.class)
                .hasMessageContaining("nao exata");
    }

    @Test
    @DisplayName("divisor nao monico e recusado na porta")
    void divisorNaoMonico() {
        assertThatThrownBy(() -> Polinomio.de(1, 1).dividido(Polinomio.de(1, 2)))
                .isInstanceOf(ArithmeticException.class)
                .hasMessageContaining("monico");
    }

    @Test
    @DisplayName("coeficiente que nao cabe em long vira excecao, e nao numero errado")
    void estouroReclama() {
        Polinomio grande = Polinomio.de(Long.MAX_VALUE / 2, Long.MAX_VALUE / 2);
        assertThatThrownBy(() -> grande.vezes(grande))
                .isInstanceOf(ArithmeticException.class);
    }

    @Test
    @DisplayName("altura e o maior valor absoluto, nao o maior coeficiente")
    void alturaUsaValorAbsoluto() {
        assertThat(Polinomio.de(1, -7, 2).altura()).isEqualTo(7);
        assertThat(Polinomio.de(1, -7, 2).coeficientesGrandes()).isEqualTo(2);
    }

    @Test
    @DisplayName("subtracao bate com a definicao")
    void subtracao() {
        assertThat(Polinomio.de(1, 2, 3).menos(Polinomio.de(1, 2, 3)).ehZero()).isTrue();
        assertThat(Polinomio.de(0, 0, 1).menos(Polinomio.de(1))).isEqualTo(Polinomio.de(-1, 0, 1));
    }
}
