package com.pao.laboratory10.exercise3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

public class Main {
    public static void main(String[] args) {
        ArrayList<Tranzactie> tranzactii = new ArrayList<>();
        
        tranzactii.add(new Tranzactie(1, 1500.00, "2024-01-15", TipTranzactie.CREDIT));
        tranzactii.add(new Tranzactie(2, 2000.00, "2024-02-10", TipTranzactie.CREDIT));
        tranzactii.add(new Tranzactie(3, 500.50, "2024-03-05", TipTranzactie.CREDIT));
        tranzactii.add(new Tranzactie(4, 3200.00, "2024-02-20", TipTranzactie.CREDIT));
        tranzactii.add(new Tranzactie(5, 750.25, "2024-01-28", TipTranzactie.CREDIT));
        
        tranzactii.add(new Tranzactie(6, 400.00, "2024-01-20", TipTranzactie.DEBIT));
        tranzactii.add(new Tranzactie(7, 850.75, "2024-02-15", TipTranzactie.DEBIT));
        tranzactii.add(new Tranzactie(8, 1200.00, "2024-03-10", TipTranzactie.DEBIT));
        tranzactii.add(new Tranzactie(9, 600.50, "2024-02-28", TipTranzactie.DEBIT));
        tranzactii.add(new Tranzactie(10, 300.00, "2024-03-22", TipTranzactie.DEBIT));

        // OPERATIA 1: filter(tip == CREDIT)
        System.out.println("=== OPERATIA 1: Filtrare tranzactii CREDIT ===");
        List<Tranzactie> creditList = tranzactii.stream()
            .filter(t -> t.getTip() == TipTranzactie.CREDIT)
            .collect(Collectors.toList());
        creditList.forEach(System.out::println);
        
        // OPERATIA 2: mapToDouble(suma).sum()
        System.out.println("\n=== OPERATIA 2: Total procesat (toti banii) ===");
        double totalProcesat = tranzactii.stream()
            .mapToDouble(Tranzactie::getSuma)
            .sum();
        System.out.printf("Total procesat: %.2f RON%n", totalProcesat);
        
        // OPERATIA 3: Collectors.groupingBy(luna, summingDouble(suma)) cu TreeMap
        System.out.println("\n=== OPERATIA 3: Total pe luni ===");
        Map<String, Double> totalPerLuna = tranzactii.stream()
            .collect(Collectors.groupingBy(
                t -> t.getData().substring(0, 7),
                TreeMap::new,
                Collectors.summingDouble(Tranzactie::getSuma)
            ));
        totalPerLuna.forEach((luna, total) -> 
            System.out.printf("%s: %.2f RON%n", luna, total)
        );
        
        // OPERATIA 4: sorted(comparingDouble.reversed()).limit(3)
        System.out.println("\n=== OPERATIA 4: Top 3 tranzactii (dupa suma) ===");
        tranzactii.stream()
            .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
            .limit(3)
            .forEach(System.out::println);
        
        // OPERATIA 5: map(id).distinct().collect(toList())
        System.out.println("\n=== OPERATIA 5: ID-uri unice ===");
        List<Integer> idUnice = tranzactii.stream()
            .map(Tranzactie::getId)
            .distinct()
            .collect(Collectors.toList());
        System.out.println("ID-uri unice: " + idUnice);
        
        // OPERATIA 6: mapToDouble(suma).average()
        System.out.println("\n=== OPERATIA 6: Suma medie ===");
        OptionalDouble medie = tranzactii.stream()
            .mapToDouble(Tranzactie::getSuma)
            .average();
        if (medie.isPresent()) {
            System.out.printf("Suma medie: %.2f RON%n", medie.getAsDouble());
        }
        
        // OPERATIA 7: Extrase de cont lunare cu Collectors.groupingBy
        System.out.println("\n=== OPERATIA 7: Extrase de cont lunare ===");
        Map<String, List<Tranzactie>> extrasePerLuna = tranzactii.stream()
            .collect(Collectors.groupingBy(
                t -> t.getData().substring(0, 7),
                TreeMap::new,
                Collectors.toList()
            ));
        
        extrasePerLuna.forEach((luna, tranzactiiluna) -> {
            double totalLuna = tranzactiiluna.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();
            System.out.printf("EXTRASUL DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                luna, tranzactiiluna.size(), totalLuna);
            tranzactiiluna.forEach(t -> System.out.println("  " + t));
        });
    }
}
