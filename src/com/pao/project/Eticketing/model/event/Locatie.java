package com.pao.project.Eticketing.model.event;

import java.util.Objects;

public class Locatie {
    private final String denumire;
    private final int capacitate;
    private final String oras;

    public Locatie(String denumire, String oras,  int capacitate) {
        if (denumire == null || denumire.trim().isEmpty()) {
            throw new IllegalArgumentException("Denumirea locatiei este invalida");
        }
        if (capacitate <= 0) {
            throw new IllegalArgumentException("Capacitatea trebuie sa fie pozitiva");
        }

        this.denumire = denumire;
        this.oras = oras;
        this.capacitate = capacitate;
    }

    public String getDenumire() {
        return denumire;
    }

    public int getCapacitate() {
        return capacitate;
    }

    @Override
    public String toString() {
        return "Locatie{" +
                "denumire='" + denumire + '\'' +
                ", capacitate=" + capacitate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Locatie locatie)) return false;
        return capacitate == locatie.capacitate && denumire.equals(locatie.denumire);
    }

    @Override
    public int hashCode() {
        return Objects.hash(denumire, capacitate);
    }
}
