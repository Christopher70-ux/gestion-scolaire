package org.example.dao;

import org.example.modele.Note;
import org.example.utils.DataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NoteDAO {

    private static final String JOIN_SQL =
            "SELECT n.id, n.etudiant_id, n.cours_id, n.note, n.date_note, " +
                    "e.nom AS etudiant_nom, c.code AS cours_code, c.intitule AS cours_intitule, " +
                    "c.coefficient " +
                    "FROM notes n " +
                    "JOIN etudiants e ON e.id = n.etudiant_id " +
                    "JOIN cours c ON c.id = n.cours_id ";

    public List<Note> toutesLesNotes() throws SQLException {
        List<Note> liste = new ArrayList<>();
        String sql = JOIN_SQL + "ORDER BY e.nom, c.code";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(map(rs));
        }
        return liste;
    }

    public List<Note> notesParEtudiant(int etudiantId) throws SQLException {
        List<Note> liste = new ArrayList<>();
        String sql = JOIN_SQL + "WHERE n.etudiant_id = ? ORDER BY c.code";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, etudiantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) liste.add(map(rs));
            }
        }
        return liste;
    }

    public Optional<Note> trouverParEtudiantEtCours(int etudiantId, int coursId) throws SQLException {
        String sql = JOIN_SQL + "WHERE n.etudiant_id = ? AND n.cours_id = ?";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, etudiantId);
            ps.setInt(2, coursId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    public Note ajouter(int etudiantId, int coursId, double note, LocalDate date) throws SQLException {
        String sql = "INSERT INTO notes (etudiant_id, cours_id, note, date_note) VALUES (?, ?, ?, ?)";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, etudiantId);
            ps.setInt(2, coursId);
            ps.setDouble(3, note);
            ps.setDate(4, date != null ? Date.valueOf(date) : null);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Note(keys.getInt(1), etudiantId, coursId, note, date);
                }
            }
        }
        return null;
    }

    private Note map(ResultSet rs) throws SQLException {
        Note n = new Note(
                rs.getInt("id"),
                rs.getInt("etudiant_id"),
                rs.getInt("cours_id"),
                rs.getDouble("note"),
                null
        );
        Date d = rs.getDate("date_note");
        if (d != null) n.setDateNote(d.toLocalDate());
        n.setEtudiantNom(rs.getString("etudiant_nom"));
        n.setCoursCode(rs.getString("cours_code"));
        n.setCoursIntitule(rs.getString("cours_intitule"));
        n.setCoefficient(rs.getInt("coefficient"));
        return n;
    }

    // Trouver une note par son id
    public Optional<Note> trouverParId(int id) throws SQLException {
        String sql = JOIN_SQL + "WHERE n.id = ?";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    // Modifier note + date
    public boolean modifier(int id, double note, LocalDate date) throws SQLException {
        String sql = "UPDATE notes SET note = ?, date_note = ? WHERE id = ?";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, note);
            ps.setDate(2, date != null ? Date.valueOf(date) : null);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Supprimer
    public boolean supprimer(int id) throws SQLException {
        String sql = "DELETE FROM notes WHERE id = ?";
        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

}