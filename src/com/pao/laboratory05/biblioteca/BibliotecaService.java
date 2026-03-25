package com.pao.laboratory05.biblioteca;


import com.pao.laboratory05.angajati.Angajat;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {

    private Carte[] carti = new Carte[0];


    private BibliotecaService(){}

    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance(){
        return Holder.INSTANCE;
    }


    void addCarte(Carte c){
        carti = Arrays.copyOf(carti, carti.length + 1);
        carti[carti.length - 1] = c;
    }

    void listSortedByRating(){
        Carte[] copy = carti.clone();
        Arrays.sort(copy);

        for(Carte c : copy){
            System.out.println(c);
        }
    }

    void listSortedBy(Comparator<Carte> comparator){
        Carte[] copy = carti.clone();
        Arrays.sort(copy, comparator);

        for (Carte c : copy){
            System.out.println(c);
        }
    }
}
