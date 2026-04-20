package com.pao.project.Eticketing.model.user;

import com.pao.project.Eticketing.model.order.Comanda;

public class Organizator extends Utilizator {

    public Organizator(String username, String password) {
        super(username, password);
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
}
