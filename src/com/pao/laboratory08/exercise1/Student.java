package com.pao.laboratory08.exercise1;

public class Student {
    private String nume;
    private int varsta;
    private Adresa adresa;

    public Student(String nume, int varsta, Adresa adresa) {
        this.nume = nume;
        this.varsta = varsta;
        this.adresa = adresa;
    }

    public Student(Student altStudent) {
        this.nume = altStudent.nume;
        this.varsta = altStudent.varsta;
        this.adresa = altStudent.adresa;
    }

    public Student shallowClone() {
        return new Student(this);
    }

    public Student deepClone() {
        Student clona = new Student(this);
        clona.setAdresa(new Adresa(this.adresa));
        return clona;
    }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public int getVarsta() { return varsta; }
    public void setVarsta(int varsta) { this.varsta = varsta; }
    public Adresa getAdresa() { return adresa; }
    public void setAdresa(Adresa adresa) { this.adresa = adresa; }

    @Override
    public String toString() {
        return "Student{nume='" + nume + "', varsta=" + varsta + ", adresa=" + adresa + "}";
    }
}
