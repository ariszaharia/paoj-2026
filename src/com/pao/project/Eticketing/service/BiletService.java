package com.pao.project.Eticketing.service;

import com.pao.project.Eticketing.model.ticket.Bilet;
import com.pao.project.Eticketing.model.event.Eveniment;

import java.util.ArrayList;
import java.util.List;

public class BiletService {
    private static BiletService instance;
    private final ArrayList<Bilet> bilete = new ArrayList<>();
    private BiletService(){
    }

    public static BiletService getInstance(){
        if(instance == null){
            instance = new BiletService();
        }
        return instance;
    }

    public void addBilet(Bilet b){
        if (b == null) {
            throw new IllegalArgumentException("Biletul nu poate fi null");
        }
        bilete.add(b);
    }

    public boolean deleteBilet(Bilet b) {
        if (b == null) {
            throw new IllegalArgumentException("Biletul nu poate fi null");
        }
        return bilete.remove(b);
    }

    public Bilet findByDescriere(String descriere) {
        if (descriere == null || descriere.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrierea nu poate fi goala");
        }

        for (Bilet bilet : bilete) {
            if (bilet.getDescriere() != null && bilet.getDescriere().equalsIgnoreCase(descriere.trim())) {
                return bilet;
            }
        }
        return null;
    }

    public List<Bilet> findByEveniment(Eveniment eveniment) {
        if (eveniment == null) {
            throw new IllegalArgumentException("Evenimentul nu poate fi null");
        }

        List<Bilet> rezultat = new ArrayList<>();
        for (Bilet bilet : bilete) {
            if (bilet.getEveniment().equals(eveniment)) {
                rezultat.add(bilet);
            }
        }
        return rezultat;
    }

    public ArrayList<Bilet> getBilete(){
        return new ArrayList<>(bilete);
    }



}
