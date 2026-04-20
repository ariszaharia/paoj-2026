package com.pao.project.Eticketing.model.user;

public enum TipClient {
    NORMAL(1.0),
    STUDENT(0.5),
    SENIOR(0.7);

    private final double factorPlata;

    TipClient(double factorPlata) {
        this.factorPlata = factorPlata;
    }

    public double getFactorPlata() {
        return factorPlata;
    }
}
