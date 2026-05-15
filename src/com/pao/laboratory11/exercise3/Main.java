package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // TODO: Manual demo for bonus requirements.
        System.out.println("TODO: implement laboratory11 exercise3 bonus demo");
        List<Transaction> data = List.of(
                new Transaction(1,  new BigDecimal("120.00"), LocalDate.of(2024, 1,  5),  "RO", "online"),
                new Transaction(2,  new BigDecimal("340.00"), LocalDate.of(2024, 1, 12),  "DE", "branch"),
                new Transaction(3,  new BigDecimal("340.00"), LocalDate.of(2024, 2,  3),  "RO", "online"),
                new Transaction(4,  new BigDecimal("90.00"),  LocalDate.of(2024, 2, 18),  "FR", "atm"),
                new Transaction(5,  new BigDecimal("512.00"), LocalDate.of(2024, 3,  1),  "DE", "online"),
                new Transaction(6,  new BigDecimal("75.00"),  LocalDate.of(2024, 3, 14),  "RO", "branch"),
                new Transaction(7,  new BigDecimal("512.00"), LocalDate.of(2024, 4,  2),  "FR", "online"),
                new Transaction(8,  new BigDecimal("200.00"), LocalDate.of(2024, 4, 20),  "DE", "atm")
        );

        Snapshot snap = data.stream()
                .collect(CustomCollectors.toSnapshot(3));

        System.out.println("=== Top 3 tranzactii ===");
        snap.getTopTransactions().forEach(System.out::println);

        System.out.println("\n=== Tranzactii per tara (desc) ===");
        snap.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> System.out.printf("  %-5s -> %d%n", e.getKey(), e.getValue()));

        System.out.println("\n=== Canale dupa frecventa ===");
        snap.getCountByChannel().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> System.out.printf("  %-10s -> %d tranzactii%n", e.getKey(), e.getValue()));

        System.out.println("\n=== Total ===");
        System.out.println("  " + snap.getTotalAmount() + " EUR");


    }
}
