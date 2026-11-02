package br.com.conde.ciclotomicos;

/**
 * O juiz: calcula o mesmo polinomio por um caminho que nao conhece a recursao.
 *
 * <p>A inversao de Mobius aplicada a identidade do produto da
 *
 * <pre>Phi_n(x) = produto de (x^(n/d) - 1) elevado a mu(d), para d dividindo n</pre>
 *
 * <p>Como mu(d) so vale -1, 0 ou 1, isso vira uma fracao de dois produtos de
 * binomios: os de expoente +1 em cima, os de expoente -1 embaixo, e os de
 * mu zero nem entram. Nenhuma chamada recursiva, nenhuma memoria compartilhada
 * com o {@link Ciclotomico}. Se os dois concordam em todo n varrido, os dois
 * estao certos; se discordam em um, um dos dois esta errado, e e ai que mora
 * a noticia.
 */
public final class ViaMobius {

    private ViaMobius() {
    }

    public static Polinomio de(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n tem que ser positivo: " + n);
        }
        Polinomio cima = Polinomio.UM;
        Polinomio baixo = Polinomio.UM;
        for (int d : Aritmetica.divisores(n)) {
            int mu = Aritmetica.mobius(d);
            if (mu == 1) {
                cima = cima.vezes(Polinomio.xElevadoMenosUm(n / d));
            } else if (mu == -1) {
                baixo = baixo.vezes(Polinomio.xElevadoMenosUm(n / d));
            }
        }
        // O divisor aqui NAO e monico, entao a divisao exata do Polinomio nao
        // serve direto: inverte-se a ordem dos coeficientes dos dois lados,
        // divide, e desinverte no fim. Um polinomio com termo constante nao
        // nulo vira monico ao ser espelhado -- a menos do SINAL: cada fator
        // (x^k - 1) contribui com -1 no termo constante, entao um numero impar
        // de fatores embaixo deixa o espelhado com coeficiente lider -1. Nesse
        // caso os dois lados trocam de sinal, o que nao mexe no quociente.
        Polinomio cimaEspelhado = espelha(cima);
        Polinomio baixoEspelhado = espelha(baixo);
        if (baixoEspelhado.coeficiente(baixoEspelhado.grau()) == -1L) {
            Polinomio menosUm = Polinomio.de(-1);
            cimaEspelhado = cimaEspelhado.vezes(menosUm);
            baixoEspelhado = baixoEspelhado.vezes(menosUm);
        }
        return espelha(cimaEspelhado.dividido(baixoEspelhado));
    }

    /** Troca o coeficiente de x^k pelo de x^(grau - k). */
    static Polinomio espelha(Polinomio p) {
        long[] v = p.coeficientes();
        long[] r = new long[v.length];
        for (int i = 0; i < v.length; i++) {
            r[i] = v[v.length - 1 - i];
        }
        return Polinomio.de(r);
    }
}
