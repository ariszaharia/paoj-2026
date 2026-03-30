package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS {
    private final List<String> smsTrimise;
    private double sold;

    public PersoanaJuridica(String nume, String prenume, String telefon, double soldInitial) {
        super(nume, prenume, telefon);
        if (soldInitial < 0) {
            throw new IllegalArgumentException("Soldul initial nu poate fi negativ.");
        }
        this.smsTrimise = new ArrayList<>();
        this.sold = soldInitial;
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.trim().isEmpty() || parola == null || parola.trim().isEmpty()) {
            throw new IllegalArgumentException("User-ul si parola nu pot fi null sau goale.");
        }
        System.out.println("Autentificare persoana juridica reusita!");
    }

    @Override
    public double consultareSold() {
        return sold;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0 || suma > sold) {
            return false;
        }
        sold -= suma;
        return true;
    }

    @Override
    public boolean trimiteSMS(String mesaj) {
        if (getTelefon() == null || getTelefon().trim().isEmpty() || mesaj == null || mesaj.trim().isEmpty()) {
            return false;
        }
        smsTrimise.add(mesaj);
        return true;
    }

    public List<String> getSmsTrimise() {
        return Collections.unmodifiableList(smsTrimise);
    }
}
