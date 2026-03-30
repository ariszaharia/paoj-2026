package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Inginer[] ingineri = {
                new Inginer("Popescu", "Ion", "0712345678", 5000),
                new Inginer("Ionescu", "Maria", "0722000000", 7500),
                new Inginer("Apostol", "Vlad", "0733000000", 6200)
        };

        Arrays.sort(ingineri);
        System.out.println("Sortare naturala dupa nume:");
        for (Inginer inginer : ingineri) {
            System.out.println(inginer.getNume() + " - " + inginer.getSalariu());
        }

        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        System.out.println("\nSortare descrescatoare dupa salariu:");
        for (Inginer inginer : ingineri) {
            System.out.println(inginer.getNume() + " - " + inginer.getSalariu());
        }

        PlataOnline plataOnline = ingineri[0];
        plataOnline.autentificare("userInginer", "parolaSigura");
        System.out.println("Plata 500 efectuata? " + plataOnline.efectuarePlata(500));
        System.out.println("Sold ramas inginer: " + plataOnline.consultareSold());

        PlataOnlineSMS clientFaraTelefon = new PersoanaJuridica("Tech", "SRL", "", 15000);
        System.out.println("SMS fara telefon: " + clientFaraTelefon.trimiteSMS("Plata in proces"));

        PersoanaJuridica clientCuTelefon = new PersoanaJuridica("Data", "Solutions", "0744000000", 20000);
        PlataOnlineSMS clientSms = clientCuTelefon;
        System.out.println("SMS valid: " + clientSms.trimiteSMS("Plata confirmata"));
        System.out.println("SMS invalid: " + clientSms.trimiteSMS("   "));
        System.out.println("SMS trimise: " + clientCuTelefon.getSmsTrimise());

        System.out.println("TVA curent: " + ConstanteFinanciare.TVA.getValoare());

        try {
            plataOnline.autentificare(null, "abc");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare autentificare: " + e.getMessage());
        }

        try {
            trimiteMesajObligatoriuSms(plataOnline, "Test SMS");
        } catch (UnsupportedOperationException e) {
            System.out.println("Eroare capabilitate SMS: " + e.getMessage());
        }
    }

    private static boolean trimiteMesajObligatoriuSms(PlataOnline client, String mesaj) {
        if (!(client instanceof PlataOnlineSMS)) {
            throw new UnsupportedOperationException("Entitatea nu are capabilitate SMS.");
        }
        return ((PlataOnlineSMS) client).trimiteSMS(mesaj);
    }
}
