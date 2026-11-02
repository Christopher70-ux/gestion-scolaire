package org.example.modele;

import java.time.LocalDateTime;

public class Etudiants {

    private int id;
    private String nom;
    private String email;
    private String telephone;
    private String filiere;
    private String niveau;
    private LocalDateTime dateCreation;

    public Etudiants(int id, String nom, String email, String telephone, String filiere, String niveau,
            LocalDateTime dateCreation) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.filiere = filiere;
        this.niveau = niveau;
        this.dateCreation = dateCreation;
    }

    public Etudiants(String nom, String email, String telephone, String filiere, String niveau) {
        this(0, nom, email, telephone, filiere, niveau, LocalDateTime.now());
    }

    public Etudiants(int id, String nom, String email, String telephone, String filiere, String niveau) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.filiere = filiere;
        this.niveau = niveau;
        this.dateCreation = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getFiliere() {
        return filiere;
    }

    public String getNiveau() {
        return niveau;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }
}