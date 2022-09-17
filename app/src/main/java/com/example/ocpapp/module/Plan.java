package com.example.ocpapp.module;

import java.util.List;

public class Plan {
    private int id_plan;
    private String num_ordre;
    private String date_emission;
    private String date_etablissement;
    private String date_approuvation;
    private int id_personnel_approuver;
    private int id_personnel_etablir;
    private int id_equip;
    private String nom_equip;
    private String nom_personnel_etablir;
    private String nom_personnel_approuve;
    private List<String> interventions;
    private boolean approuve;

    public int getId_plan() {
        return id_plan;
    }

    public void setId_plan(int id_plan) {
        this.id_plan = id_plan;
    }

    public String getNum_ordre() {
        return num_ordre;
    }

    public void setNum_ordre(String num_ordre) {
        this.num_ordre = num_ordre;
    }

    public String getDate_emission() {
        return date_emission;
    }

    public void setDate_emission(String date_emission) {
        this.date_emission = date_emission;
    }

    public String getDate_etablissement() {
        return date_etablissement;
    }

    public void setDate_etablissement(String date_etablissement) {
        this.date_etablissement = date_etablissement;
    }

    public String getDate_approuvation() {
        return date_approuvation;
    }

    public void setDate_approuvation(String date_approuvation) {
        this.date_approuvation = date_approuvation;
    }

    public int getId_personnel_approuver() {
        return id_personnel_approuver;
    }

    public void setId_personnel_approuver(int id_personnel_approuver) {
        this.id_personnel_approuver = id_personnel_approuver;
    }

    public int getId_personnel_etablir() {
        return id_personnel_etablir;
    }

    public void setId_personnel_etablir(int id_personnel_etablir) {
        this.id_personnel_etablir = id_personnel_etablir;
    }

    public int getId_equip() {
        return id_equip;
    }

    public void setId_equip(int id_equip) {
        this.id_equip = id_equip;
    }

    public String getNom_equip() {
        return nom_equip;
    }

    public void setNom_equip(String nom_equip) {
        this.nom_equip = nom_equip;
    }

    public String getNom_personnel_etablir() {
        return nom_personnel_etablir;
    }

    public void setNom_personnel_etablir(String nom_personnel_etablir) {
        this.nom_personnel_etablir = nom_personnel_etablir;
    }

    public List<String> getInterventions() {
        return interventions;
    }

    public void setInterventions(List<String> interventions) {
        this.interventions = interventions;
    }

    public boolean isApprouve() {
        return approuve;
    }

    public void setApprouve(boolean approuve) {
        this.approuve = approuve;
    }

    public String getNom_personnel_approuve() {
        return nom_personnel_approuve;
    }

    public void setNom_personnel_approuve(String nom_personnel_approuve) {
        this.nom_personnel_approuve = nom_personnel_approuve;
    }
}
