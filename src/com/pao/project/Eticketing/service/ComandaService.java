package com.pao.project.Eticketing.service;

import com.pao.project.Eticketing.model.order.Tranzactie;
import com.pao.project.Eticketing.model.order.Comanda;
import com.pao.project.Eticketing.model.user.Client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ComandaService {
    private static ComandaService instance;
    private final List<Comanda> comenzi;
    private final List<Tranzactie> tranzactii;

    private ComandaService(){
        this.comenzi = new ArrayList<>();
        this.tranzactii = new ArrayList<>();
    }

    public static ComandaService getInstance(){
        if (instance == null){
            instance = new ComandaService();
        }
        return instance;
    }
    
    public Tranzactie platesteComanda(Client client, Comanda comanda) {
        if (client == null) throw new IllegalArgumentException("Clientul nu poate fi null");
        if (comanda == null) throw new IllegalArgumentException("Comanda nu poate fi null");

        double total = comanda.calculeazaPretTotal();
        client.pay(comanda);
        comenzi.add(comanda);
        Tranzactie t = new Tranzactie(client.getUsername(), comanda.getId(), total);
        tranzactii.add(t);
        return t;
    }

    public List<Tranzactie> getTranzactii() {
        return new ArrayList<>(tranzactii);
    }

    public boolean deleteComanda(Comanda c) {
        if (c == null) {
            throw new IllegalArgumentException("Comanda nu poate fi null");
        }
        return comenzi.remove(c);
    }

    public List<Comanda> findAllComenzi() {
        return new ArrayList<>(comenzi);
    }

    public Comanda findComandaByIndex(int index) {
        if (index < 0 || index >= comenzi.size()) {
            return null;
        }
        return comenzi.get(index);
    }

    public boolean deleteComandaByIndex(int index) {
        if (index < 0 || index >= comenzi.size()) {
            return false;
        }
        comenzi.remove(index);
        return true;
    }

    public Comanda findById(int id) {
        for (Comanda c : comenzi) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public List<Comanda> findComenziSortateDupaTotal() {
        List<Comanda> rezultat = new ArrayList<>(comenzi);
        rezultat.sort(Comparator.comparingDouble(Comanda::calculeazaPretTotal));
        return rezultat;
    }

}
