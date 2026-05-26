package com.pao.project.Eticketing;

import com.pao.project.Eticketing.model.event.Concert;
import com.pao.project.Eticketing.model.event.Eveniment;
import com.pao.project.Eticketing.model.event.Locatie;
import com.pao.project.Eticketing.model.event.Meci;
import com.pao.project.Eticketing.model.order.Comanda;
import com.pao.project.Eticketing.model.order.ComandaNormala;
import com.pao.project.Eticketing.model.ticket.Bilet;
import com.pao.project.Eticketing.model.user.Client;
import com.pao.project.Eticketing.model.user.Organizator;
import com.pao.project.Eticketing.model.user.TipClient;
import com.pao.project.Eticketing.model.user.Utilizator;
import com.pao.project.Eticketing.repository.BiletRepository;
import com.pao.project.Eticketing.repository.ComandaRepository;
import com.pao.project.Eticketing.repository.EvenimentRepository;
import com.pao.project.Eticketing.repository.LocatieRepository;
import com.pao.project.Eticketing.repository.TranzactieRepository;
import com.pao.project.Eticketing.repository.UtilizatorRepository;
import com.pao.project.Eticketing.service.AuditService;
import com.pao.project.Eticketing.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {

    private static void seedDatabase(LocatieRepository lr, EvenimentRepository er, BiletRepository br) {
        System.out.println("--- SEEDING DATABASE (Introducere date initiale) ---");
        try {
            // 1. Locatii
            Locatie locStadion = new Locatie("Stadionul National", "Bucuresti", 55000);
            Locatie locArene = new Locatie("Arenele Romane", "Bucuresti", 5000);
            Locatie locCluj = new Locatie("Cluj Arena", "Cluj-Napoca", 30000);
            lr.save(locStadion);
            lr.save(locArene);
            lr.save(locCluj);
            System.out.println("Locatii salvate.");

            // 2. Evenimente
            Eveniment meciFinala = new Meci("Finala Cupei", "2024-06-01", 120, locStadion, "FCSB", "CFR Cluj", 50000);
            Eveniment concertRock = new Concert("Rock Fest", "2024-07-15", 240, locArene, "Phoenix", "Rock", 4500);
            Eveniment meciCluj = new Meci("Derby local", "2024-08-20", 120, locCluj, "U Cluj", "CFR Cluj", 28000);
            er.save(meciFinala);
            er.save(concertRock);
            er.save(meciCluj);
            System.out.println("Evenimente salvate.");

            // 3. Bilete
            Bilet biletFinalaTribuna1 = new Bilet(100.0, meciFinala, "Tribuna 1");
            Bilet biletFinalaVIP = new Bilet(300.0, meciFinala, "VIP");
            Bilet biletRockNormal = new Bilet(150.0, concertRock, "Acces General");
            Bilet biletDerbyPeluza = new Bilet(50.0, meciCluj, "Peluza");
            br.save(biletFinalaTribuna1);
            br.save(biletFinalaVIP);
            br.save(biletRockNormal);
            br.save(biletDerbyPeluza);
            System.out.println("Bilete salvate.");
            
            System.out.println("--- SEEDING COMPLET ---");
        } catch (Exception e) {
            System.err.println("Eroare la seeding-ul bazei de date: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void cleanDatabase(Connection conn) {
        System.out.println("\n--- CLEANING DATABASE (Stergere date initiale) ---");

        // Ordinea inversa a dependentelor: stergem copiii, apoi parintii
        String[] tablesToDelete = {
                "tranzactii",      // Depinde de comenzi
                "comenzi_bilete",  // Depinde de comenzi si bilete
                "bilete",          // Depinde de evenimente
                "comenzi",         // Depinde de utilizatori
                "evenimente",      // Depinde de locatii
                "locatii",         // Fara dependente (Tabela de baza)
                "utilizatori"      // Fara dependente (Tabela de baza)
        };

        try (Statement stmt = conn.createStatement()) {
            for (String table : tablesToDelete) {
                // DELETE simplu, pe care orice user de DB are voie sa il faca
                stmt.executeUpdate("DELETE FROM " + table);
            }
            System.out.println("Baza de date a fost curatata cu succes.");
        } catch (SQLException e) {
            System.err.println("Eroare la curatarea bazei de date: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        System.out.println("=== INITIALIZARE CONEXIUNE SI REPOSITORY-URI ===");
        Connection connection = null;
        EvenimentRepository evenimentRepo = null;
        LocatieRepository locatieRepo = null;
        BiletRepository biletRepo = null;
        ComandaRepository comandaRepo = null;
        UtilizatorRepository utilizatorRepo = null;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            evenimentRepo = new EvenimentRepository(connection);
            locatieRepo = new LocatieRepository(connection);
            biletRepo = new BiletRepository(connection);
            comandaRepo = new ComandaRepository(connection);
            utilizatorRepo = new UtilizatorRepository(connection);

            // --- SEEDING ---
            seedDatabase(locatieRepo, evenimentRepo, biletRepo);

            System.out.println("\n=== RULARE SECVENTIALA ETICKETING ===");
            
            String testUsername = "client_" + System.currentTimeMillis();
            String testPassword = "password123";

            // --- 1. Inregistrare client ---
            System.out.println("\n--- 1. Inregistrare client ---");
            try {
                Client clientNou = new Client(testUsername, testPassword, TipClient.NORMAL);
                utilizatorRepo.save(clientNou);
                AuditService.getInstance().logAction("inregistrare_client");
                System.out.println("Client inregistrat cu succes: " + testUsername);
            } catch (Exception e) {
                System.out.println("Eroare la inregistrare: " + e.getMessage());
            }

            // --- 2. Login ---
            System.out.println("\n--- 2. Login ---");
            Client clientLogat = null;
            Comanda comandaCurenta = null;
            try {
                Utilizator user = utilizatorRepo.findByUsernameAndPassword(testUsername, testPassword).orElse(null);
                if (user instanceof Client c) {
                    clientLogat = c;
                    comandaCurenta = new ComandaNormala();
                    comandaCurenta.setClientId(clientLogat.getId());
                    System.out.println("Login reusit. Salut, " + clientLogat.getUsername() + "! Sold: " + clientLogat.getBalance());
                    AuditService.getInstance().logAction("login_client");
                } else {
                    System.out.println("Credentiale invalide.");
                }
            } catch (Exception e) {
                System.out.println("Eroare la login: " + e.getMessage());
            }

            // --- 3. Depoziteaza bani in cont ---
            System.out.println("\n--- 3. Depoziteaza bani in cont ---");
            if (clientLogat != null) {
                try {
                    double sumaDepozit = 500.0;
                    clientLogat.setBalance(clientLogat.getBalance() + sumaDepozit);
                    utilizatorRepo.update(clientLogat);
                    System.out.println("Depozit efectuat cu succes. Noul sold: " + clientLogat.getBalance() + " lei");
                    AuditService.getInstance().logAction("depozitare_bani");
                } catch (Exception e) {
                    System.out.println("Eroare la depozitare: " + e.getMessage());
                }
            }

            // --- 4. Cauta eveniment dupa denumire ---
            System.out.println("\n--- 4. Cauta eveniment dupa denumire ---");
            String denumireCautata = "Finala Cupei";
            Optional<Eveniment> evCautat = evenimentRepo.findByDenumire(denumireCautata);
            if (evCautat.isPresent()) {
                Eveniment e = evCautat.get();
                System.out.println("Eveniment gasit: " + e.getDenumire() + " | Data: " + e.getData() + " | Locatie: " + e.getLocatie().getDenumire());
            } else {
                System.out.println("Nu am gasit niciun eveniment cu denumirea: " + denumireCautata);
            }
            AuditService.getInstance().logAction("cautare_eveniment");

            // --- 5. Afiseaza bilete disponibile (Catalog) ---
            System.out.println("\n--- 5. Afiseaza bilete disponibile (Catalog) ---");
            List<Bilet> catalogBilete = biletRepo.findAll();
            if (catalogBilete.isEmpty()) {
                System.out.println("Nu exista bilete in catalog.");
            } else {
                for (Bilet b : catalogBilete) {
                    System.out.println("ID: " + b.getId() + " | " + b.getDescriere() + " - " + b.getPret() + " lei (Ev: " + b.getEveniment().getDenumire() + ")");
                }
            }
            // Audit the catalog display
            AuditService.getInstance().logAction("afisare_catalog");

            // --- 6. Cauta bilete disponibile intr-un Oras (JOIN) ---
            System.out.println("\n--- 6. Cauta bilete disponibile intr-un Oras (JOIN) ---");
            String orasCautat = "Bucuresti";
            List<BiletRepository.BiletOrasInfo> bileteOras = biletRepo.findBileteByOras(orasCautat);
            if (bileteOras.isEmpty()) {
                System.out.println("Nu am gasit bilete in orasul " + orasCautat);
            } else {
                System.out.println("Bilete in " + orasCautat + ":");
                for (BiletRepository.BiletOrasInfo info : bileteOras) {
                    System.out.printf("Spectacol: %s la %s | Data: %s | Bilet: %s - %.2f RON\n",
                            info.numeEveniment(), info.numeLocatie(), info.dataEveniment(), info.tipBilet(), info.pret());
                }
            }
            AuditService.getInstance().logAction("cautare_bilete_oras");

            // --- 7. Adauga bilet in cosul curent ---
            System.out.println("\n--- 7. Adauga bilet in cosul curent ---");
            if (clientLogat != null && !catalogBilete.isEmpty()) {
                Bilet biletGasit = catalogBilete.get(0);
                comandaCurenta.adaugaBilet(biletGasit);
                System.out.println("Adaugat in cos: 1x " + biletGasit.getDescriere() + ". Cosul contine " + comandaCurenta.getBilete().size() + " bilete.");
                AuditService.getInstance().logAction("adaugare_cos");
            } else {
                System.out.println("Nu se poate adauga bilet (catalog gol sau nelogat).");
            }

            // --- 8. Afiseaza cosul curent ---
            System.out.println("\n--- 8. Afiseaza cosul curent ---");
            if (comandaCurenta == null || comandaCurenta.getBilete().isEmpty()) {
                System.out.println("Cosul este gol.");
            } else {
                double total = comandaCurenta.calculeazaPretTotal();
                System.out.println("--- Cosul Tau ---");
                for (Bilet b : comandaCurenta.getBilete()) {
                    System.out.println("1x " + b.getDescriere() + " = " + b.getPret() + " lei");
                }
                System.out.println("TOTAL DE PLATA: " + total + " lei");
            }
            // Audit the cart display
            AuditService.getInstance().logAction("afisare_cos");

            // --- 9. Plateste comanda curenta (TRANZACTIE JDBC) ---
            System.out.println("\n--- 9. Plateste comanda curenta (TRANZACTIE JDBC) ---");
            if (clientLogat != null && comandaCurenta != null && !comandaCurenta.getBilete().isEmpty()) {
                double totalPlata = comandaCurenta.calculeazaPretTotal();
                if (clientLogat.getBalance() >= totalPlata) {
                    try {
                        clientLogat.setBalance(clientLogat.getBalance() - totalPlata);
                        utilizatorRepo.update(clientLogat);
                        
                        comandaRepo.save(comandaCurenta);

                        // Persist tranzactie in DB so raportul contine inregistrarea platii
                        try {
                            com.pao.project.Eticketing.model.order.Tranzactie tranz =
                                    new com.pao.project.Eticketing.model.order.Tranzactie(clientLogat.getUsername(), comandaCurenta.getId(), totalPlata);
                            new TranzactieRepository().save(connection, tranz);
                        } catch (java.sql.SQLException sqle) {
                            System.err.println("Eroare la salvarea tranzactiei in DB: " + sqle.getMessage());
                        }

                        System.out.println("Plata a fost procesata cu succes! Noul sold: " + clientLogat.getBalance());
                        AuditService.getInstance().logAction("plata_comanda");

                        comandaCurenta = new ComandaNormala();
                        comandaCurenta.setClientId(clientLogat.getId());
                    } catch (Exception e) {
                        System.out.println("Eroare la procesarea platii: " + e.getMessage());
                        clientLogat.setBalance(clientLogat.getBalance() + totalPlata);
                    }
                } else {
                    System.out.println("Fonduri insuficiente pentru a plati " + totalPlata + " lei.");
                }
            } else {
                System.out.println("Nu exista comanda de platit.");
            }

            // --- 10. Logout ---
            System.out.println("\n--- 10. Logout ---");
            clientLogat = null;
            comandaCurenta = null;
            AuditService.getInstance().logAction("logout_utilizator");
            System.out.println("Logout reusit.");

            // --- Actiuni Organizator ---
            System.out.println("\n=== ACTIUNI ORGANIZATOR ===");
            Organizator organizatorLogat = new Organizator("admin", "admin123");
            
            // --- 11. Afiseaza TOP cele mai profitabile evenimente ---
            System.out.println("\n--- 11. Afiseaza TOP cele mai profitabile evenimente ---");
            Map<Eveniment, Double> topEvents = evenimentRepo.findTopNMostProfitableEvents(5);
            if (topEvents.isEmpty()) {
                System.out.println("Nu exista date financiare.");
            } else {
                for (Map.Entry<Eveniment, Double> entry : topEvents.entrySet()) {
                    System.out.printf("Eveniment: %s (ID: %d), Venit Total: %.2f RON\n",
                            entry.getKey().getDenumire(), entry.getKey().getId(), entry.getValue());
                }
            }
            AuditService.getInstance().logAction("raport_profit_admin");

            // --- 12. Afiseaza toti utilizatorii inregistrati ---
            System.out.println("\n--- 12. Afiseaza toti utilizatorii inregistrati ---");
            List<Utilizator> utilizatori = utilizatorRepo.findAll();
            if (utilizatori.isEmpty()) {
                System.out.println("Nu exista utilizatori inregistrati.");
            } else {
                for (Utilizator u : utilizatori) {
                    String detaliiSuplimentare = "";
                    if (u instanceof Client c) {
                        detaliiSuplimentare = " | Tip: " + c.getTipClient() + " | Sold: " + c.getBalance() + " lei";
                    }
                    System.out.println("ID: " + u.getId() + " | Username: " + u.getUsername() + detaliiSuplimentare);
                }
            }
            AuditService.getInstance().logAction("vizualizare_utilizatori");

            // --- 13. Raport Detaliat Tranzactii ---
            System.out.println("\n--- 13. Raport Detaliat Tranzactii ---");
            List<ComandaRepository.TranzactieDetaliata> tranzactii = comandaRepo.findToateTranzactiileDetaliate();
            if (tranzactii.isEmpty()) {
                System.out.println("Nu exista tranzactii inregistrate.");
            } else {
                for (ComandaRepository.TranzactieDetaliata t : tranzactii) {
                    System.out.printf("Tranzactia #%d [%s] - %s a platit %.2f RON pt %d x '%s' la %s\n",
                            t.tranzactieId(), t.dataPlata(), t.usernameClient(), t.sumaPlatita(), t.cantitate(), t.tipBilet(), t.numeEveniment());
                }
            }
            AuditService.getInstance().logAction("raport_tranzactii_admin");

        } catch (Exception e) {
            System.err.println("A aparut o eroare neasteptata in rularea principala: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // --- CLEANUP ---
            if (connection != null) {
                cleanDatabase(connection);
            }
            System.out.println("\n=== RULARE FINALIZATA CU SUCCES ===");
        }
    }
}
