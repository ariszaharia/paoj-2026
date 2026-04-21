# Platformă E-Ticketing

**Temă:** Platformă e-ticketing (evenimente, locații, bilete, clienți)  
**Pachet:** `com.pao.project.Eticketing`

---

## 1. Lista cu 10 acțiuni / interogări posibile în sistem

1. **Înregistrează un client nou** — creează un cont cu username, parolă și tip client (NORMAL, STUDENT, SENIOR)
2. **Autentifică un utilizator** — login cu username și parolă; creează automat o comandă activă pentru client
3. **Deautentifică un utilizator** — logout; comanda curentă este resetată
4. **Șterge contul unui client** — elimină utilizatorul din sistem după confirmare
5. **Afișează toate evenimentele disponibile (sortate)** — listează evenimentele ordonate natural după denumire
6. **Caută un eveniment după denumire** — returnează detaliile unui eveniment specific
7. **Afișează biletele disponibile pentru un eveniment** — filtrează biletele după evenimentul selectat
8. **Caută un bilet după descriere** — returnează biletul cu descrierea exactă (ex: "VIP", "Peluza")
9. **Adaugă un bilet în comanda curentă** — clientul logat selectează eveniment și tip bilet
10. **Plătește comanda curentă** — debitează soldul clientului cu prețul total (cu discount aplicat automat după tipul clientului) și salvează comanda

---

## 2. Lista cu cel puțin 8 tipuri de obiecte din domeniu

| Tip | Descriere |
|-----|-----------|
| `Eveniment` | Clasă abstractă de bază pentru toate tipurile de evenimente |
| `Concert` | Eveniment muzical cu artist și gen muzical (extends `Eveniment`) |
| `Meci` | Eveniment sportiv cu echipa gazdă și echipa oaspete (extends `Eveniment`) |
| `Locatie` | Locația unui eveniment (denumire, oraș, capacitate) |
| `Bilet` | Bilet imutabil asociat unui eveniment, cu preț și descriere |
| `Comanda` | Clasă abstractă pentru o comandă de bilete |
| `Client` | Utilizator cu tip client și discount automat (extends `Utilizator`) |
| `Organizator` | Utilizator cu rol de organizator (extends `Utilizator`) |
| `TipClient` | Enum cu factori de discount: NORMAL (1.0), STUDENT (0.5), SENIOR (0.7) |
| `ComandaNormala` / `ComandaStudent` / `ComandaSenior` | Comenzi concrete cu calcul preț specific fiecărui tip |
