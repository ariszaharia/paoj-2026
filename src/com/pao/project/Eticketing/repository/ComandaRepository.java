package com.pao.project.Eticketing.repository;

import com.pao.project.Eticketing.model.order.Comanda;
import com.pao.project.Eticketing.model.order.ComandaNormala;
import com.pao.project.Eticketing.model.order.ComandaSenior;
import com.pao.project.Eticketing.model.order.ComandaStudent;
import com.pao.project.Eticketing.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComandaRepository implements Repository<Comanda, Integer> {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Comanda mapRow(ResultSet rs) throws SQLException {
        Comanda c = switch (rs.getString("tip_comanda")) {
            case "STUDENT" -> new ComandaStudent();
            case "SENIOR" -> new ComandaSenior();
            default -> new ComandaNormala();
        };
        c.setId(rs.getInt("id"));
        c.setClientId(rs.getInt("client_id"));
        return c;
    }

    @Override
    public List<Comanda> findAll() {
        List<Comanda> comenzi = new ArrayList<>();
        String sql = "SELECT * FROM comenzi";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                comenzi.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea comenzilor", e);
        }
        return comenzi;
    }

    @Override
    public Optional<Comanda> findById(Integer id) {
        String sql = "SELECT * FROM comenzi WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea comenzii cu id " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Comanda c) {
        String sql = "INSERT INTO comenzi (client_id, tip_comanda) VALUES (?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, c.getClientId());
            ps.setString(2, c.getTipComanda());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Comanda c) {
        String sql = "UPDATE comenzi SET client_id = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, c.getClientId());
            ps.setInt(2, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM comenzi WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
