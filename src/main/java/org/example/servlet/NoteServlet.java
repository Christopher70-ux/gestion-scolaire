package org.example.servlet;

import com.google.gson.Gson;
import org.example.dao.NoteDAO;
import org.example.modele.Note;
import org.example.utils.GsonConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/notes/*")
public class NoteServlet extends HttpServlet {

    private final NoteDAO dao = new NoteDAO();
    private final Gson gson = GsonConfig.create();

    // ============================================================
    // GET — liste, recherche, détail
    // ============================================================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");
        String pathInfo = req.getPathInfo();

        try {
            // /api/notes/5 → une note
            if (pathInfo != null && pathInfo.length() > 1) {
                int id = Integer.parseInt(pathInfo.substring(1));
                var opt = dao.trouverParId(id);
                if (opt.isEmpty()) {
                    resp.setStatus(404);
                    writeJson(resp, Map.of("success", false, "message", "Note introuvable"));
                    return;
                }
                writeJson(resp, Map.of("success", true, "data", opt.get()));
                return;
            }

            // /api/notes → toutes les notes (avec recherche optionnelle)
            List<Note> liste = dao.toutesLesNotes();

            Map<String, Object> json = new HashMap<>();
            json.put("success", true);
            json.put("data", liste);
            json.put("count", liste.size());
            writeJson(resp, json);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            writeJson(resp, Map.of("success", false,
                    "message", e.getMessage() != null ? e.getMessage() : "Erreur serveur"));
        }
    }

    // ============================================================
    // POST — ajouter une note
    // ============================================================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        try {
            int etudiantId = Integer.parseInt(req.getParameter("etudiantId"));
            int coursId = Integer.parseInt(req.getParameter("coursId"));
            double note = Double.parseDouble(req.getParameter("note"));

            String dateStr = req.getParameter("dateNote");
            LocalDate date = (dateStr != null && !dateStr.isEmpty())
                    ? LocalDate.parse(dateStr) : null;

            // Vérifier doublon (1 note par étudiant/cours)
            if (dao.trouverParEtudiantEtCours(etudiantId, coursId).isPresent()) {
                resp.setStatus(400);
                writeJson(resp, Map.of("success", false,
                        "message", "Cet étudiant a déjà une note pour ce cours"));
                return;
            }

            Note n = dao.ajouter(etudiantId, coursId, note, date);

            Map<String, Object> json = new HashMap<>();
            json.put("success", true);
            json.put("message", "Note ajoutée");
            json.put("id", n.getId());
            json.put("data", n);
            writeJson(resp, json);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            writeJson(resp, Map.of("success", false,
                    "message", e.getMessage() != null ? e.getMessage() : "Erreur serveur"));
        }
    }

    // ============================================================
    // PUT — modifier (note et date uniquement)
    // ============================================================
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.length() < 2) {
                resp.setStatus(400);
                writeJson(resp, Map.of("success", false, "message", "ID manquant"));
                return;
            }

            int id = Integer.parseInt(pathInfo.substring(1));

            if (dao.trouverParId(id).isEmpty()) {
                resp.setStatus(404);
                writeJson(resp, Map.of("success", false, "message", "Note introuvable"));
                return;
            }

            double note = Double.parseDouble(req.getParameter("note"));
            String dateStr = req.getParameter("dateNote");
            LocalDate date = (dateStr != null && !dateStr.isEmpty())
                    ? LocalDate.parse(dateStr) : null;

            boolean ok = dao.modifier(id, note, date);

            if (ok) {
                writeJson(resp, Map.of("success", true, "message", "Note modifiée"));
            } else {
                resp.setStatus(400);
                writeJson(resp, Map.of("success", false, "message", "Modification impossible"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            writeJson(resp, Map.of("success", false,
                    "message", e.getMessage() != null ? e.getMessage() : "Erreur serveur"));
        }
    }

    // ============================================================
    // DELETE — supprimer
    // ============================================================
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.length() < 2) {
                resp.setStatus(400);
                writeJson(resp, Map.of("success", false, "message", "ID manquant"));
                return;
            }

            int id = Integer.parseInt(pathInfo.substring(1));
            boolean ok = dao.supprimer(id);

            if (ok) {
                writeJson(resp, Map.of("success", true, "message", "Note supprimée"));
            } else {
                resp.setStatus(404);
                writeJson(resp, Map.of("success", false, "message", "Note introuvable"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            writeJson(resp, Map.of("success", false,
                    "message", e.getMessage() != null ? e.getMessage() : "Erreur serveur"));
        }
    }

    private void writeJson(HttpServletResponse resp, Object obj) throws IOException {
        resp.getWriter().write(gson.toJson(obj));
    }
}