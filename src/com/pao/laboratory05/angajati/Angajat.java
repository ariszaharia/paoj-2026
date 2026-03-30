package com.pao.laboratory05.angajati;

public class Angajat implements Comparable<Angajat> {
    private String nume;
    private Departament departament;
    private double salariu;

    public Angajat(String nume, Departament departament, double salariu) {
        this.nume = nume;
        this.departament = departament;
        this.salariu = salariu;
    }

    public Departament getDepartament() {
        return departament;
    }

    public double getSalariu() {
        return salariu;
    }
    public String getNume(){return nume;}


    @Override
    public int compareTo(Angajat o) {
        return Double.compare(this.salariu, o.salariu);
    }

    @Override
    public String toString() {
        return "Angajat{nume='" + nume + "', departament=" + departament + ", salariu=" + salariu + "}";
    }
}
