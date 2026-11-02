package br.com.conde.ciclotomicos;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AritmeticaTest {

    @Test
    @DisplayName("os divisores saem em ordem e sem repetir o do quadrado perfeito")
    void divisores() {
        assertThat(Aritmetica.divisores(1)).containsExactly(1);
        assertThat(Aritmetica.divisores(12)).containsExactly(1, 2, 3, 4, 6, 12);
        assertThat(Aritmetica.divisores(36)).containsExactly(1, 2, 3, 4, 6, 9, 12, 18, 36);
        assertThat(Aritmetica.divisores(37)).containsExactly(1, 37);
    }

    @Test
    @DisplayName("primos distintos ignoram a multiplicidade")
    void primosDistintos() {
        assertThat(Aritmetica.primosDistintos(8)).containsExactly(2);
        assertThat(Aritmetica.primosDistintos(105)).containsExactly(3, 5, 7);
        assertThat(Aritmetica.primosDistintos(360)).containsExactly(2, 3, 5);
    }

    @Test
    @DisplayName("Mobius vale zero exatamente quando um primo aparece ao quadrado")
    void mobius() {
        assertThat(Aritmetica.mobius(1)).isEqualTo(1);
        assertThat(Aritmetica.mobius(2)).isEqualTo(-1);
        assertThat(Aritmetica.mobius(6)).isEqualTo(1);
        assertThat(Aritmetica.mobius(30)).isEqualTo(-1);
        assertThat(Aritmetica.mobius(4)).isZero();
        assertThat(Aritmetica.mobius(12)).isZero();
    }

    @Test
    @DisplayName("Mobius concorda com livreDeQuadrado em todo n ate mil")
    void mobiusConcordaComLivreDeQuadrado() {
        for (int n = 1; n <= 1000; n++) {
            assertThat(Aritmetica.mobius(n) == 0)
                    .as("n = %d", n)
                    .isEqualTo(!Aritmetica.livreDeQuadrado(n));
        }
    }

    @Test
    @DisplayName("a soma de Mobius sobre os divisores vale 1 so em n = 1")
    void somaDeMobiusSobreDivisores() {
        for (int n = 1; n <= 300; n++) {
            int soma = 0;
            for (int d : Aritmetica.divisores(n)) {
                soma += Aritmetica.mobius(d);
            }
            assertThat(soma).as("n = %d", n).isEqualTo(n == 1 ? 1 : 0);
        }
    }

    @Test
    @DisplayName("a soma da totiente sobre os divisores devolve o proprio n")
    void somaDaTotienteSobreDivisores() {
        for (int n = 1; n <= 300; n++) {
            int soma = 0;
            for (int d : Aritmetica.divisores(n)) {
                soma += Aritmetica.totiente(d);
            }
            assertThat(soma).as("n = %d", n).isEqualTo(n);
        }
    }

    @Test
    @DisplayName("totiente nos casos de cabeceira")
    void totiente() {
        assertThat(Aritmetica.totiente(1)).isEqualTo(1);
        assertThat(Aritmetica.totiente(7)).isEqualTo(6);
        assertThat(Aritmetica.totiente(105)).isEqualTo(48);
        assertThat(Aritmetica.totiente(385)).isEqualTo(240);
    }

    @Test
    @DisplayName("a parte impar tira todo fator dois e so ele")
    void parteImpar() {
        assertThat(Aritmetica.parteImpar(105)).isEqualTo(105);
        assertThat(Aritmetica.parteImpar(80)).isEqualTo(5);
        assertThat(Aritmetica.parteImpar(1024)).isEqualTo(1);
    }
}
