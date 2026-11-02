package org.example.servlet;

import com.google.gson.Gson;
import org.example.service.ConnexionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/connexion")
public class ConnexionServlet extends HttpServlet {

    private final ConnexionService service = new ConnexionService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        Map<String, Object> json = new HashMap<>();

        try {
            String email = req.getParameter("email");
            String motDePasse = req.getParameter("motDePasse");

            ConnexionService.ResultatConnexion r = service.connecter(email, motDePasse);

            if (r.succes) {
                // === CRÉER LA SESSION ===
                HttpSession session = req.getSession(true);
                session.setAttribute("userId", r.utilisateur.getId());
                session.setAttribute("userNom", r.utilisateur.getNom());
                session.setAttribute("userEmail", r.utilisateur.getEmail());
                session.setMaxInactiveInterval(30 * 60);   // 30 min

                json.put("success", true);
                json.put("message", r.message);
                json.put("redirect", req.getContextPath() + "/dashboard");
            } else {
                json.put("success", false);
                json.put("message", r.message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            json.put("success", false);
            json.put("message", "Erreur serveur, veuillez réessayer");
        }

        resp.getWriter().write(gson.toJson(json));
    }
}