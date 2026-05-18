package com.pao.project.Eticketing.repository;

import com.pao.project.Eticketing.model.event.Concert;
import com.pao.project.Eticketing.model.event.Eveniment;
import com.pao.project.Eticketing.model.event.Locatie;
import com.pao.project.Eticketing.model.event.Meci;
import com.pao.project.Eticketing.model.ticket.Bilet;
import com.pao.project.Eticketing.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BiletRepository implements Repository<Bilet, Integer> {

    private static final String SELECT_WITH_JOINS =
            "SELECT b.id, b.pret, b.descriere, b.comanda_id, " +
            "e.id as ev_id, e.denumire as ev_denumire, e.data, e.durata_minute, e.av_tickets, e.tip, " +
            "e.artist, e.gen_muzical, e.echipa_gazda, e.echipa_oaspete, " +
            "l.id as loc_id, l.denumire as loc_denumire, l.oras, l.capacitate " +
            "FROM bilete b " +
            "JOIN evenimente e ON b.eveniment_id = e.id " +
            "JOIN locatii l ON e.locatie_id = l.id";

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Bilet mapRow(ResultSet rs) throws SQLException {
        Locatie locatie = new Locatie(rs.getString("loc_denumire"), rs.getString("oras"), rs.getInt("capacitate"));
        locatie.setId(rs.getInt("loc_id"));

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
        ev.setId(rs.getInt("ev_id"));

        Bilet b = new Bilet(rs.getDouble("pret"), ev, rs.getString("descriere"));
        b.setId(rs.getInt("id"));
        b.setComandaId(rs.getInt("comanda_id"));
        return b;
    }

    @Override
    public List<Bilet> findAll() {
        List<Bilet> bilete = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(SELECT_WITH_JOINS);
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
        String sql = SELECT_WITH_JOINS + " WHERE b.id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
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
        String sql = "INSERT INTO bilete (pret, descriere, eveniment_id, comanda_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setDouble(1, b.getPret());
            ps.setString(2, b.getDescriere());
            ps.setInt(3, b.getEveniment().getId());
            if (b.getComandaId() == 0) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, b.getComandaId());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Bilet b) {
        String sql = "UPDATE bilete SET comanda_id = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            if (b.getComandaId() == 0) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, b.getComandaId());
            }
            ps.setInt(2, b.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM bilete WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
