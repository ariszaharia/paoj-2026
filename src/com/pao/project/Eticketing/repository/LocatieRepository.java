package com.pao.project.Eticketing.repository;

import com.pao.project.Eticketing.model.event.Locatie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocatieRepository implements Repository<Locatie, Integer> {

    private final Connection conn;

    public LocatieRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Locatie> findAll() {
        List<Locatie> locatii = new ArrayList<>();
        String sql = "SELECT * FROM locatii";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Locatie l = new Locatie(rs.getString("denumire"), rs.getString("oras"), rs.getInt("capacitate"));
                l.setId(rs.getInt("locatie_id"));
                locatii.add(l);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea locatiilor din baza de date", e);
        }
        return locatii;
    }

    @Override
    public Optional<Locatie> findById(Integer id) {
        String sql = "SELECT * FROM locatii WHERE locatie_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Locatie l = new Locatie(rs.getString("denumire"), rs.getString("oras"), rs.getInt("capacitate"));
                    l.setId(rs.getInt("locatie_id"));
                    return Optional.of(l);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea locatiei cu id " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Locatie l) {
        String sql = "INSERT INTO locatii (denumire, oras, capacitate) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, l.getDenumire());
            ps.setString(2, l.getOras());
            ps.setInt(3, l.getCapacitate());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    l.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Locatie l) {
        String sql = "UPDATE locatii SET oras = ? WHERE locatie_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, l.getOras());
            ps.setInt(2, l.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM locatii WHERE locatie_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}