package com.pao.laboratory10.exercise3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

        Map<TipTranzactie, ArrayList<Tranzactie>> tranzactiitip = tranzactii.stream().collect(Collectors.groupingBy(Tranzactie::getTip))

    }
}
