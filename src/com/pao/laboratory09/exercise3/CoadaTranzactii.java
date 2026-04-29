package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.Tranzactie;
import java.util.LinkedList;
import java.util.Queue;

public class CoadaTranzactii {
    private final Queue<Tranzactie> queue = new LinkedList<>();
    private final int capacitate;

    public CoadaTranzactii(int capacitate) {
        this.capacitate = capacitate;
    }

    public synchronized void adauga(Tranzactie t, String waitMsg) throws InterruptedException {
        if (queue.size() >= capacitate && waitMsg != null) {
            System.out.println(waitMsg);
        }
        while (queue.size() >= capacitate) {
            wait();
        }
        queue.add(t);
        notifyAll();
    }

    public synchronized Tranzactie extrage() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        Tranzactie t = queue.poll();
        notifyAll();
        return t;
    }

    public synchronized int size() {
        return queue.size();
    }
}
