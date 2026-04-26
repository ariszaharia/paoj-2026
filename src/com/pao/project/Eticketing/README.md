# E-Ticketing — Proiect Individual PAOJ 2026

**Tema:** Platforma e-ticketing (evenimente, locatii, bilete, clienti) — Tema 10

---

## 1. Actiuni / interogari posibile in sistem

1. Inregistreaza un client nou (cu tip: NORMAL, STUDENT, SENIOR)
2. Autentifica un utilizator in sistem (login cu username si parola)
3. Deconecteaza utilizatorul curent (logout)
4. Sterge contul unui utilizator (cu conditia ca soldul este 0)
5. Afiseaza toate evenimentele disponibile, sortate dupa data
6. Cauta un eveniment dupa denumire
7. Afiseaza biletele disponibile pentru un eveniment selectat
8. Cauta un bilet dupa descriere
9. Adauga un bilet in comanda curenta
10. Plateste comanda curenta (discount automat: 50% studenti, 30% seniori)
11. Afiseaza comanda curenta si totalul de plata
12. Depune bani in contul utilizatorului logat
13. Retrage bani din contul utilizatorului logat
14. Afiseaza toate comenzile platite, sortate dupa total (doar organizator)
15. Sterge o comanda platita dupa index (doar organizator)
16. Sterge un eveniment din sistem (doar organizator)
17. Sterge un bilet din sistem (doar organizator)
18. Afiseaza toti utilizatorii inregistrati (doar organizator)
19. Afiseaza istoricul tuturor tranzactiilor (doar organizator)
20. Cauta un utilizator dupa username (doar organizator)

---

## 2. Tipuri de obiecte din domeniu

| Clasa | Descriere |
|---|---|
| `Eveniment` | Clasa abstracta — eveniment generic cu denumire, data, durata si locatie |
| `Concert` | Extinde `Eveniment` — adauga artist si gen muzical |
| `Meci` | Extinde `Eveniment` — adauga echipa gazda si echipa oaspete |
| `Locatie` | Locatia unui eveniment (denumire, oras, capacitate) |
| `Bilet` | Clasa imutabila — bilet pentru un eveniment, cu pret si descriere (ex: VIP, Peluza) |
| `Tranzactie` | Clasa imutabila — inregistrare read-only a unei plati (client, suma, timestamp) |
| `Utilizator` | Clasa abstracta — utilizator cu username, parola si sold |
| `Client` | Extinde `Utilizator` — client care cumpara bilete, cu tip (NORMAL/STUDENT/SENIOR) |
| `Organizator` | Extinde `Utilizator` — organizator cu acces la functiile de administrare |
| `Comanda` | Clasa abstracta — comanda de bilete cu calcul de pret total |
| `ComandaNormala` | Extinde `Comanda` — comanda fara discount (factor 1.0) |
| `ComandaStudent` | Extinde `Comanda` — comanda cu discount pentru studenti (factor 0.5) |
| `ComandaSenior` | Extinde `Comanda` — comanda cu discount pentru seniori (factor 0.7) |

---

## 3. Arhitectura sistemului

### Ierarhii de mostenire

```
Utilizator (abstract)
├── Client
└── Organizator

Eveniment (abstract, Comparable<Eveniment>)
├── Concert
└── Meci

Comanda (abstract)
├── ComandaNormala
├── ComandaStudent
└── ComandaSenior
```

### Servicii Singleton

| Serviciu | Responsabilitate |
|---|---|
| `AuthService` | Inregistrare, login, logout, stergere cont, cautare utilizatori |
| `EvenimentService` | Adaugare, stergere, cautare si listare evenimente |
| `BiletService` | Adaugare, stergere, cautare bilete dupa descriere sau eveniment |
| `ComandaService` | Procesare plati, istoric comenzi si tranzactii |

### Colectii folosite

| Colectie | Unde | Scop |
|---|---|---|
| `HashMap<String, Utilizator>` | `AuthService` | Indexare utilizatori dupa username |
| `ArrayList<Eveniment>` | `EvenimentService` | Lista evenimente, sortata cu `Collections.sort` via `Comparable` |
| `ArrayList<Bilet>` | `BiletService` | Lista bilete disponibile |
| `ArrayList<Comanda>` | `ComandaService` | Lista comenzi platite, sortabila cu `Comparator` |
| `ArrayList<Tranzactie>` | `ComandaService` | Istoric tranzactii |
| `ArrayList<Bilet>` | `Comanda` | Biletele dintr-o comanda |

### Exceptii custom

| Exceptie | Cand este aruncata |
|---|---|
| `InsufficientFundsException` | Sold insuficient la plata sau retragere |
| `InvalidDeposit` | Suma de depunere negativa sau zero |
| `PretIncorect` | Pret bilet negativ sau zero |
| `EvenimenteException` | Bilete de la evenimente diferite in aceeasi comanda |
| `UserDoesNotExistException` | Stergere utilizator inexistent |
| `StergereUtilizator` | Stergere utilizator cu sold nenul |

---

## 4. Roluri si acces

| Functionalitate | Client | Organizator |
|---|---|---|
| Inregistrare / Login / Logout | DA | DA |
| Vizualizare evenimente si bilete | DA | DA |
| Cumparare bilete | DA | NU |
| Depunere / Retragere bani | DA | NU |
| Administrare comenzi, evenimente, bilete | NU | DA |
| Vizualizare utilizatori si tranzactii | NU | DA |

---

## 5. Cum se ruleaza

La pornire, sistemul inregistreaza automat un cont de organizator cu credentialele:
- **Username:** `admin`
- **Parola:** `admin123`

Si doua evenimente cu bilete predefinite (Concert Rock, Meci fotbal).
