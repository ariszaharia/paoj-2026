package com.pao.project.Eticketing.repository;

import com.pao.project.Eticketing.model.event.Eveniment;
import com.pao.project.Eticketing.model.event.Locatie;
import com.pao.project.Eticketing.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocatieRepository implements Repository<Locatie, Integer> {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Locatie> findAll(){
        List<Locatie> locatii = new ArrayList<>();
        String sql = "SELECT * FROM locatii";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                Locatie l = new Locatie(rs.getString("denumire"), rs.getString("oras"), rs.getInt("capacitate"));
                l.setId(rs.getInt("id"));
                locatii.add(l);
            }
        }catch(SQLException e){
            throw new RuntimeException("Eroare la preluarea locatiilor din baza de date", e);
        }

        return locatii;
    }
    @Override
    public Optional<Locatie> findById(Integer id) {
        String sql = "SELECT * FROM locatii WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Locatie l = new Locatie(rs.getString("denumire"), rs.getString("oras"), rs.getInt("capacitate"));
                    l.setId(rs.getInt("id"));
                    return Optional.of(l);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la preluarea locatiei cu id " + id, e);
        }
        return Optional.empty();
    }
    @Override
    public void save(Locatie l){
        String sql = "INSERT INTO locatii (denumire, oras, capacitate) VALUES (?, ?, ?)";
        try(PreparedStatement ps = getConn().prepareStatement(sql)){
            ps.setString(1, l.getDenumire());
            ps.setString(2, l.getOras());
            ps.setInt(3, l.getCapacitate());
            ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Locatie l){
        String sql = "UPDATE locatii SET oras = ? WHERE id = ?";
        try(PreparedStatement ps = getConn().prepareStatement(sql)){
            ps.setString(1, l.getOras());
            ps.setInt(2, l.getId());
            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id){
        String sql = "DELETE FROM locatii WHERE id = ?";
        try(PreparedStatement ps = getConn().prepareStatement(sql)){
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
