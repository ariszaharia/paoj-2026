package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica implements IOperatiiCitireScriere {
    private boolean bonus;

    public CIMColaborator() {
    }

    public CIMColaborator(String nume, String prenume, double venitBrutLunar, boolean bonus) {
        super(nume, prenume, venitBrutLunar);
        this.bonus = bonus;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = venitBrutLunar * 12 * 0.55;
        if (bonus) {
            venitNet *= 1.10;
        }
        return venitNet;
    }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        this.bonus = false;
        if (in.hasNext("(?i)DA|NU")) {
            this.bonus = "DA".equalsIgnoreCase(in.next());
        }
    }

    @Override
    public void afiseaza() {
        System.out.println(descriere());
    }

    @Override
    public String tipContract() {
        return "CIM";
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }
}
