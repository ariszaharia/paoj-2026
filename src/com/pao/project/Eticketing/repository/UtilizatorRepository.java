package com.pao.project.Eticketing.repository;

import com.pao.project.Eticketing.model.user.Client;
import com.pao.project.Eticketing.model.user.Organizator;
import com.pao.project.Eticketing.model.user.TipClient;
import com.pao.project.Eticketing.model.user.Utilizator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilizatorRepository {

    private final Connection conn;

    public UtilizatorRepository(Connection conn) {
        this.conn = conn;
    }

    private Utilizator mapRow(ResultSet rs) throws SQLException {
        String tip = rs.getString("tip");
        if ("CLIENT".equals(tip)) {
            TipClient tipClient = TipClient.valueOf(rs.getString("tip_client"));
            Client c = new Client(rs.getString("username"), rs.getString("password"), tipClient);
            c.setId(rs.getInt("utilizator_id"));
            c.setBalance(rs.getDouble("balance"));
            return c;
        } else { // Presupunem ca restul sunt Organizatori
            Organizator o = new Organizator(rs.getString("username"), rs.getString("password"));
            o.setId(rs.getInt("utilizator_id"));
            return o;
        }
    }

    public List<Utilizator> findAll() {
        List<Utilizator> utilizatori = new ArrayList<>();
        String sql = "SELECT * FROM utilizatori";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                utilizatori.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea utilizatorilor", e);
        }
        return utilizatori;
    }

    public Optional<Utilizator> findById(Integer id) {
        String sql = "SELECT * FROM utilizatori WHERE utilizator_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la gasire Utilizator dupa ID", e);
        }
        return Optional.empty();
    }

    public Optional<Utilizator> findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM utilizatori WHERE username = ? AND password = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la gasire Utilizator", e);
        }
        return Optional.empty();
    }

    public void save(Client c) {
        String sql = "INSERT INTO utilizatori (username, password, balance, tip, tip_client) VALUES (?, ?, ?, 'CLIENT', ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getUsername());
            ps.setString(2, c.getPassword());
            ps.setDouble(3, c.getBalance());
            ps.setString(4, c.getTipClient().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Client c) {
        String sql = "UPDATE utilizatori SET balance = ? WHERE utilizator_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, c.getBalance());
            ps.setInt(2, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea clientului", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM utilizatori WHERE utilizator_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}