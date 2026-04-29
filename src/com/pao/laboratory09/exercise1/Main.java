package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Tranzactie> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String[] parts = sc.nextLine().trim().split(" ");
            int id = Integer.parseInt(parts[0]);
            double suma = Double.parseDouble(parts[1]);
            String data = parts[2];
            String contSursa = parts[3];
            String contDestinatie = parts[4];
            TipTranzactie tip = TipTranzactie.valueOf(parts[5]);
            Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            t.note = "procesat";
            list.add(t);
        }

        new File(OUTPUT_FILE).getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            oos.writeObject(list);
        }

        List<Tranzactie> loaded;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            loaded = (List<Tranzactie>) ois.readObject();
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(" ");
            String cmd = parts[0];
            if (cmd.equals("LIST")) {
                for (Tranzactie t : loaded) {
                    printTranzactie(t);
                }
            } else if (cmd.equals("FILTER")) {
                String prefix = parts[1];
                boolean found = false;
                for (Tranzactie t : loaded) {
                    if (t.getData().startsWith(prefix)) {
                        printTranzactie(t);
                        found = true;
                    }
                }
                if (!found) System.out.println("Niciun rezultat.");
            } else if (cmd.equals("NOTE")) {
                int id = Integer.parseInt(parts[1]);
                Tranzactie found = null;
                for (Tranzactie t : loaded) {
                    if (t.getId() == id) { found = t; break; }
                }
                if (found == null) {
                    System.out.println("NOTE[" + id + "]: not found");
                } else {
                    System.out.println("NOTE[" + id + "]: " + found.note);
                }
            }
        }
    }

    private static void printTranzactie(Tranzactie t) {
        System.out.printf("[%d] %s %s: %.2f RON | %s -> %s%n",
                t.getId(), t.getData(), t.getTip(), t.getSuma(),
                t.getContSursa(), t.getContDestinatie());
    }
}
