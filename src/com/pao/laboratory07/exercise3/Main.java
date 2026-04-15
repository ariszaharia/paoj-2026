package com.pao.laboratory07.exercise3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            int n = scanner.nextInt();
            List<Comanda> comenzi = citesteComenzi(scanner, n);

            for (Comanda comanda : comenzi) {
                System.out.println(comanda.descriere());
            }
            System.out.println();

            while (scanner.hasNext()) {
                String command = scanner.next().toUpperCase(Locale.ROOT);
                if ("QUIT".equals(command)) {
                    return;
                }

                switch (command) {
                    case "STATS":
                        afiseazaStats(comenzi);
                        break;
                    case "FILTER":
                        double threshold = scanner.nextDouble();
                        afiseazaFilter(comenzi, threshold);
                        break;
                    case "SORT":
                        afiseazaSort(comenzi);
                        break;
                    case "SPECIAL":
                        afiseazaSpecial(comenzi);
                        break;
                    default:
                        throw new InputInvalidException("Comanda necunoscuta: " + command);
                }

                System.out.println();
            }
        } catch (InputInvalidException e) {
            System.out.println("Input invalid: " + e.getMessage());
        }
    }

    private static List<Comanda> citesteComenzi(Scanner scanner, int n) {
        List<Comanda> comenzi = new ArrayList<Comanda>();

        for (int i = 0; i < n; i++) {
            String tip = scanner.next().toUpperCase(Locale.ROOT);

            switch (tip) {
                case "STANDARD": {
                    String nume = scanner.next();
                    double pret = scanner.nextDouble();
                    String client = scanner.next();
                    comenzi.add(new ComandaStandard(nume, pret, client));
                    break;
                }
                case "DISCOUNTED": {
                    String nume = scanner.next();
                    double pret = scanner.nextDouble();
                    int discount = scanner.nextInt();
                    String client = scanner.next();
                    comenzi.add(new ComandaRedusa(nume, pret, discount, client));
                    break;
                }
                case "GIFT": {
                    String nume = scanner.next();
                    String client = scanner.next();
                    comenzi.add(new ComandaGratuita(nume, client));
                    break;
                }
                default:
                    throw new InputInvalidException("Tip comanda necunoscut la linia " + (i + 1) + ": " + tip);
            }
        }

        return comenzi;
    }

    private static void afiseazaStats(List<Comanda> comenzi) {
        System.out.println("--- STATS ---");

        Map<String, Double> medii = comenzi.stream().collect(
                Collectors.groupingBy(Comanda::tip, Collectors.averagingDouble(Comanda::pretFinal))
        );

        afiseazaMedieTip(medii, "STANDARD");
        afiseazaMedieTip(medii, "DISCOUNTED");
        afiseazaMedieTip(medii, "GIFT");
    }

    private static void afiseazaMedieTip(Map<String, Double> medii, String tip) {
        double valoare = medii.getOrDefault(tip, 0.0);
        System.out.printf("%s: medie = %.2f lei%n", tip, valoare);
    }

    private static void afiseazaFilter(List<Comanda> comenzi, double threshold) {
        System.out.printf("--- FILTER (>= %.2f) ---%n", threshold);

        List<Comanda> filtrate = comenzi.stream()
                .filter(c -> c.pretFinal() >= threshold)
                .collect(Collectors.toList());

        if (filtrate.isEmpty()) {
            System.out.println("Nicio comanda gasita.");
            return;
        }

        for (Comanda comanda : filtrate) {
            System.out.println(comanda.descriereScurta());
        }
    }

    private static void afiseazaSort(List<Comanda> comenzi) {
        System.out.println("--- SORT (by client, then by pret) ---");

        List<Comanda> sortate = comenzi.stream()
                .sorted(Comparator.comparing(Comanda::client).thenComparing(Comanda::pretFinal))
                .collect(Collectors.toList());

        for (Comanda comanda : sortate) {
            System.out.println(comanda.descriereScurta());
        }
    }

    private static void afiseazaSpecial(List<Comanda> comenzi) {
        System.out.println("--- SPECIAL (discount > 15%) ---");

        List<ComandaRedusa> speciale = comenzi.stream()
                .filter(ComandaRedusa.class::isInstance)
                .map(ComandaRedusa.class::cast)
                .filter(c -> c.discountProcent() > 15)
                .collect(Collectors.toList());

        if (speciale.isEmpty()) {
            System.out.println("Nicio comanda speciala.");
            return;
        }

        for (ComandaRedusa comanda : speciale) {
            System.out.println(comanda.descriereScurta());
        }
    }
}
