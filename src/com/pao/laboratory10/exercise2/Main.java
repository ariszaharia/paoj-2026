package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

import static java.util.Comparator.comparingDouble;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ArrayList<Tranzactie> tranzactii = new ArrayList<>();
        int n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            String tip = scanner.next();
            tranzactii.add(new Tranzactie(id, suma, data, TipTranzactie.valueOf(tip)));
        }
        while (scanner.hasNext()) {
            String comanda = scanner.next();
            switch(comanda){
                case "UNIQUE_IDS":
                    LinkedHashSet<Integer> uniqueIds = new LinkedHashSet<>();
                    for (Tranzactie t : tranzactii) {
                        uniqueIds.add(t.getId());
                    }
                    System.out.println("IDs unice (" + uniqueIds.size() + "): " + uniqueIds);
                    break;
                case "MONTHLY_REPORT":
                    TreeMap<String, double[]> monthlyReport = new TreeMap<>();
                    for(Tranzactie t : tranzactii){
                        if (t.getTip() == TipTranzactie.CREDIT){
                            monthlyReport.putIfAbsent(t.getData().substring(0, 7), new double[2]);
                            monthlyReport.get(t.getData().substring(0, 7))[0] += t.getSuma();
                        }
                        else if (t.getTip() == TipTranzactie.DEBIT){
                            monthlyReport.putIfAbsent(t.getData().substring(0, 7), new double[2]);
                            monthlyReport.get(t.getData().substring(0, 7))[1] += t.getSuma();
                        }
                    }
                    for (Map.Entry<String, double[]> entry : monthlyReport.entrySet()) {
                        System.out.println(entry.getKey() + ": CREDIT " + String.format("%.2f", entry.getValue()[0]) + " RON, DEBIT " + String.format("%.2f", entry.getValue()[1]) + " RON");
                    }
                    break;

                case "TOP":
                    int nr = scanner.nextInt();
                    Collections.sort(tranzactii, comparingDouble(Tranzactie::getSuma).reversed());
                    ArrayList<Tranzactie> topTranzactii = new ArrayList<>(tranzactii.subList(0, Math.min(nr, tranzactii.size())));
                    System.out.println("Top " + nr + ":");
                    for (Tranzactie t : topTranzactii) {
                        System.out.println(t);
                    }
                    break;

                case "SORT_ASC":
                    Collections.sort(tranzactii, comparingDouble(Tranzactie::getSuma));
                    for (Tranzactie t : tranzactii) {
                         System.out.println(t);
                        }
                    break;


                case "SORT_DESC":
                    Collections.sort(tranzactii, comparingDouble(Tranzactie::getSuma).reversed());
                    for (Tranzactie t : tranzactii) {
                         System.out.println(t);
                        }
                    break;
                case "REVERSE":
                    Collections.reverse(tranzactii);
                    for (Tranzactie t : tranzactii) {
                        System.out.println(t);
                    }
                    break;

                case "MIN_MAX":
                    Tranzactie min = Collections.min(tranzactii, comparingDouble(Tranzactie::getSuma));
                    Tranzactie max = Collections.max(tranzactii, comparingDouble(Tranzactie::getSuma));

                    System.out.println("MIN: " + min);
                    System.out.println("MAX: " + max);
                    break;

                case "CME_DEMO":
                    try{
                        for(Tranzactie t : tranzactii){
                            tranzactii.remove(t);
                        }

                    }catch(ConcurrentModificationException e){
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
            }

        }
    }
}
