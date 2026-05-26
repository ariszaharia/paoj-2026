package com.pao.project.Eticketing.repository;

import com.pao.project.Eticketing.model.order.Comanda;
import com.pao.project.Eticketing.model.order.ComandaNormala;
import com.pao.project.Eticketing.model.order.ComandaSenior;
import com.pao.project.Eticketing.model.order.ComandaStudent;
import com.pao.project.Eticketing.model.ticket.Bilet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ComandaRepository implements Repository<Comanda, Integer> {

    private final Connection conn;
    private final BiletRepository biletRepository;

    public ComandaRepository(Connection connection){
        this.conn = connection;
        this.biletRepository = new BiletRepository(connection);
    }

    private Comanda mapRow(ResultSet rs) throws SQLException {
        Comanda c = switch (rs.getString("tip_comanda")) {
            case "STUDENT" -> new ComandaStudent();
            case "SENIOR" -> new ComandaSenior();
            default -> new ComandaNormala();
        };
        c.setId(rs.getInt("comanda_id"));
        c.setClientId(rs.getInt("client_id"));
        return c;
    }

    private void populateComandaWithBilete(Comanda comanda) throws SQLException {
        String sql = "SELECT bilet_id, cantitate FROM comenzi_bilete WHERE comanda_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, comanda.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int biletId = rs.getInt("bilet_id");
                    int cantitate = rs.getInt("cantitate");
                    Optional<Bilet> biletOpt = biletRepository.findById(biletId);
                    if (biletOpt.isPresent()) {
                        Bilet bilet = biletOpt.get();
                        for (int i = 0; i < cantitate; i++) {
                            comanda.adaugaBilet(bilet);
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<Comanda> findAll(){
        List<Comanda> comenzi = new ArrayList<>();
        String sql = "SELECT * FROM comenzi";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Comanda comanda = mapRow(rs);
                populateComandaWithBilete(comanda);
                comenzi.add(comanda);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la gasire Comenzi", e);
        }
        return comenzi;
    }

    @Override
    public Optional<Comanda> findById(Integer id){
        String sql = "SELECT * FROM comenzi WHERE comanda_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Comanda comanda = mapRow(rs);
                    populateComandaWithBilete(comanda);
                    return Optional.of(comanda);
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Eroare la gasire Comanda", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Comanda c) {
        String sqlComanda = "INSERT INTO comenzi (client_id, tip_comanda) VALUES (?, ?)";
        String sqlComenziBilete = "INSERT INTO comenzi_bilete (comanda_id, bilet_id, cantitate) VALUES (?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement psComanda = conn.prepareStatement(sqlComanda, Statement.RETURN_GENERATED_KEYS)) {
                psComanda.setInt(1, c.getClientId());
                psComanda.setString(2, c.getTipComanda());
                psComanda.executeUpdate();

                try (ResultSet generatedKeys = psComanda.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        c.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating comanda failed, no ID obtained.");
                    }
                }
            }

            Map<Integer, Long> biletCounts = c.getBilete().stream()
                    .map(Bilet::getId)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            try (PreparedStatement psComenziBilete = conn.prepareStatement(sqlComenziBilete)) {
                for (Map.Entry<Integer, Long> entry : biletCounts.entrySet()) {
                    psComenziBilete.setInt(1, c.getId());
                    psComenziBilete.setInt(2, entry.getKey());
                    psComenziBilete.setLong(3, entry.getValue());
                    psComenziBilete.addBatch();
                }
                psComenziBilete.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Error rolling back transaction", ex);
            }
            throw new RuntimeException("Error saving comanda", e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Error restoring auto-commit", e);
            }
        }
    }


    @Override
    public void update(Comanda c){
        String sqlDeleteBilete = "DELETE FROM comenzi_bilete WHERE comanda_id = ?";
        String sqlInsertBilete = "INSERT INTO comenzi_bilete (comanda_id, bilet_id, cantitate) VALUES (?, ?, ?)";
        String sqlUpdateComanda = "UPDATE comenzi SET client_id = ? WHERE comanda_id = ?";

        try {
            conn.setAutoCommit(false);

            // Update comanda details
            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateComanda)) {
                psUpdate.setInt(1, c.getClientId());
                psUpdate.setInt(2, c.getId());
                psUpdate.executeUpdate();
            }

            // Delete old bilete
            try (PreparedStatement psDelete = conn.prepareStatement(sqlDeleteBilete)) {
                psDelete.setInt(1, c.getId());
                psDelete.executeUpdate();
            }

            // Insert new bilete
            Map<Integer, Long> biletCounts = c.getBilete().stream()
                    .map(Bilet::getId)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsertBilete)) {
                for (Map.Entry<Integer, Long> entry : biletCounts.entrySet()) {
                    psInsert.setInt(1, c.getId());
                    psInsert.setInt(2, entry.getKey());
                    psInsert.setLong(3, entry.getValue());
                    psInsert.addBatch();
                }
                psInsert.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Error rolling back transaction", ex);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Error restoring auto-commit", e);
            }
        }
    }

    @Override
    public void delete(Integer id){
        String sqlDeleteBilete = "DELETE FROM comenzi_bilete WHERE comanda_id = ?";
        String sqlDeleteComanda = "DELETE FROM comenzi WHERE comanda_id = ?";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlDeleteBilete)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlDeleteComanda)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException("Error rolling back transaction", ex);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Error restoring auto-commit", e);
            }
        }

    }

    public record TranzactieDetaliata(
            int tranzactieId,
            String dataPlata,
            String usernameClient,
            double sumaPlatita,
            int cantitate,
            String tipBilet,
            String numeEveniment,
            String tipEveniment
    ) {}

    public List<TranzactieDetaliata> findToateTranzactiileDetaliate() {
        List<TranzactieDetaliata> rapoarte = new ArrayList<>();

        String sql = "SELECT t.tranzactie_id, t.timestamp, t.client_username, t.suma AS suma_platita, " +
                "cb.cantitate, b.descriere AS tip_bilet, " +
                "e.denumire AS nume_eveniment, e.tip AS tip_eveniment " +
                "FROM tranzactii t " +
                "JOIN comenzi c ON t.comanda_id = c.comanda_id " +
                "JOIN comenzi_bilete cb ON c.comanda_id = cb.comanda_id " +
                "JOIN bilete b ON cb.bilet_id = b.bilet_id " +
                "JOIN evenimente e ON b.eveniment_id = e.eveniment_id " +
                "ORDER BY t.timestamp DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rapoarte.add(new TranzactieDetaliata(
                        rs.getInt("tranzactie_id"),
                        rs.getString("timestamp"),
                        rs.getString("client_username"),
                        rs.getDouble("suma_platita"),
                        rs.getInt("cantitate"),
                        rs.getString("tip_bilet"),
                        rs.getString("nume_eveniment"),
                        rs.getString("tip_eveniment")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la generarea raportului de tranzactii detaliate", e);
        }

        return rapoarte;
    }
}