DROP TABLE IF EXISTS comenzi_bilete; -- <--- Adaugat aici primul!
DROP TABLE IF EXISTS tranzactii;
DROP TABLE IF EXISTS bilete;
DROP TABLE IF EXISTS comenzi;
DROP TABLE IF EXISTS utilizatori;
DROP TABLE IF EXISTS evenimente;
DROP TABLE IF EXISTS locatii;

-- Locatii
CREATE TABLE locatii (
                         locatie_id SERIAL PRIMARY KEY,
                         denumire VARCHAR(255) NOT NULL,
                         oras VARCHAR(255),
                         capacitate INT NOT NULL
);

-- Evenimente
CREATE TABLE evenimente (
                            eveniment_id SERIAL PRIMARY KEY,
                            denumire VARCHAR(255) NOT NULL,
                            data VARCHAR(50) NOT NULL,
                            durata_minute INT NOT NULL,
                            av_tickets INT NOT NULL,
                            tip VARCHAR(50) NOT NULL,
                            locatie_id INT NOT NULL,
                            artist VARCHAR(255),
                            gen_muzical VARCHAR(255),
                            echipa_gazda VARCHAR(255),
                            echipa_oaspete VARCHAR(255),
                            FOREIGN KEY (locatie_id) REFERENCES locatii(locatie_id)
);

-- Utilizatori
CREATE TABLE utilizatori (
                             utilizator_id SERIAL PRIMARY KEY,
                             username VARCHAR(255) NOT NULL UNIQUE,
                             password VARCHAR(255) NOT NULL,
                             balance DOUBLE PRECISION NOT NULL DEFAULT 0.0,
                             tip VARCHAR(50) NOT NULL,
                             tip_client VARCHAR(50)
);

-- Comenzi
CREATE TABLE comenzi (
                         comanda_id SERIAL PRIMARY KEY,
                         client_id INT NOT NULL,
                         tip_comanda VARCHAR(50) NOT NULL,
                         FOREIGN KEY (client_id) REFERENCES utilizatori(utilizator_id)
);

-- Tranzactii
CREATE TABLE tranzactii (
                            tranzactie_id SERIAL PRIMARY KEY,
                            client_username VARCHAR(255) NOT NULL,
                            comanda_id INT NOT NULL,
                            suma DOUBLE PRECISION NOT NULL,
                            timestamp TIMESTAMP NOT NULL,
                            FOREIGN KEY (comanda_id) REFERENCES comenzi(comanda_id)
);

-- Bilete (Catalog)
CREATE TABLE bilete (
                        bilet_id SERIAL PRIMARY KEY,
                        pret DOUBLE PRECISION NOT NULL,
                        descriere VARCHAR(500),
                        eveniment_id INT NOT NULL,
                        FOREIGN KEY (eveniment_id) REFERENCES evenimente(eveniment_id) -- <--- Fara virgula aici!
);

-- Cosul de cumparaturi
CREATE TABLE comenzi_bilete (
                                id SERIAL PRIMARY KEY,
                                comanda_id INT NOT NULL,
                                bilet_id INT NOT NULL,
                                cantitate INT NOT NULL DEFAULT 1,
                                FOREIGN KEY (comanda_id) REFERENCES comenzi(comanda_id),
                                FOREIGN KEY (bilet_id) REFERENCES bilete(bilet_id)
);