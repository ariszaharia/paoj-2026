package com.pao.project.Eticketing.repository;

import com.pao.project.Eticketing.model.event.Concert;
import com.pao.project.Eticketing.model.event.Eveniment;
import com.pao.project.Eticketing.model.event.Locatie;
import com.pao.project.Eticketing.model.event.Meci;
import com.pao.project.Eticketing.model.ticket.Bilet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BiletRepository implements Repository<Bilet, Integer> {

    // Am actualizat numele coloanelor PK (bilet_id, eveniment_id, locatie_id)
    // Am eliminat comanda_id
    private static final String SELECT_WITH_JOINS =
            "SELECT b.bilet_id, b.pret, b.descriere, " +
                    "e.eveniment_id, e.denumire as ev_denumire, e.data, e.durata_minute, e.av_tickets, e.tip, " +
                    "e.artist, e.gen_muzical, e.echipa_gazda, e.echipa_oaspete, " +
                    "l.locatie_id, l.denumire as loc_denumire, l.oras, l.capacitate " +
                    "FROM bilete b " +
                    "JOIN evenimente e ON b.eveniment_id = e.eveniment_id " +
                    "JOIN locatii l ON e.locatie_id = l.locatie_id";

    private final Connection conn;

    public BiletRepository(Connection conn) {
        this.conn = conn;
    }

    private Bilet mapRow(ResultSet rs) throws SQLException {
        Locatie locatie = new Locatie(rs.getString("loc_denumire"), rs.getString("oras"), rs.getInt("capacitate"));
        locatie.setId(rs.getInt("locatie_id")); // Actualizat

        Eveniment ev;
        if ("CONCERT".equals(rs.getString("tip"))) {
            ev = new Concert(rs.getString("ev_denumire"), rs.getString("data"),
                    rs.getInt("durata_minute"), locatie,
                    rs.getString("artist"), rs.getString("gen_muzical"),
                    rs.getInt("av_tickets"));
        } else {
            ev = new Meci(rs.getString("ev_denumire"), rs.getString("data"),
                    rs.getInt("durata_minute"), locatie,
                    rs.getString("echipa_gazda"), rs.getString("echipa_oaspete"),
                    rs.getInt("av_tickets"));
        }
        ev.setId(rs.getInt("eveniment_id")); // Actualizat

        Bilet b = new Bilet(rs.getDouble("pret"), ev, rs.getString("descriere"));
        b.setId(rs.getInt("bilet_id")); // Actualizat

        // b.setComandaId() A FOST STERS
        return b;
    }

    @Override
    public List<Bilet> findAll() {
        List<Bilet> bilete = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_WITH_JOINS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                bilete.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea biletelor", e);
        }
        return bilete;
    }

    @Override
    public Optional<Bilet> findById(Integer id) {
        // Am actualizat b.id -> b.bilet_id
        String sql = SELECT_WITH_JOINS + " WHERE b.bilet_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea biletului cu id " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Bilet b) {
        // Am scos comanda_id. Salvam doar datele din catalog
        String sql = "INSERT INTO bilete (pret, descriere, eveniment_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, b.getPret());
            ps.setString(2, b.getDescriere());
            ps.setInt(3, b.getEveniment().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea biletului", e);
        }
    }

    @Override
    public void update(Bilet b) {
        // Deoarece nu mai ai comanda_id, update-ul modifica descrierea sau pretul
        String sql = "UPDATE bilete SET pret = ?, descriere = ?, eveniment_id = ? WHERE bilet_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, b.getPret());
            ps.setString(2, b.getDescriere());
            ps.setInt(3, b.getEveniment().getId());
            ps.setInt(4, b.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea biletului", e);
        }
    }

    @Override
    public void delete(Integer id) {
        // Actualizat id -> bilet_id
        String sql = "DELETE FROM bilete WHERE bilet_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea biletului", e);
        }
    }

    public record BiletOrasInfo(
            String tipBilet,
            double pret,
            String numeEveniment,
            String dataEveniment,
            String numeLocatie,
            String oras
    ) {}

    public List<BiletOrasInfo> findBileteByOras(String orasCautat) {
        List<BiletOrasInfo> rezultate = new ArrayList<>();

        String sql = "SELECT b.descriere AS tip_bilet, b.pret, " +
                "e.denumire AS nume_eveniment, e.data AS data_eveniment, " +
                "l.denumire AS nume_locatie, l.oras " +
                "FROM bilete b " +
                "JOIN evenimente e ON b.eveniment_id = e.eveniment_id " +
                "JOIN locatii l ON e.locatie_id = l.locatie_id " +
                "WHERE l.oras = ? " +
                "ORDER BY e.data ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, orasCautat);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rezultate.add(new BiletOrasInfo(
                            rs.getString("tip_bilet"),
                            rs.getDouble("pret"),
                            rs.getString("nume_eveniment"),
                            rs.getString("data_eveniment"),
                            rs.getString("nume_locatie"),
                            rs.getString("oras")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea biletelor in orasul: " + orasCautat, e);
        }

        return rezultate;
    }
}