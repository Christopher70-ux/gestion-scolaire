package org.example.modele;

import java.time.LocalDate;

public class Note {
    private int id;
    private int etudiantId;
    private String etudiantNom;     // jointure (lecture seule)
    private int coursId;
    private String coursCode;       // jointure
    private String coursIntitule;   // jointure
    private int coefficient;        // jointure
    private double note;
    private LocalDate dateNote;

    public Note(int id, int etudiantId, int coursId, double note, LocalDate dateNote) {
        this.id = id;
        this.etudiantId = etudiantId;
        this.coursId = coursId;
        this.note = note;
        this.dateNote = dateNote;
    }

    // Getters + setters
    public int getId() { return id; }
    public int getEtudiantId() { return etudiantId; }
    public String getEtudiantNom() { return etudiantNom; }
    public int getCoursId() { return coursId; }
    public String getCoursCode() { return coursCode; }
    public String getCoursIntitule() { return coursIntitule; }
    public int getCoefficient() { return coefficient; }
    public double getNote() { return note; }
    public LocalDate getDateNote() { return dateNote; }

    public void setEtudiantNom(String s) { this.etudiantNom = s; }
    public void setCoursCode(String s) { this.coursCode = s; }
    public void setCoursIntitule(String s) { this.coursIntitule = s; }
    public void setCoefficient(int c) { this.coefficient = c; }
    public void setDateNote(LocalDate d) { this.dateNote = d; }
}