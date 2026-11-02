package br.com.conde.ciclotomicos;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * O juiz contra a busca. Se estes testes passam, os dois caminhos chegaram ao
 * mesmo polinomio sem trocar uma linha de codigo.
 */
class ViaMobiusTest {

    private final Ciclotomico c = new Ciclotomico();

    @Test
    @DisplayName("o espelhamento inverte a ordem dos coeficientes e volta ao original")
    void espelharDuasVezesVolta() {
        Polinomio p = Polinomio.de(-1, 2, 0, 3);
        assertThat(ViaMobius.espelha(p)).isEqualTo(Polinomio.de(3, 0, 2, -1));
        assertThat(ViaMobius.espelha(ViaMobius.espelha(p))).isEqualTo(p);
    }

    @Test
    @DisplayName("a inversao de Mobius da os mesmos polinomios pequenos")
    void osPequenos() {
        assertThat(ViaMobius.de(1)).isEqualTo(Polinomio.de(-1, 1));
        assertThat(ViaMobius.de(2)).isEqualTo(Polinomio.de(1, 1));
        assertThat(ViaMobius.de(6)).isEqualTo(Polinomio.de(1, -1, 1));
        assertThat(ViaMobius.de(12)).isEqualTo(Polinomio.de(1, 0, -1, 0, 1));
    }

    @Test
    @DisplayName("os dois caminhos concordam em todo n ate 300")
    void concordamAte300() {
        for (int n = 1; n <= 300; n++) {
            assertThat(ViaMobius.de(n)).as("n = %d", n).isEqualTo(c.de(n));
        }
    }

    @Test
    @DisplayName("concordam tambem no 105, que e onde a regra simples quebra")
    void concordamNoCento5() {
        Polinomio pelaRecursao = c.de(105);
        Polinomio peloMobius = ViaMobius.de(105);
        assertThat(peloMobius).isEqualTo(pelaRecursao);
        assertThat(peloMobius.altura()).isEqualTo(2L);
        assertThat(peloMobius.coeficiente(7)).isEqualTo(-2L);
        assertThat(peloMobius.coeficiente(41)).isEqualTo(-2L);
    }

    @Test
    @DisplayName("o caso em que o denominador tem numero impar de fatores tambem fecha")
    void denominadorComSinalNegativo() {
        // n primo: o denominador e um binomio so, e o espelhado dele tem
        // coeficiente lider -1. Foi o caso que quebrou a primeira versao.
        for (int p : new int[] {2, 3, 5, 7, 11, 13}) {
            assertThat(ViaMobius.de(p)).as("p = %d", p).isEqualTo(c.de(p));
        }
    }
}
