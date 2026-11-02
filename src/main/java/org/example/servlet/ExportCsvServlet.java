package org.example.servlet;

import org.example.dao.CoursDAO;
import org.example.dao.NoteDAO;
import org.example.modele.Cours;
import org.example.modele.Note;
import org.example.service.BulletinService;
import org.example.modele.Bulletin;
import org.example.utils.CsvUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/export/*")
public class ExportCsvServlet extends HttpServlet {

    private final CoursDAO coursDAO = new CoursDAO();
    private final NoteDAO noteDAO = new NoteDAO();
    private final BulletinService bulletinService = new BulletinService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();
        if (path == null) { resp.setStatus(404); return; }

        try {
            switch (path) {
                case "/cours":
                    exportCours(resp);
                    return;
                case "/notes":
                    exportNotes(resp);
                    return;
                case "/bulletins":
                    // TODO : boucle sur tous les étudiants
                    resp.setStatus(501);
                    return;
                default:
                    if (path.startsWith("/bulletin/")) {
                        int id = Integer.parseInt(path.substring("/bulletin/".length()));
                        exportBulletin(resp, id);
                        return;
                    }
            }
            resp.setStatus(404);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.setContentType("text/plain; charset=UTF-8");
            resp.getWriter().write("Erreur : " + e.getMessage());
        }
    }

    // ============================================================
    // /api/export/cours → cours.csv
    // ============================================================
    private void exportCours(HttpServletResponse resp) throws Exception {
        prepCsv(resp, "cours.csv");

        try (PrintWriter out = resp.getWriter()) {
            out.write('\uFEFF');
            out.print(CsvUtils.header("code", "intitule", "coefficient", "volume_heures"));

            for (Cours c : coursDAO.tousLesCours()) {
                out.print(CsvUtils.line(
                        c.getCode(), c.getIntitule(), c.getCoefficient(), c.getVolumeHeures()
                ));
            }
        }
    }

    // ============================================================
    // /api/export/notes → notes.csv
    // ============================================================
    private void exportNotes(HttpServletResponse resp) throws Exception {
        prepCsv(resp, "notes.csv");

        try (PrintWriter out = resp.getWriter()) {
            out.write('\uFEFF');
            out.print(CsvUtils.header("etudiant", "cours_code", "cours_intitule",
                    "note", "coefficient", "date"));

            for (Note n : noteDAO.toutesLesNotes()) {
                out.print(CsvUtils.line(
                        n.getEtudiantNom(),
                        n.getCoursCode(),
                        n.getCoursIntitule(),
                        n.getNote(),
                        n.getCoefficient(),
                        n.getDateNote() != null ? n.getDateNote().toString() : ""
                ));
            }
        }
    }

    // ============================================================
    // /api/export/bulletin/5 → bulletin_5.csv
    // ============================================================
    private void exportBulletin(HttpServletResponse resp, int etudiantId) throws Exception {
        var opt = bulletinService.bulletinEtudiant(etudiantId);
        if (opt.isEmpty()) {
            resp.setStatus(404);
            resp.setContentType("text/plain; charset=UTF-8");
            resp.getWriter().write("Étudiant introuvable");
            return;
        }

        Bulletin b = opt.get();
        prepCsv(resp, "bulletin_" + etudiantId + ".csv");

        try (PrintWriter out = resp.getWriter()) {
            out.write('\uFEFF');

            // En-tête étudiant
            out.print(CsvUtils.header("BULLETIN", ""));
            out.print(CsvUtils.line("Étudiant", b.getEtudiantNom()));
            out.print(CsvUtils.line("Email", b.getEmail()));
            out.print(CsvUtils.line("Filière", b.getFiliere()));
            out.print(CsvUtils.line("Niveau", b.getNiveau()));
            out.print(CsvUtils.line("", ""));

            // Notes
            out.print(CsvUtils.header("code", "intitule", "note", "coefficient", "note_ponderee"));

            for (Note n : b.getNotes()) {
                out.print(CsvUtils.line(
                        n.getCoursCode(),
                        n.getCoursIntitule(),
                        n.getNote(),
                        n.getCoefficient(),
                        n.getNote() * n.getCoefficient()
                ));
            }

            // Moyenne
            out.print(CsvUtils.line("", "", "", ""));
            out.print(CsvUtils.line("MOYENNE GENERALE", "", b.getMoyenneGenerale(),
                    b.getTotalCoefficients(), b.getTotalPoints()));
        }
    }

    private void prepCsv(HttpServletResponse resp, String filename) {
        resp.setContentType("text/csv; charset=UTF-8");
        resp.setHeader("Content-Disposition",
                "attachment; filename=\"" + filename + "\"");
    }
}