package com.pao.project.Eticketing.service;

import com.pao.project.Eticketing.model.order.Comanda;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ComandaService {
    private static ComandaService instance;
    private final List<Comanda> comenzi;

    private ComandaService(){
        this.comenzi = new ArrayList<>();
    }

    public static ComandaService getInstance(){
        if (instance == null){
            instance = new ComandaService();
        }
        return instance;
    }
    
    public void addComanda(Comanda c){
        if (c == null) {
            throw new IllegalArgumentException("Comanda nu poate fi null");
        }
        comenzi.add(c);
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

    public List<Comanda> findComenziSortateDupaTotal() {
        List<Comanda> rezultat = new ArrayList<>(comenzi);
        rezultat.sort(Comparator.comparingDouble(Comanda::calculeazaPretTotal));
        return rezultat;
    }

}
