package com.pao.project.Eticketing.repository;

import com.pao.project.Eticketing.model.event.Concert;
import com.pao.project.Eticketing.model.event.Eveniment;
import com.pao.project.Eticketing.model.event.Locatie;
import com.pao.project.Eticketing.model.event.Meci;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EvenimentRepository implements Repository<Eveniment, Integer> {

    private static final String SELECT_WITH_LOCATIE =
            "SELECT e.*, l.locatie_id as loc_id, l.denumire as loc_denumire, l.oras, l.capacitate " +
            "FROM evenimente e JOIN locatii l ON e.locatie_id = l.locatie_id";

    private final Connection conn;

    public EvenimentRepository(Connection conn) {
        this.conn = conn;
    }

    private Eveniment mapRow(ResultSet rs) throws SQLException {
        Locatie locatie = new Locatie(rs.getString("loc_denumire"), rs.getString("oras"), rs.getInt("capacitate"));
        locatie.setId(rs.getInt("loc_id"));

        String tip = rs.getString("tip");
        Eveniment e;
        if ("CONCERT".equals(tip)) {
            e = new Concert(rs.getString("denumire"), rs.getString("data"),
                    rs.getInt("durata_minute"), locatie,
                    rs.getString("artist"), rs.getString("gen_muzical"),
                    rs.getInt("av_tickets"));
        } else {
            e = new Meci(rs.getString("denumire"), rs.getString("data"),
                    rs.getInt("durata_minute"), locatie,
                    rs.getString("echipa_gazda"), rs.getString("echipa_oaspete"),
                    rs.getInt("av_tickets"));
        }
        e.setId(rs.getInt("eveniment_id"));
        return e;
    }

    @Override
    public List<Eveniment> findAll() {
        List<Eveniment> evenimente = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_WITH_LOCATIE);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                evenimente.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea evenimentelor", e);
        }
        return evenimente;
    }

    @Override
    public Optional<Eveniment> findById(Integer id) {
        String sql = SELECT_WITH_LOCATIE + " WHERE e.eveniment_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea evenimentului cu id " + id, e);
        }
        return Optional.empty();
    }

    public Optional<Eveniment> findByDenumire(String denumire) {
        String sql = SELECT_WITH_LOCATIE + " WHERE e.denumire = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, denumire);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea evenimentului cu denumirea " + denumire, e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Eveniment ev) {
        String sql = "INSERT INTO evenimente (denumire, data, durata_minute, av_tickets, tip, locatie_id, artist, gen_muzical, echipa_gazda, echipa_oaspete) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ev.getDenumire());
            ps.setString(2, ev.getData());
            ps.setInt(3, ev.getDurataMinute());
            ps.setInt(4, ev.getAvTickets());
            ps.setInt(6, ev.getLocatie().getId());
            if (ev instanceof Concert c) {
                ps.setString(5, "CONCERT");
                ps.setString(7, c.getArtist());
                ps.setString(8, c.getGenMuzical());
                ps.setNull(9, Types.VARCHAR);
                ps.setNull(10, Types.VARCHAR);
            } else if (ev instanceof Meci m) {
                ps.setString(5, "MECI");
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.VARCHAR);
                ps.setString(9, m.getEchipaGazda());
                ps.setString(10, m.getEchipaOaspete());
            }
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    ev.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Eveniment ev) {
        String sql = "UPDATE evenimente SET av_tickets = ? WHERE eveniment_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ev.getAvTickets());
            ps.setInt(2, ev.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM evenimente WHERE eveniment_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Eveniment, Double> findTopNMostProfitableEvents(int n) {
        Map<Eveniment, Double> topEvents = new LinkedHashMap<>();
        String sql = "SELECT e.eveniment_id, SUM(b.pret * cb.cantitate) as total_revenue " +
                     "FROM evenimente e " +
                     "JOIN bilete b ON e.eveniment_id = b.eveniment_id " +
                     "JOIN comenzi_bilete cb ON b.bilet_id = cb.bilet_id " +
                     "GROUP BY e.eveniment_id " +
                     "ORDER BY total_revenue DESC " +
                     "LIMIT ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, n);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int evenimentId = rs.getInt("eveniment_id");
                    double totalRevenue = rs.getDouble("total_revenue");
                    Optional<Eveniment> evenimentOpt = findById(evenimentId);
                    evenimentOpt.ifPresent(eveniment -> topEvents.put(eveniment, totalRevenue));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la gasirea celor mai profitabile evenimente", e);
        }
        return topEvents;
    }
}