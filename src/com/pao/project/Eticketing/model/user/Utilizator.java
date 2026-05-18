package com.pao.project.Eticketing.model.user;

import com.pao.project.Eticketing.exception.InsufficientFundsException;
import com.pao.project.Eticketing.exception.InvalidDeposit;
import com.pao.project.Eticketing.model.order.Comanda;

import java.util.Objects;

public abstract class Utilizator {
    private int id;
    private final String username;
    private final String password;
    private double balance;

    public Utilizator(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username invalid");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Parola invalida");
        }

        this.username = username;
        this.password = password;
        this.balance = 0.0;
    }

    public boolean hasPassword(String password) {
        if (password == null) {
            return false;
        }
        return this.password.equals(password);
    }

    public void deposit(double suma) {
        if (suma <= 0) {
            throw new InvalidDeposit("Suma de depunere trebuie sa fie pozitiva");
        }
        balance += suma;
    }

    public void withdraw(double suma) {
        debitBalance(suma, "Suma de retragere trebuie sa fie pozitiva");
    }

    protected void chargeForOrder(double suma) {
        debitBalance(suma, "Suma de plata trebuie sa fie pozitiva");
    }

    private void debitBalance(double suma, String invalidAmountMessage) {
        if (suma <= 0) {
            throw new IllegalArgumentException(invalidAmountMessage);
        }
        if (balance < suma) {
            throw new InsufficientFundsException("Fonduri insuficiente");
        }
        balance -= suma;
    }

    public abstract boolean pay(Comanda comanda);

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getPassword() { return password; }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "username='" + username + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator that)) return false;
        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
