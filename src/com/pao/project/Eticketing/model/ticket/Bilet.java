package com.pao.project.Eticketing.model.ticket;

import com.pao.project.Eticketing.exception.PretIncorect;
import com.pao.project.Eticketing.model.event.Eveniment;

import java.util.Objects;

public class Bilet {
    private int id;
    private int comandaId;
    private final double pret;
    private final Eveniment eveniment;
    private final String descriere;

    public Bilet(double pret, Eveniment eveniment, String descriere) {
        if (pret <= 0) {
            throw new PretIncorect("Pretul biletului trebuie sa fie pozitiv");
        }
        if (eveniment == null) {
            throw new IllegalArgumentException("Evenimentul nu poate fi null");
        }

        this.pret = pret;
        this.eveniment = eveniment;
        this.descriere = descriere;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getComandaId() { return comandaId; }
    public void setComandaId(int comandaId) { this.comandaId = comandaId; }

    public double getPret() {
        return pret;
    }

    public Eveniment getEveniment() {
        return eveniment;
    }

    public String getDescriere() {
        return descriere;
    }



    @Override
    public String toString() {
        return "Bilet{" +
                "pret=" + pret +
                ", descriere='" + descriere + '\'' +
                ", eveniment=" + eveniment +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bilet bilet)) return false;
        return Double.compare(pret, bilet.pret) == 0 && eveniment.equals(bilet.eveniment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pret, eveniment);
    }
}
