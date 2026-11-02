package org.example.service;

import org.example.dao.EtudiantsDAO;
import org.example.dao.NoteDAO;
import org.example.modele.Bulletin;
import org.example.modele.Etudiants;
import org.example.modele.Note;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BulletinService {

    private final EtudiantsDAO etudiantDAO = new EtudiantsDAO();
    private final NoteDAO noteDAO = new NoteDAO();

    public Optional<Bulletin> bulletinEtudiant(int etudiantId) throws SQLException {
        Optional<Etudiants> opt = etudiantDAO.trouverParId(etudiantId);
        if (opt.isEmpty()) return Optional.empty();

        Etudiants e = opt.get();
        List<Note> notes = noteDAO.notesParEtudiant(etudiantId);

        return Optional.of(new Bulletin(
                e.getId(), e.getNom(), e.getEmail(), e.getFiliere(), e.getNiveau(), notes
        ));
    }
}