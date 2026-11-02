package br.com.conde.ciclotomicos;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Os numeros que estao escritos no README, presos aqui.
 *
 * <p>Se o codigo mudar e uma medida mudar junto, esta bateria quebra ANTES de o
 * texto virar mentira. Nenhum valor daqui foi digitado de cabeca: todos sairam
 * do {@link Medidor} depois de a varredura terminar.
 */
class MedidasTest {

    /** Uma so para todos os testes: a memoria do Ciclotomico vale muito tempo. */
    private static final Varredura V = new Varredura();

    @Test
    @DisplayName("a altura bate recorde em 1, 105 e 385, e em mais nenhum n ate mil")
    void recordesDeAltura() {
        List<Varredura.Medida> recordes = V.recordesDeAltura(Medidor.LIMITE);
        assertThat(recordes).hasSize(3);

        assertThat(recordes.get(0).n()).isEqualTo(1);
        assertThat(recordes.get(0).altura()).isEqualTo(1L);

        assertThat(recordes.get(1).n()).isEqualTo(105);
        assertThat(recordes.get(1).altura()).isEqualTo(2L);
        assertThat(recordes.get(1).grau()).isEqualTo(48);

        assertThat(recordes.get(2).n()).isEqualTo(385);
        assertThat(recordes.get(2).altura()).isEqualTo(3L);
        assertThat(recordes.get(2).grau()).isEqualTo(240);
    }

    @Test
    @DisplayName("942 dos mil primeiros tem so -1, 0 e 1 como coeficiente")
    void quantosTemAlturaUm() {
        assertThat(V.comAlturaUm(Medidor.LIMITE)).isEqualTo(942);
    }

    @Test
    @DisplayName("ate dois primos impares distintos, a altura e 1 sem uma excecao")
    void regraDeMigotti() {
        List<Varredura.PorFatores> linhas = V.porQuantidadeDeFatores(Medidor.LIMITE);
        assertThat(linhas).hasSize(4);

        assertThat(linhas.get(0).primosImpares()).isZero();
        assertThat(linhas.get(0).quantosN()).isEqualTo(10);
        assertThat(linhas.get(0).quantosComAlturaUm()).isEqualTo(10);

        assertThat(linhas.get(1).primosImpares()).isEqualTo(1);
        assertThat(linhas.get(1).quantosN()).isEqualTo(431);
        assertThat(linhas.get(1).quantosComAlturaUm()).isEqualTo(431);

        assertThat(linhas.get(2).primosImpares()).isEqualTo(2);
        assertThat(linhas.get(2).quantosN()).isEqualTo(482);
        assertThat(linhas.get(2).quantosComAlturaUm()).isEqualTo(482);

        // as tres primeiras linhas somam 923 valores de n, e nenhum deles escapa
        assertThat(linhas.get(0).quantosN() + linhas.get(1).quantosN() + linhas.get(2).quantosN())
                .isEqualTo(923);
        assertThat(linhas.get(0).maiorAltura()).isEqualTo(1L);
        assertThat(linhas.get(1).maiorAltura()).isEqualTo(1L);
        assertThat(linhas.get(2).maiorAltura()).isEqualTo(1L);
    }

    @Test
    @DisplayName("tres fatores impares nao bastam: 19 dos 77 continuam com altura 1")
    void tresFatoresNaoBastam() {
        Varredura.PorFatores tres = V.porQuantidadeDeFatores(Medidor.LIMITE).get(3);
        assertThat(tres.primosImpares()).isEqualTo(3);
        assertThat(tres.quantosN()).isEqualTo(77);
        assertThat(tres.quantosComAlturaUm()).isEqualTo(19);
        assertThat(tres.maiorAltura()).isEqualTo(3L);
        assertThat(tres.primeiroComAlturaMaior()).isEqualTo(105);
    }

    @Test
    @DisplayName("as tres conferencias fecham em mil de mil")
    void asTresConferencias() {
        assertThat(V.conferirIdentidade(Medidor.LIMITE)).isEqualTo(1000);
        assertThat(V.conferirContraMobius(Medidor.LIMITE)).isEqualTo(1000);
        assertThat(V.conferirGrau(Medidor.LIMITE)).isEqualTo(1000);
    }

    @Test
    @DisplayName("Phi_105: grau 48, altura 2, e os dois -2 nos expoentes 7 e 41")
    void oRetratoDoCento5() {
        Polinomio p = V.ciclotomico().de(105);
        assertThat(p.grau()).isEqualTo(48);
        assertThat(p.altura()).isEqualTo(2L);
        assertThat(p.coeficientesGrandes()).isEqualTo(2);
        assertThat(p.coeficiente(7)).isEqualTo(-2L);
        assertThat(p.coeficiente(41)).isEqualTo(-2L);
    }

    @Test
    @DisplayName("105 e o menor produto de tres primos impares distintos")
    void porQue105() {
        assertThat(105).isEqualTo(3 * 5 * 7);
        assertThat(Aritmetica.primosDistintos(105)).containsExactly(3, 5, 7);
        // nao existe produto menor de tres primos impares distintos
        for (int n = 1; n < 105; n++) {
            long impares = Aritmetica.primosDistintos(n).stream().filter(p -> p != 2).count();
            assertThat(impares).as("n = %d", n).isLessThan(3L);
        }
    }

    @Test
    @DisplayName("385 = 5 x 7 x 11, e nao tem o 3 que o 105 tem")
    void oRetratoDo385() {
        assertThat(385).isEqualTo(5 * 7 * 11);
        assertThat(Aritmetica.primosDistintos(385)).containsExactly(5, 7, 11);
        assertThat(V.ciclotomico().de(385).altura()).isEqualTo(3L);
    }
}
