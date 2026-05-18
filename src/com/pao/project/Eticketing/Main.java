package com.pao.project.Eticketing;

import com.pao.project.Eticketing.model.order.Tranzactie;
import com.pao.project.Eticketing.model.event.Concert;
import com.pao.project.Eticketing.model.event.Eveniment;
import com.pao.project.Eticketing.model.event.Locatie;
import com.pao.project.Eticketing.model.event.Meci;
import com.pao.project.Eticketing.model.order.Comanda;
import com.pao.project.Eticketing.model.ticket.Bilet;
import com.pao.project.Eticketing.model.user.Client;
import com.pao.project.Eticketing.model.user.Organizator;
import com.pao.project.Eticketing.model.user.TipClient;
import com.pao.project.Eticketing.model.user.Utilizator;
import com.pao.project.Eticketing.service.AuthService;
import com.pao.project.Eticketing.service.BiletService;
import com.pao.project.Eticketing.service.ComandaService;
import com.pao.project.Eticketing.service.EvenimentService;
import com.pao.project.Eticketing.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnection conn = DatabaseConnection.getInstance();
            System.out.println("Conexiune la baza de date realizata cu succes!");

        } catch (Exception e) {
            System.out.println("Eroare la conectarea la baza de date: " + e.getMessage());
            return;
        }
        finally {
            DatabaseConnection.closeConnection(DatabaseConnection.getInstance().getConnection());
        }
    }
}