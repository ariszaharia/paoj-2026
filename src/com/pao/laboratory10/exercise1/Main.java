package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        LinkedList<Tranzactie> linkedList = new LinkedList<>();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String comanda = scanner.next();
            
            switch (comanda) {
                case "ENQUEUE": {
                    int id = scanner.nextInt();
                    double suma = scanner.nextDouble();
                    String data = scanner.next();
                    String tip = scanner.next();
                    linkedList.addLast(new Tranzactie(id, suma, data, TipTranzactie.valueOf(tip)));
                    break;
                }
                case "DEQUEUE": {
                    if (linkedList.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        Tranzactie t = linkedList.removeFirst();
                        System.out.println("Procesat: " + t);
                    }
                    break;
                }
                case "PUSH": {
                    int id = scanner.nextInt();
                    double suma = scanner.nextDouble();
                    String data = scanner.next();
                    String tip = scanner.next();
                    linkedList.addFirst(new Tranzactie(id, suma, data, TipTranzactie.valueOf(tip)));
                    break;
                }
                case "POP": {
                    if (linkedList.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        Tranzactie t = linkedList.removeFirst();
                        System.out.println("Extras: " + t);
                    }
                    break;
                }
                case "REMOVE_DEBIT": {
                    int count = 0;
                    Iterator<Tranzactie> itr = linkedList.iterator();
                    while (itr.hasNext()) {
                        Tranzactie t = itr.next();
                        if (t.getTip() == TipTranzactie.DEBIT) {
                            itr.remove();
                            count++;
                        }
                    }
                    System.out.println("Eliminat " + count + " tranzactii DEBIT.");
                    break;
                }
                case "REMOVE_BELOW": {
                    double threshold = scanner.nextDouble();
                    int count = 0;
                    Iterator<Tranzactie> itr = linkedList.iterator();
                    while (itr.hasNext()) {
                        Tranzactie t = itr.next();
                        if (t.getSuma() < threshold) {
                            itr.remove();
                            count++;
                        }
                    }
                    System.out.println("Eliminat " + count + " tranzactii sub " + String.format("%.2f", threshold) + " RON.");
                    break;
                }
                case "PRINT": {
                    for (Tranzactie t : linkedList) {
                        System.out.println(t);
                    }
                    break;
                }
                case "SIZE": {
                    System.out.println("Dimensiune coada: " + linkedList.size());
                    break;
                }
            }
        }

    }
}
