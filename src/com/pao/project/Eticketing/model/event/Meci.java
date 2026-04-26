package com.pao.project.Eticketing.model.event;

public class Meci extends Eveniment{
    private final String echipaGazda;
    private final String echipaOaspete;

    public Meci(String denumire, String data, int durataMinute, Locatie locatie, String echipaGazda, String echipaOaspete, int nr_bilete) {
        super(denumire, data, durataMinute, locatie, nr_bilete);
        if (echipaGazda == null || echipaGazda.trim().isEmpty()) {
            throw new IllegalArgumentException("Echipa gazda invalida");
        }
        if (echipaOaspete == null || echipaOaspete.trim().isEmpty()) {
            throw new IllegalArgumentException("Echipa oaspete invalida");
        }
        this.echipaGazda = echipaGazda;
        this.echipaOaspete = echipaOaspete;
    }

    public String getEchipaGazda() {
        return echipaGazda;
    }

    public String getEchipaOaspete() {
        return echipaOaspete;
    }

    @Override
    public String toString() {
        return "Meci{" +
                "denumire='" + getDenumire() + '\'' +
                ", data='" + getData() + '\'' +
                ", echipaGazda='" + echipaGazda + '\'' +
                ", echipaOaspete='" + echipaOaspete + '\'' +
                '}';
    }
}
