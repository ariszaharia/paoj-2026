package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica implements IOperatiiCitireScriere {
    private static final double SALARIU_MINIM_BRUT_ANUAL_2026 = 4050.0 * 12;
    private static final double COTA_IMPOZIT_VENIT = 0.10;
    private static final double COTA_CASS = 0.10;
    private static final double COTA_CAS = 0.25;

    private double cheltuieliLunare;

    public PFAColaborator() {}

    public PFAColaborator(String nume, String prenume, double venitBrutLunar, double cheltuieliLunare) {
        super(nume, prenume, venitBrutLunar);
        this.cheltuieliLunare = cheltuieliLunare;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNetInainteDeTaxe = (venitBrutLunar - cheltuieliLunare) * 12;

        double impozitVenit = COTA_IMPOZIT_VENIT * venitNetInainteDeTaxe;
        double cass = calculeazaCASS(venitNetInainteDeTaxe);
        double cas = calculeazaCAS(venitNetInainteDeTaxe);

        return venitNetInainteDeTaxe - impozitVenit - cass - cas;
    }

    private double calculeazaCASS(double venitNetAnual) {
        double prag6Salarii = 6 * SALARIU_MINIM_BRUT_ANUAL_2026;
        double prag72Salarii = 72 * SALARIU_MINIM_BRUT_ANUAL_2026;

        if (venitNetAnual < prag6Salarii) {
            return COTA_CASS * prag6Salarii;
        }
        if (venitNetAnual <= prag72Salarii) {
            return COTA_CASS * venitNetAnual;
        }
        return COTA_CASS * prag72Salarii;
    }

    private double calculeazaCAS(double venitNetAnual) {
        double prag12Salarii = 12 * SALARIU_MINIM_BRUT_ANUAL_2026;
        double prag24Salarii = 24 * SALARIU_MINIM_BRUT_ANUAL_2026;

        if (venitNetAnual < prag12Salarii) {
            return 0.0;
        }
        if (venitNetAnual <= prag24Salarii) {
            return COTA_CAS * prag12Salarii;
        }
        return COTA_CAS * prag24Salarii;
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
        return "PFA";
    }
}
