package com.pao.project.Eticketing;

import com.pao.project.Eticketing.model.event.Concert;
import com.pao.project.Eticketing.model.event.Eveniment;
import com.pao.project.Eticketing.model.event.Locatie;
import com.pao.project.Eticketing.model.event.Meci;
import com.pao.project.Eticketing.model.order.Comanda;
import com.pao.project.Eticketing.model.ticket.Bilet;
import com.pao.project.Eticketing.model.user.Client;
import com.pao.project.Eticketing.model.user.TipClient;
import com.pao.project.Eticketing.model.user.Utilizator;
import com.pao.project.Eticketing.service.AuthService;
import com.pao.project.Eticketing.service.BiletService;
import com.pao.project.Eticketing.service.ComandaService;
import com.pao.project.Eticketing.service.EvenimentService;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthService authService = AuthService.getInstance();
        EvenimentService evenimentService = EvenimentService.getInstance();
        ComandaService comandaService = ComandaService.getInstance();
        BiletService biletService = BiletService.getInstance();

        Comanda comandaCurenta = null;

        Locatie l1 = new Locatie("Sala Palatului", "Bucuresti", 1000);
        Locatie l2 = new Locatie("Arena Nationala", "Bucuresti", 50000);

        Concert c1 = new Concert("Concert rock", "24.02.2026", 60, l1, "Beatles", "Rock");
        Meci m1 = new Meci("Meci fotbal", "25.02.2026", 90, l2, "FCSB", "Dinamo");
        
        Bilet b1 = new Bilet(200, m1, "VIP");
        Bilet b2 = new Bilet(50, m1, "Peluza");
        Bilet b3 = new Bilet(180, c1, "VIP");
        Bilet b4 = new Bilet(90, c1, "Golden Circle");
        Bilet b5 = new Bilet(100, m1, "Tribuna");
        Bilet b6 = new Bilet(50, c1, "Normal");

        biletService.addBilet(b1);
        biletService.addBilet(b2);
        biletService.addBilet(b3);
        biletService.addBilet(b4);
        biletService.addBilet(b5);
        biletService.addBilet(b6);
        evenimentService.addEveniment(m1);
        evenimentService.addEveniment(c1);

        boolean running = true;
        while (running) {
            Client clientLogat = authService.getLoggedClient();
            if (clientLogat == null) {
                comandaCurenta = null;
            }

            System.out.println("\n=== MENIU ETICKETING ===");
            System.out.println("--- Autentificare ---");
            System.out.println("1. Inregistrare client");
            System.out.println("2. Login");
            System.out.println("3. Logout");
            System.out.println("4. Sterge cont");
            System.out.println("--- Explorare ---");
            System.out.println("5. Afiseaza evenimente (sortate)");
            System.out.println("6. Cauta eveniment dupa denumire");
            System.out.println("7. Afiseaza bilete pentru eveniment");
            System.out.println("8. Cauta bilet dupa descriere");
            System.out.println("--- Comanda ---");
            System.out.println("9. Adauga bilet in comanda curenta");
            System.out.println("10. Afiseaza comanda curenta");
            System.out.println("11. Depoziteaza bani in cont");
            System.out.println("12. Retrage bani");
            System.out.println("13. Plateste comanda curenta");
            System.out.println("--- Administrare ---");
            System.out.println("14. Afiseaza comenzile platite (sortate)");
            System.out.println("15. Sterge comanda platita dupa index");
            System.out.println("16. Sterge eveniment dupa denumire");
            System.out.println("17. Sterge bilet dupa descriere");
            System.out.println("0. Iesire");
            System.out.print("Introduce optiunea: ");
            String opt = scanner.nextLine().trim();

            switch (opt) {
                case "1":
                    try {
                        System.out.print("Username: ");
                        String usernameNou = scanner.nextLine().trim();
                        System.out.print("Parola: ");
                        String parolaNoua = scanner.nextLine().trim();
                        System.out.print("Tip client (NORMAL, STUDENT, SENIOR): ");
                        String tipClientStr = scanner.nextLine().trim().toUpperCase();
                        TipClient tipClient = TipClient.valueOf(tipClientStr);
                        Client clientNou = new Client(usernameNou, parolaNoua, tipClient);
                        authService.register(clientNou);
                        System.out.println("Client inregistrat cu succes.");
                    } catch (Exception e) {
                        System.out.println("Eroare la inregistrare: " + e.getMessage());
                    }
                    break;

                case "2":
                    if(clientLogat == null){
                    try {
                        System.out.print("Username: ");
                        String usernameLogin = scanner.nextLine().trim();
                        System.out.print("Parola: ");
                        String parolaLogin = scanner.nextLine().trim();

                        Utilizator user = authService.login(usernameLogin, parolaLogin);
                        if (user instanceof Client) {
                            clientLogat = (Client) user;
                            comandaCurenta = clientLogat.creeazaComandaAutomata();
                            System.out.println("Login reusit. Salut, " + clientLogat.getUsername() + "!");
                            System.out.println("Sold: " + clientLogat.getBalance() + " lei. Tip client: " + clientLogat.getTipClient());
                        } else if (user != null) {
                            System.out.println("Utilizator logat, dar nu este client. Comenzile sunt disponibile doar clientilor.");
                            authService.logout(user);
                        } else {
                            System.out.println("Credentiale invalide.");
                        }
                    } catch (Exception e) {
                        System.out.println("Eroare la login: " + e.getMessage());
                    }
                    } else {
                        System.out.println("Deja esti logat ca " + clientLogat.getUsername() + ". Fa logout pentru a te loga cu alt cont.");
                    }
                    break;

                case "5":
                    List<Eveniment> evenimente = evenimentService.getEvenimenteSortateNatural();
                    if (evenimente.isEmpty()) {
                        System.out.println("Nu exista evenimente disponibile.");
                    } else {
                        System.out.println("Evenimente disponibile (sortate):");
                        for (int i = 0; i < evenimente.size(); i++) {
                            System.out.println((i + 1) + ". " + evenimente.get(i));
                        }
                    }
                    break;

                case "6":
                    try {
                        System.out.print("Denumire eveniment: ");
                        String denumireCautata = scanner.nextLine().trim();
                        Eveniment gasit = evenimentService.findByDenumire(denumireCautata);
                        if (gasit == null) {
                            System.out.println("Eveniment negasit.");
                        } else {
                            System.out.println("Eveniment gasit: " + gasit);
                        }
                    } catch (Exception e) {
                        System.out.println("Eroare la cautare eveniment: " + e.getMessage());
                    }
                    break;

                case "7":
                    try {
                        List<Eveniment> listaEvenimente = evenimentService.getEvenimente();
                        if (listaEvenimente.isEmpty()) {
                            System.out.println("Nu exista evenimente disponibile.");
                            break;
                        }

                        System.out.println("Alege evenimentul:");
                        for (int i = 0; i < listaEvenimente.size(); i++) {
                            System.out.println((i + 1) + ". " + listaEvenimente.get(i));
                        }

                        System.out.print("Numar eveniment: ");
                        int indexEveniment = Integer.parseInt(scanner.nextLine().trim()) - 1;
                        if (indexEveniment < 0 || indexEveniment >= listaEvenimente.size()) {
                            System.out.println("Index eveniment invalid.");
                            break;
                        }

                        Eveniment evenimentSelectat = listaEvenimente.get(indexEveniment);
                        List<Bilet> bileteFiltrate = biletService.findByEveniment(evenimentSelectat);

                        if (bileteFiltrate.isEmpty()) {
                            System.out.println("Nu exista bilete pentru evenimentul selectat.");
                        } else {
                            System.out.println("Bilete disponibile:");
                            for (int i = 0; i < bileteFiltrate.size(); i++) {
                                Bilet biletAfisat = bileteFiltrate.get(i);
                                System.out.println((i + 1) + ". " + biletAfisat.getDescriere() + " - " + biletAfisat.getPret() + " lei");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Eroare la listarea biletelor: " + e.getMessage());
                    }
                    break;

                case "9":
                    if (clientLogat == null || comandaCurenta == null) {
                        System.out.println("Trebuie sa fii logat ca client ca sa adaugi bilete.");
                        break;
                    }

                    try {
                        List<Eveniment> listaEvenimente = evenimentService.getEvenimente();
                        if (listaEvenimente.isEmpty()) {
                            System.out.println("Nu exista evenimente disponibile.");
                            break;
                        }

                        System.out.println("Alege evenimentul pentru bilet:");
                        for (int i = 0; i < listaEvenimente.size(); i++) {
                            System.out.println((i + 1) + ". " + listaEvenimente.get(i));
                        }

                        System.out.print("Numar eveniment: ");
                        int indexEveniment = Integer.parseInt(scanner.nextLine().trim()) - 1;
                        if (indexEveniment < 0 || indexEveniment >= listaEvenimente.size()) {
                            System.out.println("Index eveniment invalid.");
                            break;
                        }

                        Eveniment evenimentSelectat = listaEvenimente.get(indexEveniment);
                        List<Bilet> bileteFiltrate = biletService.findByEveniment(evenimentSelectat);

                        if (bileteFiltrate.isEmpty()) {
                            System.out.println("Nu exista bilete disponibile pentru evenimentul selectat.");
                            break;
                        }

                        System.out.println("Alege tipul de bilet: ");
                        for (int i = 0; i < bileteFiltrate.size(); i++) {
                            Bilet biletAfisat = bileteFiltrate.get(i);
                            System.out.println((i + 1) + ". " + biletAfisat.getDescriere() + " - " + biletAfisat.getPret() + " lei");
                        }

                        System.out.print("Numar tip bilet: ");
                        int indexBilet = Integer.parseInt(scanner.nextLine().trim()) - 1;
                        if (indexBilet < 0 || indexBilet >= bileteFiltrate.size()) {
                            System.out.println("Index bilet invalid.");
                            break;
                        }

                        Bilet biletSelectat = bileteFiltrate.get(indexBilet);

                        comandaCurenta.adaugaBilet(biletSelectat);
                        System.out.println("Bilet adaugat. Numar bilete in comanda: " + comandaCurenta.getNumarBilete());
                    } catch (Exception e) {
                        System.out.println("Eroare la adaugare bilet: " + e.getMessage());
                    }
                    break;

                case "11":
                    if (clientLogat == null) {
                        System.out.println("Trebuie sa fii logat ca client pentru a depune bani.");
                        break;
                    }

                    try {
                        System.out.print("Suma de depus: ");
                        double sumaDepusa = Double.parseDouble(scanner.nextLine().trim());
                        clientLogat.deposit(sumaDepusa);
                        System.out.println("Depunere reusita. Sold curent: " + clientLogat.getBalance());
                    } catch (Exception e) {
                        System.out.println("Eroare la depunere: " + e.getMessage());
                    }
                    break;

                case "13":
                    if (clientLogat == null || comandaCurenta == null) {
                        System.out.println("Trebuie sa fii logat ca client si sa ai o comanda.");
                        break;
                    }
                    if (comandaCurenta.getNumarBilete() == 0) {
                        System.out.println("Comanda este goala. Adauga bilete mai intai.");
                        break;
                    }

                    try {
                        double total = comandaCurenta.calculeazaPretTotal();
                        clientLogat.pay(comandaCurenta);
                        comandaService.addComanda(comandaCurenta);
                        System.out.println("Plata reusita. Total platit: " + total);
                        System.out.println("Sold ramas: " + clientLogat.getBalance());
                        comandaCurenta = clientLogat.creeazaComandaAutomata();
                    } catch (Exception e) {
                        System.out.println("Eroare la plata: " + e.getMessage());
                    }
                    break;

                case "10":
                    if (comandaCurenta == null) {
                        System.out.println("Nu exista comanda activa.");
                    } else {
                        System.out.println("Comanda curenta: " + comandaCurenta);
                        System.out.println("Total de plata: " + comandaCurenta.calculeazaPretTotal() + " lei");
                    }
                    break;

                case "14":
                    try {
                        List<Comanda> comenziSortate = comandaService.findComenziSortateDupaTotal();
                        if (comenziSortate.isEmpty()) {
                            System.out.println("Nu exista comenzi platite.");
                        } else {
                            System.out.println("Comenzi platite (sortate dupa total):");
                            for (int i = 0; i < comenziSortate.size(); i++) {
                                System.out.println((i + 1) + ". " + comenziSortate.get(i));
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Eroare la afisarea comenzilor: " + e.getMessage());
                    }
                    break;

                case "15":
                    try {
                        List<Comanda> toateComenzile = comandaService.findAllComenzi();
                        if (toateComenzile.isEmpty()) {
                            System.out.println("Nu exista comenzi de sters.");
                            break;
                        }

                        System.out.println("Comenzi curente:");
                        for (int i = 0; i < toateComenzile.size(); i++) {
                            System.out.println((i + 1) + ". " + toateComenzile.get(i));
                        }

                        System.out.print("Numar comanda de sters: ");
                        int indexComanda = Integer.parseInt(scanner.nextLine().trim()) - 1;
                        boolean stearsa = comandaService.deleteComandaByIndex(indexComanda);
                        if (stearsa) {
                            System.out.println("Comanda stearsa cu succes.");
                        } else {
                            System.out.println("Index invalid. Nicio comanda stearsa.");
                        }
                    } catch (Exception e) {
                        System.out.println("Eroare la stergere comanda: " + e.getMessage());
                    }
                    break;

                case "3":
                    try {
                        authService.logout(clientLogat);
                        comandaCurenta = null;
                        System.out.println("Logout reusit.");
                    } catch (Exception e) {
                        System.out.println("Eroare la logout: " + e.getMessage());
                    }
                    break;

                case "8":
                    try {
                        System.out.print("Descriere bilet: ");
                        String descriere = scanner.nextLine().trim();
                        Bilet bilet = biletService.findByDescriere(descriere);
                        if (bilet == null) {
                            System.out.println("Bilet negasit.");
                        } else {
                            System.out.println("Bilet gasit: " + bilet);
                        }
                    } catch (Exception e) {
                        System.out.println("Eroare la cautare bilet: " + e.getMessage());
                    }
                    break;

                case "16":
                    try {
                        System.out.print("Denumire eveniment de sters: ");
                        String denumire = scanner.nextLine().trim();
                        Eveniment eveniment = evenimentService.findByDenumire(denumire);
                        if (eveniment == null) {
                            System.out.println("Eveniment negasit.");
                        } else {
                            boolean sters = evenimentService.deleteEveniment(eveniment);
                            System.out.println(sters ? "Eveniment sters." : "Evenimentul nu a putut fi sters.");
                        }
                    } catch (Exception e) {
                        System.out.println("Eroare la stergere eveniment: " + e.getMessage());
                    }
                    break;

                case "17":
                    try {
                        System.out.print("Descriere bilet de sters: ");
                        String descriere = scanner.nextLine().trim();
                        Bilet bilet = biletService.findByDescriere(descriere);
                        if (bilet == null) {
                            System.out.println("Bilet negasit.");
                        } else {
                            boolean sters = biletService.deleteBilet(bilet);
                            System.out.println(sters ? "Bilet sters." : "Biletul nu a putut fi sters.");
                        }
                    } catch (Exception e) {
                        System.out.println("Eroare la stergere bilet: " + e.getMessage());
                    }
                    break;

                case "4":
                    if (clientLogat == null){
                        System.out.println("Trebuie sa fii logat ca client pentru a-ti sterge contul.");
                        break;
                    }
                    System.out.println("Esti sigur(y/n)?: ");
                    String alegere = scanner.nextLine().toUpperCase().trim();
                    if (alegere.equals("Y")) {
                        try {
                            authService.logout(clientLogat);
                            authService.deleteUser(clientLogat);
                            System.out.println("Cont sters cu succes.");
                        } catch (Exception e) {
                            System.out.println("Eroare la stergere cont: " + e.getMessage());
                        }
                    }
                    break;
                
                case "12":
                    if (clientLogat == null) {
                        System.out.println("Trebuie sa fii logat ca client pentru a retrage bani.");
                        break;
                    }

                    try {
                        System.out.print("Suma de retras: ");
                        double sumaRetrasa = Double.parseDouble(scanner.nextLine().trim());
                        clientLogat.withdraw(sumaRetrasa);
                        System.out.println("Retragere reusita. Sold curent: " + clientLogat.getBalance());
                    } catch (Exception e) {
                        System.out.println("Eroare la retragere: " + e.getMessage());
                    }
                    break;
                    
                case "0":
                    System.out.println("Aplicatia se inchide.");
                    running = false;
                    break;
                    
                default:
                    System.out.println("Optiune invalida.");
                    break;
            }
        }

    }
}
