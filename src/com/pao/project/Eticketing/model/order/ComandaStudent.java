package com.pao.project.Eticketing.model.order;

import com.pao.project.Eticketing.model.ticket.Bilet;
import com.pao.project.Eticketing.model.user.TipClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ComandaStudent extends Comanda {
    @Override
    public double calculeazaPretTotal(){
        List<Bilet> bilete = getBilete();
        double suma = 0;
        for (Bilet b : bilete){
            suma += b.getPret();
        }
        return suma * TipClient.STUDENT.getFactorPlata();
    }

    public static final class Tranzactie {
        private static int nextId = 1;

        private final int id;
        private final String clientUsername;
        private final int comandaId;
        private final double suma;
        private final LocalDateTime timestamp;

        public Tranzactie(String clientUsername, int comandaId, double suma) {
            if (clientUsername == null || clientUsername.trim().isEmpty()) {
                throw new IllegalArgumentException("Username client invalid");
            }
            if (suma <= 0) {
                throw new IllegalArgumentException("Suma tranzactiei trebuie sa fie pozitiva");
            }
            this.id = nextId++;
            this.clientUsername = clientUsername;
            this.comandaId = comandaId;
            this.suma = suma;
            this.timestamp = LocalDateTime.now();
        }

        public int getId() { return id; }
        public String getClientUsername() { return clientUsername; }
        public int getComandaId() { return comandaId; }
        public double getSuma() { return suma; }
        public LocalDateTime getTimestamp() { return timestamp; }

        @Override
        public String toString() {
            return "Tranzactie{" +
                    "id=" + id +
                    ", client='" + clientUsername + '\'' +
                    ", comandaId=" + comandaId +
                    ", suma=" + suma +
                    ", timestamp=" + timestamp +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Tranzactie that)) return false;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
