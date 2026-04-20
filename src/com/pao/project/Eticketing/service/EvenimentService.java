package com.pao.project.Eticketing.service;

import com.pao.project.Eticketing.model.event.Eveniment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EvenimentService {
    private static EvenimentService instance;

    private final List<Eveniment> evenimente = new ArrayList<>();

    private EvenimentService() {
    }

    public static EvenimentService getInstance() {
        if (instance == null) {
            instance = new EvenimentService();
        }
        return instance;
    }

    public void addEveniment(Eveniment eveniment) {
        if (eveniment == null) {
            throw new IllegalArgumentException("Evenimentul nu poate fi null");
        }
        evenimente.add(eveniment);
    }

    public boolean deleteEveniment(Eveniment eveniment) {
        if (eveniment == null) {
            throw new IllegalArgumentException("Evenimentul nu poate fi null");
        }
        return evenimente.remove(eveniment);
    }

    public Eveniment findByDenumire(String denumire) {
        if (denumire == null || denumire.trim().isEmpty()) {
            throw new IllegalArgumentException("Denumirea nu poate fi goala");
        }

        for (Eveniment eveniment : evenimente) {
            if (eveniment.getDenumire().equalsIgnoreCase(denumire.trim())) {
                return eveniment;
            }
        }
        return null;
    }

    public List<Eveniment> getEvenimente() {
        return new ArrayList<>(evenimente);
    }

    public List<Eveniment> getEvenimenteSortateNatural() {
        List<Eveniment> rezultat = new ArrayList<>(evenimente);
        Collections.sort(rezultat);
        return rezultat;
    }
}
