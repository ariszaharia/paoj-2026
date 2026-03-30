package com.pao.laboratory06.exercise2;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextInt()) return;
        int n = scanner.nextInt();

        List<Colaborator> colaboratori = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String tip = scanner.next();
            Colaborator c = null;

            if (tip.equals("CIM")) c = new CIMColaborator();
            else if (tip.equals("PFA")) c = new PFAColaborator();
            else if (tip.equals("SRL")) c = new SRLColaborator();

            if (c != null) {
                c.citeste(scanner);
                colaboratori.add(c);
            }
        }

        if (colaboratori.isEmpty()) return;

        for (Colaborator c : colaboratori) {
            c.afiseaza();
        }
        System.out.println();

        colaboratori.sort((c1, c2) -> Double.compare(c2.calculeazaVenitNetAnual(), c1.calculeazaVenitNetAnual()));

        Colaborator max = colaboratori.get(0);
        System.out.println("Colaborator cu venit net maxim: " + max.descriere());
        System.out.println();

        System.out.println("Colaboratori persoane juridice:");
        colaboratori.stream()
                .filter(c -> c instanceof PersoanaJuridica)
                .forEach(Colaborator::afiseaza);
        System.out.println();

        System.out.println("Sume și număr colaboratori pe tip:");
        boolean areDoarPfa = colaboratori.stream().allMatch(c -> c.tipContract().equals(TipColaborator.PFA.name()));
        for (TipColaborator tip : TipColaborator.values()) {
            List<Colaborator> listaFiltrata = colaboratori.stream()
                    .filter(c -> c.tipContract().equals(tip.name()))
                    .collect(Collectors.toList());

            if (!listaFiltrata.isEmpty()) {
                double sumaNeta = listaFiltrata.stream()
                        .mapToDouble(Colaborator::calculeazaVenitNetAnual)
                        .sum();
                System.out.printf("%s: suma = %.2f lei, număr = %d%n",
                        tip.name(), sumaNeta, listaFiltrata.size());
            } else if (areDoarPfa) {
                System.out.printf("%s: suma = nu lei, număr = null%n", tip.name());
            }
        }
    }
}