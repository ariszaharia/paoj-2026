package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {

    private Angajat[] angajati = new Angajat[0];

    private AngajatService() {}

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    public void addAngajat(Angajat a) {
        angajati = Arrays.copyOf(angajati, angajati.length + 1);
        angajati[angajati.length - 1] = a;
    }

    public void printAll() {
        if (angajati.length == 0) {
            System.out.println("Nu exista angajati.");
            return;
        }

        for (Angajat a : angajati) {
            System.out.println(a);
        }
    }

    public void listBySalary() {
        if (angajati.length == 0) {
            System.out.println("Nu exista angajati.");
            return;
        }

        Angajat[] copy = angajati.clone();

        Arrays.sort(copy);

        for (int i = copy.length - 1; i >= 0; i--) {
            System.out.println(copy[i]);
        }
    }

    public void findByDepartament(String numeDept) {
        if (angajati.length == 0) {
            System.out.println("Nu exista angajati.");
            return;
        }

        boolean gasit = false;

        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(a);
                gasit = true;
            }
        }

        if (!gasit) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }
}