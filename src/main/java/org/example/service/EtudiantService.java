package org.example.service;

import org.example.dao.EtudiantsDAO;
import org.example.modele.Etudiants;


import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Pattern;

import java.util.List;

public class EtudiantService {

    private final EtudiantsDAO dao = new EtudiantsDAO();
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // ============================================================
    // Résultat d'une opération
    // ============================================================
    public static class Resultat {
        public final boolean succes;
        public final String message;
        public final Etudiants etudiant;

        public Resultat(boolean succes, String message, Etudiants e) {
            this.succes = succes;
            this.message = message;
            this.etudiant = e;
        }

        public static Resultat ok(String message, Etudiants e) {
            return new Resultat(true, message, e);
        }

        public static Resultat erreur(String message) {
            return new Resultat(false, message, null);
        }
    }

    // ============================================================
    // AJOUT
    // ============================================================
    public Resultat ajouter(String nom, String email, String telephone,
                            String filiere, String niveau) {

        // 1. Validations
        if (nom == null || nom.trim().length() < 2) {
            return Resultat.erreur("Le nom doit faire au moins 2 caractères");
        }
        if (email == null || !EMAIL_REGEX.matcher(email).matches()) {
            return Resultat.erreur("L'email est invalide");
        }
        if (telephone != null && !telephone.isEmpty()
                && !telephone.matches("^[0-9]{8,15}$")) {
            return Resultat.erreur("Le téléphone doit contenir 8 à 15 chiffres");
        }

        // 2. Vérifier doublon
        try {
            if (dao.emailExiste(email.toLowerCase())) {
                return Resultat.erreur("Cet email est déjà utilisé");
            }

            // 3. Créer et enregistrer
            Etudiants e = new Etudiants(
                    nom.trim(),
                    email.toLowerCase(),
                    telephone,
                    filiere,
                    niveau
            );
            dao.ajouter(e);

            return Resultat.ok("Étudiant ajouté avec succès", e);

        } catch (SQLException ex) {
            ex.printStackTrace();
            return Resultat.erreur("Erreur technique : " + ex.getMessage());
        }
    }

    // ============================================================
    // MODIFICATION
    // ============================================================
    public Resultat modifier(int id, String nom, String email, String telephone,
                             String filiere, String niveau) {

        if (nom == null || nom.trim().length() < 2) {
            return Resultat.erreur("Le nom doit faire au moins 2 caractères");
        }
        if (email == null || !EMAIL_REGEX.matcher(email).matches()) {
            return Resultat.erreur("L'email est invalide");
        }

        try {
            // Vérifier que l'étudiant existe
            Optional<Etudiants> opt = dao.trouverParId(id);
            if (opt.isEmpty()) {
                return Resultat.erreur("Étudiant introuvable");
            }

            // Vérifier doublon email (autre étudiant avec le même email)
            Optional<Etudiants> autre = dao.trouverParEmail(email.toLowerCase());
            if (autre.isPresent() && autre.get().getId() != id) {
                return Resultat.erreur("Cet email est déjà utilisé par un autre étudiant");
            }

            Etudiants e = new Etudiants(id, nom.trim(), email.toLowerCase(),
                    telephone, filiere, niveau);
            boolean ok = dao.modifier(e);

            return ok
                    ? Resultat.ok("Étudiant modifié avec succès", e)
                    : Resultat.erreur("Modification impossible");

        } catch (SQLException ex) {
            ex.printStackTrace();
            return Resultat.erreur("Erreur technique : " + ex.getMessage());
        }
    }

    // ============================================================
    // SUPPRESSION
    // ============================================================
    public Resultat supprimer(int id) {
        try {
            Optional<Etudiants> opt = dao.trouverParId(id);
            if (opt.isEmpty()) {
                return Resultat.erreur("Étudiant introuvable");
            }

            boolean ok = dao.supprimer(id);
            return ok
                    ? Resultat.ok("Étudiant supprimé", opt.get())
                    : Resultat.erreur("Suppression impossible");

        } catch (SQLException ex) {
            ex.printStackTrace();
            return Resultat.erreur("Erreur technique : " + ex.getMessage());
        }
    }

    // ============================================================
// CONSULTATION
// ============================================================
    public List<Etudiants> tousLesEtudiants() throws SQLException {
        return dao.tousLesEtudiants();
    }

    public List<Etudiants> rechercher(String motCle) throws SQLException {
        return dao.rechercher(motCle);
    }

    public Optional<Etudiants> trouverParId(int id) throws SQLException {
        return dao.trouverParId(id);
    }
}