package org.example.service;

import org.example.dao.UtilisateurDAO;
import org.example.modele.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.regex.Pattern;

public class InscriptionService {

    String hash;

    private final UtilisateurDAO dao = new UtilisateurDAO();
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public static class ResultatInscription {
        public final boolean succes;
        public final String message;
        public final Utilisateur utilisateur;

        public ResultatInscription(boolean succes, String message, Utilisateur utilisateur) {
            this.succes = succes;
            this.message = message;
            this.utilisateur = utilisateur;
        }
    }

    public ResultatInscription inscrire(String nom, String email,
                                        String motDePasse, String confirmation){
        if (nom == null || nom.trim().length() < 2) {
            return new ResultatInscription(false, "Le nom doit faire au moins 2 caractères", null);
        }
        if (email == null || !EMAIL_REGEX.matcher(email).matches()) {
            return new ResultatInscription(false, "L'email est invalide", null);
        }
        if (motDePasse == null || motDePasse.length() < 8) {
            return new ResultatInscription(false, "Le mot de passe doit faire au moins 8 caractères", null);
        }
        if (!motDePasse.equals(confirmation)) {
            return new ResultatInscription(false, "Les mots de passe ne correspondent pas", null);
        }

        try{
            if(dao.emailExiste(email)){
                return new ResultatInscription(false, "Cet email est déjà utilisé", null);
            }

            // Hasher le mot de passe avec BCrypt
            hash = BCrypt.hashpw(motDePasse, BCrypt.gensalt(12));

            // 4. Enregistrer
            Utilisateur u = new Utilisateur(nom.trim(), email.toLowerCase(), hash);
            dao.creer(u);

            return new ResultatInscription(true, "Inscription réussie !"+ u.getNom(), u);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ResultatInscription(false,
                    "Erreur technique, veuillez réessayer", null);
        }
    }
}
