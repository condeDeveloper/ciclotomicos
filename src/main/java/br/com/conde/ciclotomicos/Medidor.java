package br.com.conde.ciclotomicos;

import java.util.List;
import java.util.Locale;

/**
 * Roda as tabelas que vao para o README. Nenhum numero daquele arquivo foi
 * digitado a mao: todos saem daqui.
 *
 * <pre>
 * mvn -B -ntp exec:java -Dexec.args="recordes"
 * mvn -B -ntp exec:java -Dexec.args="fatores"
 * mvn -B -ntp exec:java -Dexec.args="juiz"
 * mvn -B -ntp exec:java -Dexec.args="exemplos"
 * mvn -B -ntp exec:java -Dexec.args="fundo"     (varredura cara, job proprio)
 * </pre>
 */
public final class Medidor {

    /** O limite das tabelas do dia a dia. */
    public static final int LIMITE = 1000;

    /** O limite da varredura cara, que roda num job separado. */
    public static final int LIMITE_FUNDO = 6000;

    public static void main(String[] args) {
        String comando = args.length > 0 ? args[0] : "tudo";
        switch (comando) {
            case "recordes" -> recordes();
            case "fatores" -> fatores();
            case "juiz" -> juiz();
            case "exemplos" -> exemplos();
            case "fundo" -> fundo();
            case "tudo" -> {
                recordes();
                fatores();
                juiz();
                exemplos();
            }
            default -> {
                System.out.println("comando desconhecido: " + comando);
                System.exit(2);
            }
        }
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println(texto);
        System.out.println("-".repeat(texto.length()));
    }

    private static void recordes() {
        titulo("Onde a altura bate um recorde, ate n = " + LIMITE);
        Varredura v = new Varredura();
        System.out.printf("%-8s %-8s %-8s %s%n", "altura", "n", "grau", "fatores primos impares");
        for (Varredura.Medida m : v.recordesDeAltura(LIMITE)) {
            System.out.printf("%-8d %-8d %-8d %d%n",
                    m.altura(), m.n(), m.grau(), m.primosImparesDistintos());
        }
        int altura1 = v.comAlturaUm(LIMITE);
        System.out.printf("%nn com altura 1 (so -1, 0 e 1): %d de %d (%.2f%%)%n",
                altura1, LIMITE, 100.0 * altura1 / LIMITE);
    }

    private static void fatores() {
        titulo("Altura por quantidade de primos impares distintos, ate n = " + LIMITE);
        Varredura v = new Varredura();
        System.out.printf("%-10s %-10s %-14s %-14s %s%n",
                "primos", "quantos n", "altura 1", "maior altura", "primeiro com altura > 1");
        for (Varredura.PorFatores f : v.porQuantidadeDeFatores(LIMITE)) {
            System.out.printf("%-10d %-10d %-14d %-14d %s%n",
                    f.primosImpares(), f.quantosN(), f.quantosComAlturaUm(), f.maiorAltura(),
                    f.primeiroComAlturaMaior() == 0 ? "nenhum" : String.valueOf(f.primeiroComAlturaMaior()));
        }
    }

    private static void juiz() {
        titulo("As tres conferencias, ate n = " + LIMITE);
        Varredura v = new Varredura();
        System.out.printf("produto de Phi_d sobre os divisores = x^n - 1 : %d de %d%n",
                v.conferirIdentidade(LIMITE), LIMITE);
        System.out.printf("recursao x inversao de Mobius concordam      : %d de %d%n",
                v.conferirContraMobius(LIMITE), LIMITE);
        System.out.printf("grau de Phi_n = totiente de n                : %d de %d%n",
                v.conferirGrau(LIMITE), LIMITE);
    }

    private static void exemplos() {
        titulo("Os polinomios pequenos, e o primeiro que foge da regra");
        Ciclotomico c = new Ciclotomico();
        for (int n : new int[] {1, 2, 3, 4, 5, 6, 8, 12, 15, 105}) {
            Polinomio p = c.de(n);
            String texto = p.toString();
            if (texto.length() > 92) {
                texto = texto.substring(0, 89) + "...";
            }
            System.out.printf("Phi_%-4d grau %-4d altura %-3d %s%n", n, p.grau(), p.altura(), texto);
        }
        Polinomio p105 = c.de(105);
        System.out.printf("%nEm Phi_105 os coeficientes fora de -1, 0 e 1 sao %d, e aparecem nos expoentes:%n",
                p105.coeficientesGrandes());
        StringBuilder onde = new StringBuilder();
        for (int i = 0; i <= p105.grau(); i++) {
            if (Math.abs(p105.coeficiente(i)) > 1L) {
                onde.append(String.format(Locale.ROOT, "x^%d (%d)  ", i, p105.coeficiente(i)));
            }
        }
        System.out.println("  " + onde.toString().trim());
    }

    private static void fundo() {
        titulo("Varredura ate n = " + LIMITE_FUNDO);
        Varredura v = new Varredura();
        long comeco = System.nanoTime();
        List<Varredura.Medida> recordes = v.recordesDeAltura(LIMITE_FUNDO);
        System.out.printf("%-8s %-8s %s%n", "altura", "n", "grau");
        for (Varredura.Medida m : recordes) {
            System.out.printf("%-8d %-8d %d%n", m.altura(), m.n(), m.grau());
        }
        System.out.printf("%nos dois caminhos concordaram em %d de %d%n",
                v.conferirContraMobius(LIMITE_FUNDO), LIMITE_FUNDO);
        System.out.printf("a identidade do produto fechou em %d de %d%n",
                v.conferirIdentidade(LIMITE_FUNDO), LIMITE_FUNDO);
        System.out.printf("tempo: %.1f s%n", (System.nanoTime() - comeco) / 1e9);
    }
}
