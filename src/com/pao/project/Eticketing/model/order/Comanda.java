package com.pao.project.Eticketing.model.order;

import com.pao.project.Eticketing.exception.EvenimenteException;
import com.pao.project.Eticketing.model.ticket.Bilet;
import com.pao.project.Eticketing.model.event.Eveniment;

import java.util.ArrayList;
import java.util.List;

public abstract class Comanda {

    private static int nextId = 1;
    private int id;
    private int clientId;
    private final List<Bilet> bilete = new ArrayList<>();

    protected Comanda() {
        this.id = nextId++;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }
    public abstract String getTipComanda();

    public void adaugaBilet(Bilet b) {
        if (b == null) {
            throw new IllegalArgumentException("Biletul nu poate fi null");
        }

        if (!bilete.isEmpty()) {
            Eveniment e = bilete.getFirst().getEveniment();

            if (!e.equals(b.getEveniment())) {
                throw new EvenimenteException("Nu poti amesteca bilete de la evenimente diferite");
            }
        }

        bilete.add(b);
    }

    public List<Bilet> getBilete() {
        return new ArrayList<>(bilete);
    }

    public int getNumarBilete() {
        return bilete.size();
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "id=" + id +
                ", numarBilete=" + getNumarBilete() +
                ", total=" + calculeazaPretTotal() +
                '}';
    }

    public abstract double calculeazaPretTotal();
}