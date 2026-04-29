package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.TipTranzactie;
import com.pao.laboratory09.exercise1.Tranzactie;

public class ATMThread extends Thread {
    private final int atmId;
    private final CoadaTranzactii coada;
    private static final int NR_TRANZACTII = 4;

    public ATMThread(int atmId, CoadaTranzactii coada) {
        this.atmId = atmId;
        this.coada = coada;
    }

    @Override
    public void run() {
        for (int i = 1; i <= NR_TRANZACTII; i++) {
            int id = (atmId - 1) * NR_TRANZACTII + i;
            double suma = 100.0 * id;
            Tranzactie t = new Tranzactie(id, suma, "2024-01-15", "SRC", "DST", TipTranzactie.CREDIT);
            try {
                System.out.println("[ATM-" + atmId + "] trimite: Tranzactie #" + id + " " + suma + " RON");
                coada.adauga(t, "[ATM-" + atmId + "] astept loc...");
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
