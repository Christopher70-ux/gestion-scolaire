package org.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.example.modele.Etudiants;
import org.example.utils.DataBase;

public class EtudiantsDAO {

    // ============================================================
    // SELECT — Tous les Etudiants
    // ============================================================
    public List<Etudiants> tousLesEtudiants() throws SQLException {
        List<Etudiants> liste = new ArrayList<>();
        String sql = "SELECT id, nom, email, telephone, filiere, niveau, date_inscription FROM etudiants ORDER BY nom";

        try (Connection conn = DataBase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                liste.add(mapResultSet(rs));
            }
        }
        return liste;
    }

    // ============================================================
    // SELECT — Recherche par nom ou email (LIKE)
    // ============================================================
    public List<Etudiants> rechercher(String motCle) throws SQLException {
        List<Etudiants> liste = new ArrayList<>();
        String sql = "SELECT id, nom, email, telephone, filiere, niveau FROM etudiants "
                + "WHERE LOWER(nom) LIKE ? OR LOWER(email) LIKE ? OR LOWER(filiere) LIKE ? OR LOWER(niveau) LIKE ? "
                + "ORDER BY nom";

        String pattern = "%" + (motCle == null ? "" : motCle.toLowerCase()) + "%";

        try (Connection conn = DataBase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(mapResultSet(rs));
                }
            }
        }
        return liste;
    }

    // ============================================================
    // SELECT — Un Etudiants par id
    // ============================================================
    public Optional<Etudiants> trouverParId(int id) throws SQLException {
        String sql = "SELECT id, nom, email, telephone, filiere, niveau FROM etudiants WHERE id = ?";

        try (Connection conn = DataBase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        }
        return Optional.empty();
    }

    // ============================================================
    // UPDATE — Modifier un Etudiants
    // ============================================================
    public boolean modifier(Etudiants m) throws SQLException {
        String sql = "UPDATE etudiants SET nom = ?, email = ?, telephone = ?, filiere = ?, niveau = ? "
                + "WHERE id = ?";

        try (Connection conn = DataBase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, m.getNom());
            ps.setString(2, m.getEmail());
            ps.setString(3, m.getTelephone());
            ps.setString(4, m.getFiliere());
            ps.setString(5, m.getNiveau());
            ps.setInt(6, m.getId());

            int lignes = ps.executeUpdate();
            return lignes > 0; // true si modification effectuée
        }
    }

    // ============================================================
    // DELETE — Supprimer un Etudiants
    // ============================================================
    public boolean supprimer(int id) throws SQLException {
        String sql = "DELETE FROM etudiants WHERE id = ?";

        try (Connection conn = DataBase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int lignes = ps.executeUpdate();
            return lignes > 0;
        }
    }

    // ============================================================
    // INSERT (pour peupler la base)
    // ============================================================
    public Etudiants ajouter(Etudiants m) throws SQLException {
        String sql = "INSERT INTO etudiants (nom, email, telephone, filiere, niveau) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DataBase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, m.getNom());
            ps.setString(2, m.getEmail());
            ps.setString(3, m.getTelephone());
            ps.setString(4, m.getFiliere());
            ps.setString(5, m.getNiveau());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next())
                    m.setId(keys.getInt(1));
            }
        }
        return m;
    }

    // ============================================================
    // Utilitaire : ResultSet → Etudiants
    // ============================================================
    private Etudiants mapResultSet(ResultSet rs) throws SQLException {
        return new Etudiants(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("email"),
                rs.getString("telephone"),
                rs.getString("filiere"),
                rs.getString("niveau"));
    }

    // Vérifier si un email existe déjà
    public boolean emailExiste(String email) throws SQLException {
        String sql = "SELECT 1 FROM etudiants WHERE email = ? LIMIT 1";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Trouver par email
    public Optional<Etudiants> trouverParEmail(String email) throws SQLException {
        String sql = "SELECT * FROM etudiants WHERE email = ?";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        }
        return Optional.empty();
    }
}