-- Drop tables in reverse dependency order
DROP TABLE IF EXISTS tranzactii;
DROP TABLE IF EXISTS bilete;
DROP TABLE IF EXISTS comenzi;
DROP TABLE IF EXISTS utilizatori;
DROP TABLE IF EXISTS evenimente;
DROP TABLE IF EXISTS locatii;

-- Locatii
CREATE TABLE locatii (
    id SERIAL PRIMARY KEY,
    denumire VARCHAR(255) NOT NULL,
    oras VARCHAR(255),
    capacitate INT NOT NULL
);


-- Evenimente (tip: 'CONCERT' sau 'MECI')
CREATE TABLE evenimente (
    id SERIAL PRIMARY KEY,
    denumire VARCHAR(255) NOT NULL,
    data VARCHAR(50) NOT NULL,
    durata_minute INT NOT NULL,
    av_tickets INT NOT NULL,
    tip VARCHAR(50) NOT NULL,
    locatie_id INT NOT NULL,
    -- CONCERT
    artist VARCHAR(255),
    gen_muzical VARCHAR(255),
    -- MECI
    echipa_gazda VARCHAR(255),
    echipa_oaspete VARCHAR(255),
    FOREIGN KEY (locatie_id) REFERENCES locatii(id)
);

-- Utilizatori (tip: 'CLIENT' sau 'ORGANIZATOR'; tip_client: 'NORMAL', 'STUDENT', 'SENIOR')
CREATE TABLE utilizatori (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    balance DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    tip VARCHAR(50) NOT NULL,
    tip_client VARCHAR(50)
);

-- Comenzi (tip_comanda: 'NORMALA', 'STUDENT', 'SENIOR')
CREATE TABLE comenzi (
    id SERIAL PRIMARY KEY,
    client_id INT NOT NULL,
    tip_comanda VARCHAR(50) NOT NULL,
    FOREIGN KEY (client_id) REFERENCES utilizatori(id)
);

-- Tranzactii (clasa imutabila — se insereaza, niciodata nu se modifica)
CREATE TABLE tranzactii (
    id SERIAL PRIMARY KEY,
    client_username VARCHAR(255) NOT NULL,
    comanda_id INT NOT NULL,
    suma DOUBLE PRECISION NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    FOREIGN KEY (comanda_id) REFERENCES comenzi(id)
);

-- Bilete
CREATE TABLE bilete (
    id SERIAL PRIMARY KEY,
    pret DOUBLE PRECISION NOT NULL,
    descriere VARCHAR(500),
    eveniment_id INT NOT NULL,
    comanda_id INT,
    FOREIGN KEY (eveniment_id) REFERENCES evenimente(id),
    FOREIGN KEY (comanda_id) REFERENCES comenzi(id)
);
