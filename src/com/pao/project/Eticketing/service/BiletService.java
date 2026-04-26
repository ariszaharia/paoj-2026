package com.pao.project.Eticketing.service;

import com.pao.project.Eticketing.model.ticket.Bilet;
import com.pao.project.Eticketing.model.event.Eveniment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiletService {
    private static BiletService instance;
    private final Map<Eveniment, List<Bilet>> biletePeEveniment = new HashMap<>();

    private BiletService() {
    }

    public static BiletService getInstance() {
        if (instance == null) {
            instance = new BiletService();
        }
        return instance;
    }

    public void addBilet(Bilet b) {
        if (b == null) {
            throw new IllegalArgumentException("Biletul nu poate fi null");
        }
        Eveniment eveniment = b.getEveniment();
        if (!biletePeEveniment.containsKey(eveniment)) {
            biletePeEveniment.put(eveniment, new ArrayList<>());
        }
        biletePeEveniment.get(eveniment).add(b);
    }

    public boolean deleteBilet(Bilet b) {
        if (b == null) {
            throw new IllegalArgumentException("Biletul nu poate fi null");
        }
        List<Bilet> lista = biletePeEveniment.get(b.getEveniment());
        if (lista == null) return false;
        return lista.remove(b);
    }

    public List<Bilet> findByEveniment(Eveniment eveniment) {
        if (eveniment == null) {
            throw new IllegalArgumentException("Evenimentul nu poate fi null");
        }
        List<Bilet> lista = biletePeEveniment.get(eveniment);
        return lista != null ? new ArrayList<>(lista) : new ArrayList<>();
    }

    public Bilet findByDescriere(String descriere) {
        if (descriere == null || descriere.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrierea nu poate fi goala");
        }
        for (List<Bilet> lista : biletePeEveniment.values()) {
            for (Bilet bilet : lista) {
                if (bilet.getDescriere() != null && bilet.getDescriere().equalsIgnoreCase(descriere.trim())) {
                    return bilet;
                }
            }
        }
        return null;
    }

    public List<Bilet> getBilete() {
        List<Bilet> toate = new ArrayList<>();
        for (List<Bilet> lista : biletePeEveniment.values()) {
            toate.addAll(lista);
        }
        return toate;
    }

    public Map<Eveniment, List<Bilet>> getBiletePeEveniment() {
        return new HashMap<>(biletePeEveniment);
    }
}
