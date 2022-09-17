package com.example.ocpapp.module;

public class Equipement {
    private int id_equipement;
    private String nom;
    private int nombre;
    private String espaceConfine;
    private int id_secteur;
    private int id_type;
    private int id_fluide;

    public int getId_equipement() {
        return id_equipement;
    }

    public void setId_equipement(int id_equipement) {
        this.id_equipement = id_equipement;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public String getEspaceConfine() {
        return espaceConfine;
    }

    public void setEspaceConfine(String espaceConfine) {
        this.espaceConfine = espaceConfine;
    }

    public int getId_secteur() {
        return id_secteur;
    }

    public void setId_secteur(int id_secteur) {
        this.id_secteur = id_secteur;
    }

    public int getId_type() {
        return id_type;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    public int getId_fluide() {
        return id_fluide;
    }

    public void setId_fluide(int id_fluide) {
        this.id_fluide = id_fluide;
    }

    @Override
    public String toString() {
        return this.nom;
    }
}
