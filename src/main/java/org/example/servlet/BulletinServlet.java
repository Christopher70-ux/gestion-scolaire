package org.example.servlet;

import com.google.gson.Gson;
import org.example.dao.BulletinDAO;
import org.example.modele.Bulletin;
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

@WebServlet("/api/bulletins/*")
public class BulletinServlet extends HttpServlet {

    private final BulletinDAO dao = new BulletinDAO();
    private final Gson gson = GsonConfig.create();

    // ============================================================
    // GET — liste ou détail
    // ============================================================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");
        String pathInfo = req.getPathInfo();

        try {
            // /api/bulletins/1 → bulletin d'un étudiant
            if (pathInfo != null && pathInfo.length() > 1) {
                int id = Integer.parseInt(pathInfo.substring(1));
                var opt = dao.bulletinParEtudiant(id);
                if (opt.isEmpty()) {
                    resp.setStatus(404);
                    writeJson(resp, Map.of("success", false, "message", "Étudiant introuvable"));
                    return;
                }
                writeJson(resp, Map.of("success", true, "data", opt.get()));
                return;
            }

            // /api/bulletins → tous les bulletins
            List<Bulletin> liste = dao.tousLesBulletins();

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

    private void writeJson(HttpServletResponse resp, Object obj) throws IOException {
        resp.getWriter().write(gson.toJson(obj));
    }
}