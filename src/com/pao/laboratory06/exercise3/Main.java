package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Inginer[] ingineri = new Inginer[] {
                new Inginer("Pop", "Ana", "0712345678", 6000),
                new Inginer("Ionescu", "Mihai", "0722334455", 4500),
                new Inginer("Dumitru", "Elena", "0733111222", 7000)
        };

        Arrays.sort(ingineri); // natural order: nume
        System.out.println("Sortare naturala (dupa nume):");
        for (Inginer inginer : ingineri) {
            System.out.println(inginer.getNume() + " " + inginer.getPrenume() + " -> " + inginer.getSalariu());
        }

        Arrays.sort(ingineri, new ComparatorInginerSalariu()); // descrescator dupa salariu
        System.out.println("Sortare cu comparator (dupa salariu desc):");
        for (Inginer inginer : ingineri) {
            System.out.println(inginer.getNume() + " " + inginer.getPrenume() + " -> " + inginer.getSalariu());
        }

        PlataOnline plataInginer = new Inginer("Marin", "Ioana", "0700000000", 5200);
        plataInginer.autentificare("ioana", "parola");
        System.out.println("Sold inginer (prin PlataOnline): " + plataInginer.consultareSold());
        System.out.println("Plata inginer (2500): " + plataInginer.efectuarePlata(2500));

        PlataOnlineSMS firma = new PersoanaJuridica("Firma", "SRL", "0744555666", 15000);
        System.out.println("Trimite SMS valid: " + firma.trimiteSMS("Plata procesata"));
        System.out.println("Trimite SMS invalid (mesaj gol): " + firma.trimiteSMS(""));

        PersoanaJuridica firmaConcreta = (PersoanaJuridica) firma;
        System.out.println("SMS-uri stocate: " + firmaConcreta.getSmsTrimise());

        PlataOnlineSMS firmaFaraTelefon = new PersoanaJuridica("Firma", "FaraTel", null, 8000);
        System.out.println("Trimite SMS fara telefon: " + firmaFaraTelefon.trimiteSMS("Confirmare"));

        System.out.println("TVA: " + ConstanteFinanciare.TVA.getValoare());

        // 5) Tratare cazuri de eroare.
        try {
            plataInginer.autentificare(null, "parola");
        } catch (IllegalArgumentException ex) {
            System.out.println("Eroare autentificare (user null): " + ex.getMessage());
        }

        try {
            PlataOnlineSMS entitateGresita = (PlataOnlineSMS) plataInginer; // Inginer nu are capabilitate SMS
            entitateGresita.trimiteSMS("Test");
        } catch (ClassCastException ex) {
            System.out.println("Eroare entitate gresita (SMS): " + ex.getMessage());
        }
    }
}
