package org.example.modele;
import java.time.LocalDateTime;

public class Utilisateur {

    private int id;
    private final String nom;
    private final String email;
    private final String motDePasseHash;
    private LocalDateTime dateCreation;

    public Utilisateur(int id, String nom, String email,
                       String motDePasseHash, LocalDateTime dateCreation) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasseHash = motDePasseHash;
        this.dateCreation = dateCreation;
    }

    public Utilisateur(String nom, String email, String motDePasseHash) {
        this(0, nom, email, motDePasseHash, LocalDateTime.now());
    }

    public int getId() {return id;}
    public String getNom() {return nom;}
    public String getEmail() {return email;}
    public String getMotDePasseHash() {return motDePasseHash;}
    public LocalDateTime getDateCreation() {return dateCreation;}

    public void setId(int id) {this.id = id;}

    @Override
    public String toString() {
        return nom + " (" + email + ")";
    }
}
