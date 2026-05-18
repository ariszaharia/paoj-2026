package com.pao.project.Eticketing.repository;

import com.pao.project.Eticketing.model.user.Client;
import com.pao.project.Eticketing.model.user.TipClient;
import com.pao.project.Eticketing.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements Repository<Client, Integer> {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Client mapRow(ResultSet rs) throws SQLException {
        TipClient tipClient = TipClient.valueOf(rs.getString("tip_client"));
        Client c = new Client(rs.getString("username"), rs.getString("password"), tipClient);
        c.setId(rs.getInt("id"));
        c.setBalance(rs.getDouble("balance"));
        return c;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clienti = new ArrayList<>();
        String sql = "SELECT * FROM utilizatori WHERE tip = 'CLIENT'";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clienti.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea clientilor", e);
        }
        return clienti;
    }

    @Override
    public Optional<Client> findById(Integer id) {
        String sql = "SELECT * FROM utilizatori WHERE id = ? AND tip = 'CLIENT'";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea clientului cu id " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Client c) {
        String sql = "INSERT INTO utilizatori (username, password, balance, tip, tip_client) VALUES (?, ?, ?, 'CLIENT', ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, c.getUsername());
            ps.setString(2, c.getPassword());
            ps.setDouble(3, c.getBalance());
            ps.setString(4, c.getTipClient().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Client c) {
        String sql = "UPDATE utilizatori SET balance = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setDouble(1, c.getBalance());
            ps.setInt(2, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM utilizatori WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
