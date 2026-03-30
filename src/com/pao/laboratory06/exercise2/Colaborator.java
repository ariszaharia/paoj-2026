package com.pao.laboratory06.exercise2;

import java.util.Locale;

public abstract class Colaborator implements IOperatiiCitireScriere {
    protected String nume;
    protected String prenume;
    protected double venitBrutLunar;

    public Colaborator() {
    }

    public Colaborator(String nume, String prenume, double venitBrutLunar) {
        this.nume = nume;
        this.prenume = prenume;
        this.venitBrutLunar = venitBrutLunar;
    }

    public abstract double calculeazaVenitNetAnual();

    @Override
    public String descriere() {
        return String.format(Locale.US, "%s: %s %s, venit net anual: %.2f lei",
                tipContract(), nume, prenume, calculeazaVenitNetAnual());
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public double getVenitBrutLunar() {
        return venitBrutLunar;
    }
}
