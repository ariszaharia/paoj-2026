package com.pao.laboratory07.exercise3;
public final class ComandaRedusa extends Comanda {
    private final double pret;
    private final int discountProcent;
    public ComandaRedusa(String nume, double pret, int discountProcent, String client) {
        super(nume, client);
        this.pret = pret;
        this.discountProcent = discountProcent;
    }
    @Override
    public double pretFinal() {
        return pret * (1 - discountProcent / 100.0);
    }
    @Override
    public String tip() {
        return "DISCOUNTED";
    }
    public int discountProcent() {
        return discountProcent;
    }
    @Override
    public String descriereFaraClient() {
        return String.format("DISCOUNTED: %s, pret: %.2f lei (-%d%%)", nume, pretFinal(), discountProcent);
    }
}
