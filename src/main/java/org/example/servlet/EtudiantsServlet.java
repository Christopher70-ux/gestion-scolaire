package org.example.servlet;

import com.google.gson.Gson;
import org.example.modele.Etudiants;
import org.example.service.EtudiantService;
import org.example.utils.GsonConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/etudiants/*")
public class EtudiantsServlet extends HttpServlet {

    private final EtudiantService service = new EtudiantService();
    private final Gson gson = GsonConfig.create();

    // ============================================================
    // GET — liste, recherche, ou détail
    // ============================================================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");
        String pathInfo = req.getPathInfo();

        try {
            // /api/etudiants/5 → un étudiant
            if (pathInfo != null && pathInfo.length() > 1) {
                int id = Integer.parseInt(pathInfo.substring(1));
                var opt = service.trouverParId(id);
                if (opt.isEmpty()) {
                    resp.setStatus(404);
                    writeJson(resp, Map.of("success", false, "message", "Étudiant introuvable"));
                    return;
                }
                writeJson(resp, Map.of("success", true, "data", opt.get()));
                return;
            }

            // /api/etudiants?q=alice → recherche
            String q = req.getParameter("q");
            List<Etudiants> liste = (q != null && !q.isEmpty())
                    ? service.rechercher(q)
                    : service.tousLesEtudiants();

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
    // POST — ajouter (via le Service)
    // ============================================================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        try {
            String nom = req.getParameter("nom");
            String email = req.getParameter("email");
            String telephone = req.getParameter("telephone");
            String filiere = req.getParameter("filiere");
            String niveau = req.getParameter("niveau");

            // Appel au SERVICE
            EtudiantService.Resultat resultat =
                    service.ajouter(nom, email, telephone, filiere, niveau);

            Map<String, Object> json = new HashMap<>();
            json.put("success", resultat.succes);
            json.put("message", resultat.message);

            if (resultat.succes && resultat.etudiant != null) {
                json.put("id", resultat.etudiant.getId());
                json.put("data", resultat.etudiant);
            } else {
                resp.setStatus(400);
            }

            writeJson(resp, json);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", e.getMessage() != null ? e.getMessage() : "Erreur serveur");
            writeJson(resp, err);
        }
    }

    // ============================================================
    // PUT — modifier (via le Service)
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

            EtudiantService.Resultat resultat = service.modifier(
                    id,
                    req.getParameter("nom"),
                    req.getParameter("email"),
                    req.getParameter("telephone"),
                    req.getParameter("filiere"),
                    req.getParameter("niveau")
            );

            if (!resultat.succes) {
                resp.setStatus(400);
            }

            Map<String, Object> json = new HashMap<>();
            json.put("success", resultat.succes);
            json.put("message", resultat.message);
            writeJson(resp, json);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            writeJson(resp, Map.of("success", false,
                    "message", e.getMessage() != null ? e.getMessage() : "Erreur serveur"));
        }
    }

    // ============================================================
    // DELETE — supprimer (via le Service)
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

            EtudiantService.Resultat resultat = service.supprimer(id);

            if (!resultat.succes) {
                resp.setStatus(400);
            }

            Map<String, Object> json = new HashMap<>();
            json.put("success", resultat.succes);
            json.put("message", resultat.message);
            writeJson(resp, json);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            writeJson(resp, Map.of("success", false,
                    "message", e.getMessage() != null ? e.getMessage() : "Erreur serveur"));
        }
    }

    // Utilitaire
    private void writeJson(HttpServletResponse resp, Object obj) throws IOException {
        resp.getWriter().write(gson.toJson(obj));
    }
}