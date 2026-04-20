package com.pao.project.Eticketing.model.event;

import java.util.Objects;

public abstract class Eveniment implements Comparable<Eveniment> {
    private final String denumire;
    private final String data;
    private final int durataMinute;
    private final Locatie locatie;

    protected Eveniment(String denumire, String data, int durataMinute, Locatie locatie) {
        if (denumire == null || denumire.trim().isEmpty()) {
            throw new IllegalArgumentException("Denumirea evenimentului este invalida");
        }
        if (data == null || data.trim().isEmpty()) {
            throw new IllegalArgumentException("Data evenimentului este invalida");
        }
        if (durataMinute <= 0) {
            throw new IllegalArgumentException("Durata trebuie sa fie pozitiva");
        }
        if (locatie == null) {
            throw new IllegalArgumentException("Locatia nu poate fi null");
        }

        this.denumire = denumire;
        this.data = data;
        this.durataMinute = durataMinute;
        this.locatie = locatie;
    }

    public String getDenumire() {
        return denumire;
    }

    public String getData() {
        return data;
    }

    public int getDurataMinute() {
        return durataMinute;
    }

    public Locatie getLocatie() {
        return locatie;
    }

    @Override
    public int compareTo(Eveniment altEveniment) {
        int byData = this.data.compareTo(altEveniment.data);
        if (byData != 0) {
            return byData;
        }
        return this.denumire.compareTo(altEveniment.denumire);
    }

    @Override
    public String toString() {
        return "Eveniment{" +
                "denumire='" + denumire + '\'' +
                ", data='" + data + '\'' +
                ", durataMinute=" + durataMinute +
                ", locatie=" + locatie +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Eveniment eveniment)) return false;
        return durataMinute == eveniment.durataMinute && denumire.equals(eveniment.denumire) && data.equals(eveniment.data) && locatie.equals(eveniment.locatie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(denumire, data, durataMinute, locatie);
    }
}
