package com.pao.laboratory06.exercise3;

public abstract class Persoana {
    private final String nume;
    private final String prenume;
    private final String telefon;

    protected Persoana(String nume, String prenume, String telefon) {
        if (nume == null || nume.trim().isEmpty()) {
            throw new IllegalArgumentException("Numele nu poate fi null sau gol.");
        }
        if (prenume == null || prenume.trim().isEmpty()) {
            throw new IllegalArgumentException("Prenumele nu poate fi null sau gol.");
        }
        this.nume = nume;
        this.prenume = prenume;
        this.telefon = telefon;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getTelefon() {
        return telefon;
    }
}
