package org.example.modele;

import java.util.List;

public class Bulletin {
    private int etudiantId;
    private String etudiantNom;
    private String email;
    private String filiere;
    private String niveau;
    private List<Note> notes;
    private double moyenneGenerale;
    private double totalPoints;
    private int totalCoefficients;

    // Constructeur + getters
    public Bulletin(int etudiantId, String etudiantNom, String email,
                    String filiere, String niveau, List<Note> notes) {
        this.etudiantId = etudiantId;
        this.etudiantNom = etudiantNom;
        this.email = email;
        this.filiere = filiere;
        this.niveau = niveau;
        this.notes = notes;
        calculerMoyenne();
    }

    private void calculerMoyenne() {
        this.totalPoints = 0;
        this.totalCoefficients = 0;
        for (Note n : notes) {
            totalPoints += n.getNote() * n.getCoefficient();
            totalCoefficients += n.getCoefficient();
        }
        this.moyenneGenerale = totalCoefficients > 0
                ? totalPoints / totalCoefficients
                : 0;
    }

    public int getEtudiantId() { return etudiantId; }
    public String getEtudiantNom() { return etudiantNom; }
    public String getEmail() { return email; }
    public String getFiliere() { return filiere; }
    public String getNiveau() { return niveau; }
    public List<Note> getNotes() { return notes; }
    public double getMoyenneGenerale() { return moyenneGenerale; }
    public double getTotalPoints() { return totalPoints; }
    public int getTotalCoefficients() { return totalCoefficients; }
}