package br.com.conde.ciclotomicos;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CiclotomicoTest {

    private final Ciclotomico c = new Ciclotomico();

    @Test
    @DisplayName("os primeiros polinomios sao os da tabela de qualquer livro")
    void osPequenos() {
        assertThat(c.de(1)).isEqualTo(Polinomio.de(-1, 1));
        assertThat(c.de(2)).isEqualTo(Polinomio.de(1, 1));
        assertThat(c.de(3)).isEqualTo(Polinomio.de(1, 1, 1));
        assertThat(c.de(4)).isEqualTo(Polinomio.de(1, 0, 1));
        assertThat(c.de(5)).isEqualTo(Polinomio.de(1, 1, 1, 1, 1));
        assertThat(c.de(6)).isEqualTo(Polinomio.de(1, -1, 1));
        assertThat(c.de(8)).isEqualTo(Polinomio.de(1, 0, 0, 0, 1));
        assertThat(c.de(12)).isEqualTo(Polinomio.de(1, 0, -1, 0, 1));
    }

    @Test
    @DisplayName("para p primo, Phi_p e a soma de todas as potencias ate p-1")
    void primoDaSomaCheia() {
        for (int p : new int[] {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31}) {
            Polinomio phi = c.de(p);
            assertThat(phi.grau()).as("grau de Phi_%d", p).isEqualTo(p - 1);
            for (int i = 0; i <= phi.grau(); i++) {
                assertThat(phi.coeficiente(i)).as("Phi_%d coeficiente %d", p, i).isEqualTo(1);
            }
        }
    }

    @Test
    @DisplayName("o produto sobre todos os divisores devolve x^n - 1, em todo n ate 200")
    void identidadeDoProduto() {
        for (int n = 1; n <= 200; n++) {
            assertThat(c.produtoSobreDivisores(n))
                    .as("n = %d", n)
                    .isEqualTo(Polinomio.xElevadoMenosUm(n));
        }
    }

    @Test
    @DisplayName("o grau e sempre a totiente, em todo n ate 200")
    void grauEhTotiente() {
        for (int n = 1; n <= 200; n++) {
            assertThat(c.de(n).grau()).as("n = %d", n).isEqualTo(Aritmetica.totiente(n));
        }
    }

    @Test
    @DisplayName("Phi_2n(x) = Phi_n(-x) para n impar maior que 1")
    void dobrarOIndiceTrocaOSinalDaVariavel() {
        for (int n = 3; n <= 199; n += 2) {
            Polinomio dobrado = c.de(2 * n);
            Polinomio phi = c.de(n);
            assertThat(dobrado.grau()).as("grau em n = %d", n).isEqualTo(phi.grau());
            for (int i = 0; i <= phi.grau(); i++) {
                long esperado = i % 2 == 0 ? phi.coeficiente(i) : -phi.coeficiente(i);
                assertThat(dobrado.coeficiente(i)).as("n = %d, expoente %d", n, i).isEqualTo(esperado);
            }
        }
    }

    @Test
    @DisplayName("o termo constante nunca e zero, senao o polinomio seria divisivel por x")
    void termoConstanteNaoEhZero() {
        for (int n = 1; n <= 300; n++) {
            assertThat(c.de(n).coeficiente(0)).as("n = %d", n).isNotZero();
        }
    }

    @Test
    @DisplayName("a memoria guarda um polinomio por indice pedido, e nao recalcula")
    void memoriaGuarda() {
        Ciclotomico fresco = new Ciclotomico();
        assertThat(fresco.guardados()).isZero();
        fresco.de(12);
        // 12 puxa 1, 2, 3, 4, 6 e ele proprio
        assertThat(fresco.guardados()).isEqualTo(6);
        fresco.de(12);
        assertThat(fresco.guardados()).isEqualTo(6);
    }

    @Test
    @DisplayName("Phi_105 e o primeiro com coeficiente fora de -1, 0 e 1")
    void cento5EhOPrimeiroForaDaRegra() {
        for (int n = 1; n < 105; n++) {
            assertThat(c.de(n).altura()).as("n = %d deveria ter altura 1", n).isEqualTo(1L);
        }
        assertThat(c.de(105).altura()).isEqualTo(2L);
    }
}
