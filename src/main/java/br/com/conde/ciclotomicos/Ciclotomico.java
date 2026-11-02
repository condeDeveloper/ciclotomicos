package br.com.conde.ciclotomicos;

import java.util.HashMap;
import java.util.Map;

/**
 * O polinomio ciclotomico pelo caminho da definicao recursiva:
 *
 * <pre>x^n - 1 = produto de Phi_d(x) para todo d que divide n</pre>
 *
 * Isolando o termo d = n, sobra
 *
 * <pre>Phi_n(x) = (x^n - 1) dividido pelo produto dos Phi_d com d menor que n</pre>
 *
 * <p>A divisao e exata por construcao. Se um dia deixar de ser, o
 * {@link Polinomio#dividido} levanta excecao em vez de devolver lixo.
 */
public final class Ciclotomico {

    private final Map<Integer, Polinomio> memoria = new HashMap<>();

    /** Phi_n calculado pela recursao, guardando o que ja saiu. */
    public Polinomio de(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n tem que ser positivo: " + n);
        }
        Polinomio pronto = memoria.get(n);
        if (pronto != null) {
            return pronto;
        }
        Polinomio produto = Polinomio.UM;
        for (int d : Aritmetica.divisores(n)) {
            if (d < n) {
                produto = produto.vezes(de(d));
            }
        }
        Polinomio phi = Polinomio.xElevadoMenosUm(n).dividido(produto);
        memoria.put(n, phi);
        return phi;
    }

    /** Quantos polinomios ja estao na memoria. */
    public int guardados() {
        return memoria.size();
    }

    /**
     * O produto de Phi_d sobre TODOS os divisores de n. Pela identidade, tem
     * que dar exatamente {@code x^n - 1}: e o fechamento que prova a conta.
     */
    public Polinomio produtoSobreDivisores(int n) {
        Polinomio produto = Polinomio.UM;
        for (int d : Aritmetica.divisores(n)) {
            produto = produto.vezes(de(d));
        }
        return produto;
    }
}
