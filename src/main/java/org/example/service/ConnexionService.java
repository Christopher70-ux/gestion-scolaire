package org.example.service;

import org.example.modele.Utilisateur;
import org.example.dao.UtilisateurDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.Optional;

public class ConnexionService {

    private final UtilisateurDAO doa = new UtilisateurDAO();

        public static class ResultatConnexion{
            public final boolean succes;
            public final String message;
            public final Utilisateur utilisateur;

            public ResultatConnexion(boolean succes, String message, Utilisateur utilisateur) {
                this.succes = succes;
                this.message = message;
                this.utilisateur = utilisateur;
            }
        }

        public ResultatConnexion connecter(String email, String motDePasse){

            if(email == null || email.isEmpty()){
                return new ResultatConnexion(false, "L'email est obligatoire", null);
            }

            if (motDePasse == null || motDePasse.isEmpty()) {
                return new ResultatConnexion(false, "Le mot de passe est obligatoire", null);
            }

            try{
                Optional<Utilisateur> opt = doa.trouverParEmail(email.toLowerCase());
                if (opt.isEmpty()){
                    return new ResultatConnexion(false, "Email ou mot de passe incorrect", null);
                }
                Utilisateur utilisateur = opt.get();

                if(!BCrypt.checkpw(motDePasse, utilisateur.getMotDePasseHash())){
                    return new ResultatConnexion(false, "Email ou mot de passe incorrect", null);
                }

                return new ResultatConnexion(true, "Connexion réussie ! Bienvenue " + utilisateur.getNom(), utilisateur);

            }catch(SQLException e){
                e.printStackTrace();
                return new ResultatConnexion(false, "Erreur technique, veuillez réessayer", null);
            }
        }

}
