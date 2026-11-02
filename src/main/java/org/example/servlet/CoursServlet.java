package org.example.servlet;

import java.io.IOException;
import java.util.Map;

import org.example.dao.CoursDAO;
import org.example.modele.Cours;
import org.example.utils.GsonConfig;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/cours/*")
public class CoursServlet extends HttpServlet {

    private final CoursDAO dao = new CoursDAO();
    private final Gson gson = GsonConfig.create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");
        String path = req.getPathInfo();

        try {
            if (path != null && path.length() > 1) {
                int id = Integer.parseInt(path.substring(1));
                var opt = dao.trouverParId(id);
                if (opt.isEmpty()) {
                    resp.setStatus(404);
                    write(resp, Map.of("success", false, "message", "Cours introuvable"));
                    return;
                }
                write(resp, Map.of("success", true, "data", opt.get()));
                return;
            }

            var liste = dao.tousLesCours();
            write(resp, Map.of("success", true, "data", liste, "count", liste.size()));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            write(resp, Map.of("success", false, "message", "Erreur : " + e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        try {
            String code = req.getParameter("code");
            String intitule = req.getParameter("intitule");
            int coefficient = Integer.parseInt(req.getParameter("coefficient"));
            int volumeHeures = Integer.parseInt(req.getParameter("volumeHeures"));

            if (code == null || code.isBlank() || intitule == null || intitule.isBlank()) {
                resp.setStatus(400);
                write(resp, Map.of("success", false, "message", "Code et intitulé requis"));
                return;
            }

            if (dao.codeExiste(code)) {
                resp.setStatus(409);
                write(resp, Map.of("success", false, "message", "Ce code de cours existe déjà"));
                return;
            }

            Cours cours = dao.ajouter(new Cours(code, intitule, coefficient, volumeHeures));
            write(resp, Map.of("success", true, "data", cours, "message", "Cours ajouté"));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            write(resp, Map.of("success", false, "message", "Erreur : " + e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        try {
            String path = req.getPathInfo();
            if (path == null || path.length() < 2) {
                resp.setStatus(400);
                write(resp, Map.of("success", false, "message", "ID manquant"));
                return;
            }

            int id = Integer.parseInt(path.substring(1));
            String code = req.getParameter("code");
            String intitule = req.getParameter("intitule");
            int coefficient = Integer.parseInt(req.getParameter("coefficient"));
            int volumeHeures = Integer.parseInt(req.getParameter("volumeHeures"));

            Cours cours = new Cours(id, code, intitule, coefficient, volumeHeures);
            boolean ok = dao.modifier(cours);
            if (!ok) {
                resp.setStatus(404);
                write(resp, Map.of("success", false, "message", "Cours introuvable"));
                return;
            }

            write(resp, Map.of("success", true, "data", cours, "message", "Cours modifié"));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            write(resp, Map.of("success", false, "message", "Erreur : " + e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");

        try {
            String path = req.getPathInfo();
            if (path == null || path.length() < 2) {
                resp.setStatus(400);
                write(resp, Map.of("success", false, "message", "ID manquant"));
                return;
            }

            int id = Integer.parseInt(path.substring(1));
            boolean ok = dao.supprimer(id);
            if (!ok) {
                resp.setStatus(404);
                write(resp, Map.of("success", false, "message", "Cours introuvable"));
                return;
            }

            write(resp, Map.of("success", true, "message", "Cours supprimé"));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            write(resp, Map.of("success", false, "message", "Erreur : " + e.getMessage()));
        }
    }

    private void write(HttpServletResponse resp, Object obj) throws IOException {
        resp.getWriter().write(gson.toJson(obj));
    }
}