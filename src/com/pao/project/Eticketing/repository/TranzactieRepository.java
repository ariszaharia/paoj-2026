package com.pao.project.Eticketing.repository;

import com.pao.project.Eticketing.model.order.Tranzactie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TranzactieRepository {

    public void save(Connection conn, Tranzactie t) throws SQLException {
        String sql = "INSERT INTO tranzactii (client_username, comanda_id, suma, timestamp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getClientUsername());
            ps.setInt(2, t.getComandaId());
            ps.setDouble(3, t.getSuma());
            ps.setTimestamp(4, Timestamp.valueOf(t.getTimestamp()));
            ps.executeUpdate();
        }
    }
}

