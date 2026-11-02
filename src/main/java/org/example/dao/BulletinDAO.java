package org.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.modele.Bulletin;
import org.example.modele.Note;
import org.example.utils.DataBase;

public class BulletinDAO {

    private final NoteDAO noteDAO = new NoteDAO();

    // ============================================================
    // Bulletin d'un étudiant
    // ============================================================
    public Optional<Bulletin> bulletinParEtudiant(int etudiantId) throws SQLException {
        String sql = "SELECT id, nom, email, filiere, niveau "
                + "FROM etudiants WHERE id = ?";

        try (Connection conn = DataBase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, etudiantId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next())
                    return Optional.empty();

                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String email = rs.getString("email");
                String filiere = rs.getString("filiere");
                String niveau = rs.getString("niveau");

                // Récupérer les notes de l'étudiant
                List<Note> notes = noteDAO.notesParEtudiant(id);

                // Construire le bulletin AVEC les notes
                Bulletin b = new Bulletin(id, nom, email, filiere, niveau, notes);
                return Optional.of(b);
            }
        }
    }

    public Optional<Bulletin> bulletinDe(int etudiantId) throws SQLException {
        return bulletinParEtudiant(etudiantId);
    }

    // ============================================================
    // Tous les bulletins
    // ============================================================
    public List<Bulletin> tousLesBulletins() throws SQLException {
        List<Bulletin> liste = new ArrayList<>();
        String sql = "SELECT id, nom, email, filiere, niveau "
                + "FROM etudiants ORDER BY nom";

        try (Connection conn = DataBase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String email = rs.getString("email");
                String filiere = rs.getString("filiere");
                String niveau = rs.getString("niveau");

                List<Note> notes = noteDAO.notesParEtudiant(id);
                liste.add(new Bulletin(id, nom, email, filiere, niveau, notes));
            }
        }
        return liste;
    }
}