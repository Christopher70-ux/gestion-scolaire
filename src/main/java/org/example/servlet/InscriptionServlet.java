package org.example.servlet;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.InscriptionService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/inscription")
public class InscriptionServlet extends HttpServlet {

    private final InscriptionService service = new InscriptionService();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        resp.setHeader("Cache-Control", "no-store");

        Map<String, Object> json = new HashMap<>();

        try {
            // 🔑 Récupération des champs (noms EXACTS du HTML)
            String nom = req.getParameter("nom");
            String email = req.getParameter("email");
            String motDePasse = req.getParameter("motDePasse");
            String confirmation = req.getParameter("confirmation");

            // Traitement
            InscriptionService.ResultatInscription resultat =
                    service.inscrire(nom, email, motDePasse, confirmation);

            json.put("success", resultat.succes);
            json.put("message", resultat.message);

            if (resultat.succes && resultat.utilisateur != null) {
                json.put("nom", resultat.utilisateur.getNom());
                json.put("email", resultat.utilisateur.getEmail());
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            json.put("success", false);
            json.put("message", "Erreur serveur, veuillez réessayer");
        }

        // Réponse JSON au JavaScript
        resp.getWriter().write(gson.toJson(json));
    }
}