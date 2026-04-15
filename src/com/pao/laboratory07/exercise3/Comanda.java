package com.pao.laboratory07.exercise3;

import com.pao.laboratory07.exercise1.OrderState;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected final String nume;
    protected final String client;
    protected final OrderState stare;

    protected Comanda(String nume, String client) {
        this.nume = nume;
        this.client = client;
        this.stare = OrderState.PLACED;
    }

    public abstract double pretFinal();

    public abstract String tip();

    public abstract String descriereFaraClient();

    public String descriere() {
        return descriereFaraClient() + " [" + stare + "] - client: " + client;
    }

    public String descriereScurta() {
        return descriereFaraClient() + " - client: " + client;
    }

    public String client() {
        return client;
    }
}

