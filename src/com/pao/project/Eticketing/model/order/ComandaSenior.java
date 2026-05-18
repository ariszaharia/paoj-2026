package com.pao.project.Eticketing.model.order;

import com.pao.project.Eticketing.model.ticket.Bilet;
import com.pao.project.Eticketing.model.user.TipClient;

import java.util.List;

public class ComandaSenior extends Comanda {
    @Override
    public String getTipComanda() { return "SENIOR"; }

    @Override
    public double calculeazaPretTotal(){
        List<Bilet> bilete = getBilete();
        double suma = 0;
        for (Bilet b : bilete){
            suma += b.getPret();
        }
        return suma * TipClient.SENIOR.getFactorPlata();
    }
}
