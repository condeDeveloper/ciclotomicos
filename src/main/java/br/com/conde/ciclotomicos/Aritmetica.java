package br.com.conde.ciclotomicos;

import java.util.ArrayList;
import java.util.List;

/** Divisores, fatoracao e Mobius. Sem nada de polinomio aqui dentro. */
public final class Aritmetica {

    private Aritmetica() {
    }

    /** Todos os divisores de n, em ordem crescente. */
    public static List<Integer> divisores(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n tem que ser positivo: " + n);
        }
        List<Integer> pequenos = new ArrayList<>();
        List<Integer> grandes = new ArrayList<>();
        for (int d = 1; (long) d * d <= n; d++) {
            if (n % d == 0) {
                pequenos.add(d);
                if (d != n / d) {
                    grandes.add(n / d);
                }
            }
        }
        for (int i = grandes.size() - 1; i >= 0; i--) {
            pequenos.add(grandes.get(i));
        }
        return pequenos;
    }

    /** Os primos distintos que dividem n, em ordem crescente. */
    public static List<Integer> primosDistintos(int n) {
        List<Integer> primos = new ArrayList<>();
        int resto = n;
        for (int p = 2; (long) p * p <= resto; p++) {
            if (resto % p == 0) {
                primos.add(p);
                while (resto % p == 0) {
                    resto /= p;
                }
            }
        }
        if (resto > 1) {
            primos.add(resto);
        }
        return primos;
    }

    /** True se nenhum primo aparece ao quadrado. */
    public static boolean livreDeQuadrado(int n) {
        int resto = n;
        for (int p = 2; (long) p * p <= resto; p++) {
            if (resto % (p * p) == 0) {
                return false;
            }
            while (resto % p == 0) {
                resto /= p;
            }
        }
        return true;
    }

    /** A funcao de Mobius: 0 se nao e livre de quadrado, senao (-1)^(numero de primos). */
    public static int mobius(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n tem que ser positivo: " + n);
        }
        if (n == 1) {
            return 1;
        }
        int resto = n;
        int primos = 0;
        for (int p = 2; (long) p * p <= resto; p++) {
            if (resto % p == 0) {
                resto /= p;
                if (resto % p == 0) {
                    return 0;
                }
                primos++;
            }
        }
        if (resto > 1) {
            primos++;
        }
        return primos % 2 == 0 ? 1 : -1;
    }

    /** A funcao totiente de Euler, contada pela definicao mais simples que existe. */
    public static int totiente(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n tem que ser positivo: " + n);
        }
        int quantos = 0;
        for (int k = 1; k <= n; k++) {
            if (mdc(k, n) == 1) {
                quantos++;
            }
        }
        return quantos;
    }

    public static int mdc(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return Math.abs(a);
    }

    /** A parte impar de n: divide por dois ate sobrar impar. */
    public static int parteImpar(int n) {
        int r = n;
        while (r % 2 == 0) {
            r /= 2;
        }
        return r;
    }
}
