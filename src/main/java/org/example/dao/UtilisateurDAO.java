package org.example.dao;

import org.example.modele.Utilisateur;
import org.example.utils.DataBase;
import java.sql.*;
import java.util.Optional;

public class UtilisateurDAO {

    private String sql;
    int lignes;

    public Utilisateur creer(Utilisateur u) throws SQLException{
        sql = "INSERT INTO utilisateurs (nom, email, mot_de_passe) VALUES (?, ?, ?)";

        try(Connection conn = DataBase.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1,u.getNom());
            ps.setString(2,u.getEmail());
            ps.setString(3,u.getMotDePasseHash());

            lignes = ps.executeUpdate();
            if (lignes == 0) {
                throw new SQLException("Création échouée, aucune ligne insérée");
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    u.setId(keys.getInt(1));
                }
            }
        }
        return u;
    }


    public Optional<Utilisateur> trouverParEmail(String email) throws SQLException{
        String sql = "SELECT id, nom, email, mot_de_passe, date_creation "
                + "FROM utilisateurs WHERE email = ?";

        try(Connection conn = DataBase.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, email);

            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    Utilisateur u = new Utilisateur(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("email"),
                            rs.getString("mot_de_passe"),
                            rs.getTimestamp("date_creation").toLocalDateTime()
                    );
                    return Optional.of(u);
                }
            }
        }
        return Optional.empty();
    }

    public boolean emailExiste(String email) throws SQLException{
        sql = "SELECT 1 FROM utilisateurs WHERE email = ? LIMIT 1";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();   // true si au moins une ligne
            }
        }
    }
}
