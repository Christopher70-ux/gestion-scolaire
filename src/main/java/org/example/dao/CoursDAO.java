package org.example.dao;

import org.example.modele.Cours;
import org.example.utils.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoursDAO {

    // ============================================================
    // SELECT — tous les cours
    // ============================================================
    public List<Cours> tousLesCours() throws SQLException {
        List<Cours> liste = new ArrayList<>();
        String sql = "SELECT * FROM cours ORDER BY code";

        try (Connection c = DataBase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(map(rs));
        }
        return liste;
    }

    // ============================================================
    // SELECT — un cours par id
    // ============================================================
    public Optional<Cours> trouverParId(int id) throws SQLException {
        String sql = "SELECT * FROM cours WHERE id = ?";

        try (Connection c = DataBase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    // ============================================================
    // Vérifier si un code existe déjà
    // ============================================================
    public boolean codeExiste(String code) throws SQLException {
        String sql = "SELECT 1 FROM cours WHERE code = ? LIMIT 1";

        try (Connection c = DataBase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // ============================================================
    // INSERT
    // ============================================================
    public Cours ajouter(Cours cours) throws SQLException {
        String sql = "INSERT INTO cours (code, intitule, coefficient, volume_heures) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection c = DataBase.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cours.getCode());
            ps.setString(2, cours.getIntitule());
            ps.setInt(3, cours.getCoefficient());
            ps.setInt(4, cours.getVolumeHeures());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) cours.setId(keys.getInt(1));
            }
        }
        return cours;
    }

    // ============================================================
    // UPDATE
    // ============================================================
    public boolean modifier(Cours c) throws SQLException {
        String sql = "UPDATE cours SET code = ?, intitule = ?, coefficient = ?, "
                + "volume_heures = ? WHERE id = ?";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCode());
            ps.setString(2, c.getIntitule());
            ps.setInt(3, c.getCoefficient());
            ps.setInt(4, c.getVolumeHeures());
            ps.setInt(5, c.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // ============================================================
    // DELETE
    // ============================================================
    public boolean supprimer(int id) throws SQLException {
        String sql = "DELETE FROM cours WHERE id = ?";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ============================================================
    // Mapper ResultSet → Cours
    // ============================================================
    private Cours map(ResultSet rs) throws SQLException {
        return new Cours(
                rs.getInt("id"),
                rs.getString("code"),
                rs.getString("intitule"),
                rs.getInt("coefficient"),
                rs.getInt("volume_heures")
        );
    }
}