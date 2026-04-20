package com.pao.project.Eticketing.model.user;

import com.pao.project.Eticketing.model.order.Comanda;
import com.pao.project.Eticketing.model.order.ComandaNormala;
import com.pao.project.Eticketing.model.order.ComandaSenior;
import com.pao.project.Eticketing.model.order.ComandaStudent;

public class Client extends Utilizator {
    private final TipClient tipClient;

    public Client(String username, String password) {
        this(username, password, TipClient.NORMAL);
    }

    public Client(String username, String password, TipClient tipClient) {
        super(username, password);
        if (tipClient == null) {
            throw new IllegalArgumentException("Tipul clientului nu poate fi null");
        }
        this.tipClient = tipClient;
    }

    @Override
    public boolean pay(Comanda comanda) {
        if (comanda == null) {
            throw new IllegalArgumentException("Comanda nu poate fi null");
        }

        double pretTotal = comanda.calculeazaPretTotal();
        chargeForOrder(pretTotal);
        return true;
    }

    public TipClient getTipClient() {
        return tipClient;
    }

    public Comanda creeazaComandaAutomata() {
        return switch (tipClient) {
            case STUDENT -> new ComandaStudent();
            case SENIOR -> new ComandaSenior();
            case NORMAL -> new ComandaNormala();
        };
    }
}
