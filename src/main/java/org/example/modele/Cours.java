package org.example.modele;

public class Cours {
    private int id;
    private String code;
    private String intitule;
    private int coefficient;
    private int volumeHeures;

    public Cours(int id, String code, String intitule, int coefficient, int volumeHeures) {
        this.id = id;
        this.code = code;
        this.intitule = intitule;
        this.coefficient = coefficient;
        this.volumeHeures = volumeHeures;
    }

    public Cours(String code, String intitule, int coefficient, int volumeHeures) {
        this(0, code, intitule, coefficient, volumeHeures);
    }

    // Getters
    public int getId() { return id; }
    public String getCode() { return code; }
    public String getIntitule() { return intitule; }
    public int getCoefficient() { return coefficient; }
    public int getVolumeHeures() { return volumeHeures; }

    public void setId(int id) { this.id = id; }
}