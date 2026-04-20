package com.pao.project.Eticketing.model.order;

import com.pao.project.Eticketing.model.ticket.Bilet;
import com.pao.project.Eticketing.model.user.TipClient;

import java.util.List;

public class ComandaNormala extends Comanda {
    @Override
    public double calculeazaPretTotal(){
        List<Bilet> bilete = getBilete();
        double suma = 0;
        for (Bilet b : bilete){
            suma += b.getPret();
        }
        return suma * TipClient.NORMAL.getFactorPlata();
    }
}
