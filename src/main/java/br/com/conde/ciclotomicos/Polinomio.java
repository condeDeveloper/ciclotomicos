package br.com.conde.ciclotomicos;

import java.util.Arrays;

/**
 * Polinomio de coeficientes inteiros. O indice do vetor e o expoente, entao
 * {@code de(-1, 0, 1)} e {@code x^2 - 1}.
 *
 * <p>Toda conta usa {@link Math#addExact} e {@link Math#multiplyExact}. Um
 * coeficiente que nao cabe em {@code long} vira excecao na hora, e nao um
 * numero errado que atravessa a varredura inteira em silencio.
 */
public final class Polinomio {

    /** Sempre aparado: o ultimo coeficiente nunca e zero, fora do polinomio zero. */
    private final long[] c;

    private Polinomio(long[] coeficientes) {
        this.c = coeficientes;
    }

    public static final Polinomio ZERO = new Polinomio(new long[] {0});
    public static final Polinomio UM = new Polinomio(new long[] {1});

    public static Polinomio de(long... coeficientes) {
        return new Polinomio(apara(coeficientes.clone()));
    }

    /** O polinomio {@code x^n - 1}. */
    public static Polinomio xElevadoMenosUm(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("expoente tem que ser positivo: " + n);
        }
        long[] v = new long[n + 1];
        v[0] = -1;
        v[n] = 1;
        return new Polinomio(v);
    }

    private static long[] apara(long[] v) {
        int g = v.length - 1;
        while (g > 0 && v[g] == 0) {
            g--;
        }
        return v.length == g + 1 ? v : Arrays.copyOf(v, g + 1);
    }

    public int grau() {
        return c.length - 1;
    }

    public long coeficiente(int expoente) {
        return expoente >= 0 && expoente < c.length ? c[expoente] : 0L;
    }

    public long[] coeficientes() {
        return c.clone();
    }

    public boolean ehZero() {
        return c.length == 1 && c[0] == 0L;
    }

    /** O maior valor absoluto entre os coeficientes. E a "altura" do polinomio. */
    public long altura() {
        long maior = 0L;
        for (long x : c) {
            maior = Math.max(maior, Math.abs(x));
        }
        return maior;
    }

    /** Quantos coeficientes caem fora de -1, 0 e 1. */
    public int coeficientesGrandes() {
        int quantos = 0;
        for (long x : c) {
            if (Math.abs(x) > 1L) {
                quantos++;
            }
        }
        return quantos;
    }

    public Polinomio vezes(Polinomio outro) {
        if (ehZero() || outro.ehZero()) {
            return ZERO;
        }
        long[] r = new long[grau() + outro.grau() + 1];
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 0L) {
                continue;
            }
            for (int j = 0; j < outro.c.length; j++) {
                if (outro.c[j] == 0L) {
                    continue;
                }
                r[i + j] = Math.addExact(r[i + j], Math.multiplyExact(c[i], outro.c[j]));
            }
        }
        return new Polinomio(apara(r));
    }

    public Polinomio menos(Polinomio outro) {
        long[] r = new long[Math.max(c.length, outro.c.length)];
        for (int i = 0; i < r.length; i++) {
            r[i] = Math.subtractExact(coeficiente(i), outro.coeficiente(i));
        }
        return new Polinomio(apara(r));
    }

    /**
     * Divisao exata. O divisor precisa ser monico, que e o caso de tudo que
     * aparece aqui: {@code x^n - 1} e todo polinomio ciclotomico sao monicos.
     *
     * @throws ArithmeticException se a divisao deixar resto, que para o nosso
     *     uso significa que a conta de cima esta errada
     */
    public Polinomio dividido(Polinomio divisor) {
        if (divisor.ehZero()) {
            throw new ArithmeticException("divisao por zero");
        }
        if (divisor.c[divisor.grau()] != 1L) {
            throw new ArithmeticException("divisor nao e monico");
        }
        if (grau() < divisor.grau()) {
            if (ehZero()) {
                return ZERO;
            }
            throw new ArithmeticException("divisao nao exata: grau menor que o do divisor");
        }
        long[] resto = c.clone();
        long[] quociente = new long[grau() - divisor.grau() + 1];
        for (int i = quociente.length - 1; i >= 0; i--) {
            long fator = resto[i + divisor.grau()];
            quociente[i] = fator;
            if (fator == 0L) {
                continue;
            }
            for (int j = 0; j <= divisor.grau(); j++) {
                resto[i + j] = Math.subtractExact(
                        resto[i + j], Math.multiplyExact(fator, divisor.c[j]));
            }
        }
        for (long x : resto) {
            if (x != 0L) {
                throw new ArithmeticException("divisao nao exata: sobrou resto");
            }
        }
        return new Polinomio(apara(quociente));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Polinomio p && Arrays.equals(c, p.c);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(c);
    }

    @Override
    public String toString() {
        if (ehZero()) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = grau(); i >= 0; i--) {
            if (c[i] == 0L) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(c[i] > 0 ? " + " : " - ");
            } else if (c[i] < 0) {
                sb.append('-');
            }
            long abs = Math.abs(c[i]);
            if (abs != 1L || i == 0) {
                sb.append(abs);
            }
            if (i >= 1) {
                sb.append('x');
                if (i > 1) {
                    sb.append('^').append(i);
                }
            }
        }
        return sb.toString();
    }
}
