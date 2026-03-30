package com.pao.laboratory06.exercise3;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {
    public Inginer(String nume, String prenume, String telefon, double salariu) {
        super(nume, prenume, telefon, salariu);
    }

    @Override
    public int compareTo(Inginer other) {
        if (other == null) {
            throw new IllegalArgumentException("Inginerul de comparat nu poate fi null.");
        }
        return this.getNume().compareToIgnoreCase(other.getNume());
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.trim().isEmpty() || parola == null || parola.trim().isEmpty()) {
            throw new IllegalArgumentException("User-ul si parola nu pot fi null sau goale.");
        }
        System.out.println("Autentificare reusita!");
    }

    @Override
    public double consultareSold() {
        return getSalariu();
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0 || suma > getSalariu()) {
            return false;
        }
        setSalariu(getSalariu() - suma);
        return true;
    }
}
