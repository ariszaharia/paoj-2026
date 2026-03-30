package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator extends PersoanaJuridica implements IOperatiiCitireScriere {
    private double cheltuieliLunare;

    public SRLColaborator() {}

    public SRLColaborator(String nume, String prenume, double venitBrutLunar, double cheltuieliLunare) {
        super(nume, prenume, venitBrutLunar);
        this.cheltuieliLunare = cheltuieliLunare;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitAnual = (venitBrutLunar - cheltuieliLunare) * 12;
        return venitAnual * 0.84;
    }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public void afiseaza() {
        System.out.println(descriere());
    }

    @Override
    public String tipContract() {
        return "SRL";
    }
}