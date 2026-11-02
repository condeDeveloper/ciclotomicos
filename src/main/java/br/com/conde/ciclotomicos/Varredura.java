package br.com.conde.ciclotomicos;

import java.util.ArrayList;
import java.util.List;

/**
 * Varre n de 1 ate um limite, calcula Phi_n pelos dois caminhos e anota o que
 * der para medir. Nada aqui chuta: toda linha de tabela sai de uma varredura
 * que terminou.
 */
public final class Varredura {

    /** O que se sabe sobre um n depois de calcular o polinomio dele. */
    public record Medida(
            int n,
            long altura,
            int grau,
            int totiente,
            int primosImparesDistintos,
            boolean livreDeQuadrado,
            int coeficientesGrandes) {
    }

    private final Ciclotomico ciclotomico = new Ciclotomico();

    public Ciclotomico ciclotomico() {
        return ciclotomico;
    }

    public Medida medir(int n) {
        Polinomio phi = ciclotomico.de(n);
        int imparesDistintos = 0;
        for (int p : Aritmetica.primosDistintos(n)) {
            if (p != 2) {
                imparesDistintos++;
            }
        }
        return new Medida(
                n,
                phi.altura(),
                phi.grau(),
                Aritmetica.totiente(n),
                imparesDistintos,
                Aritmetica.livreDeQuadrado(n),
                phi.coeficientesGrandes());
    }

    public List<Medida> ate(int limite) {
        List<Medida> medidas = new ArrayList<>(limite);
        for (int n = 1; n <= limite; n++) {
            medidas.add(medir(n));
        }
        return medidas;
    }

    /**
     * Os n em que a altura bate um recorde: o primeiro com altura 1, o primeiro
     * com altura 2, e assim por diante. E a tabela que responde "onde o
     * coeficiente deixa de ser 0 ou mais ou menos 1".
     */
    public List<Medida> recordesDeAltura(int limite) {
        List<Medida> recordes = new ArrayList<>();
        long melhor = 0L;
        for (int n = 1; n <= limite; n++) {
            Medida m = medir(n);
            if (m.altura() > melhor) {
                melhor = m.altura();
                recordes.add(m);
            }
        }
        return recordes;
    }

    /**
     * Confere, para cada n ate o limite, que o produto de Phi_d sobre todos os
     * divisores devolve exatamente x^n - 1. E o fechamento da identidade que
     * define tudo: se falhar em um n, a recursao inteira esta errada.
     *
     * @return quantos n fecharam
     */
    public int conferirIdentidade(int limite) {
        int certos = 0;
        for (int n = 1; n <= limite; n++) {
            if (ciclotomico.produtoSobreDivisores(n).equals(Polinomio.xElevadoMenosUm(n))) {
                certos++;
            } else {
                throw new IllegalStateException("a identidade do produto falhou em n = " + n);
            }
        }
        return certos;
    }

    /**
     * Confere a recursao contra a inversao de Mobius, que nao compartilha uma
     * linha de codigo com ela.
     *
     * @return quantos n os dois caminhos devolveram o mesmo polinomio
     */
    public int conferirContraMobius(int limite) {
        int iguais = 0;
        for (int n = 1; n <= limite; n++) {
            if (ciclotomico.de(n).equals(ViaMobius.de(n))) {
                iguais++;
            } else {
                throw new IllegalStateException("os dois caminhos discordam em n = " + n);
            }
        }
        return iguais;
    }

    /** Confere que o grau de Phi_n e sempre a totiente de n. */
    public int conferirGrau(int limite) {
        int certos = 0;
        for (int n = 1; n <= limite; n++) {
            Medida m = medir(n);
            if (m.grau() == m.totiente()) {
                certos++;
            } else {
                throw new IllegalStateException("o grau nao bateu com a totiente em n = " + m.n());
            }
        }
        return certos;
    }

    /**
     * Para cada quantidade de primos impares distintos, a maior altura
     * encontrada e o menor n que atinge ela. E o que separa a regra de Migotti
     * do caso geral.
     */
    public record PorFatores(
            int primosImpares,
            int quantosN,
            int quantosComAlturaUm,
            long maiorAltura,
            int primeiroComAlturaMaior) {
    }

    public List<PorFatores> porQuantidadeDeFatores(int limite) {
        int teto = 6;
        int[] quantos = new int[teto + 1];
        int[] planos = new int[teto + 1];
        long[] maior = new long[teto + 1];
        int[] primeiro = new int[teto + 1];
        for (int n = 1; n <= limite; n++) {
            Medida m = medir(n);
            int k = Math.min(m.primosImparesDistintos(), teto);
            quantos[k]++;
            if (m.altura() == 1L) {
                planos[k]++;
            }
            if (m.altura() > maior[k]) {
                maior[k] = m.altura();
            }
            if (m.altura() > 1L && primeiro[k] == 0) {
                primeiro[k] = m.n();
            }
        }
        List<PorFatores> linhas = new ArrayList<>();
        for (int k = 0; k <= teto; k++) {
            if (quantos[k] > 0) {
                linhas.add(new PorFatores(k, quantos[k], planos[k], maior[k], primeiro[k]));
            }
        }
        return linhas;
    }

    /** Quantos n ate o limite tem altura 1, ou seja, so coeficientes -1, 0 e 1. */
    public int comAlturaUm(int limite) {
        int quantos = 0;
        for (int n = 1; n <= limite; n++) {
            if (medir(n).altura() == 1L) {
                quantos++;
            }
        }
        return quantos;
    }
}
