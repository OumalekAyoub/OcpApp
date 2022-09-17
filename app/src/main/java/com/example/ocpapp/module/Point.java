package com.example.ocpapp.module;

import java.util.List;

public class Point {
    private int id_plan;
    private int id_point;
    private String repere;
    private String localisation;
    private String etat_point;
    private String disposition;
    private String charge_consignation;
    private int id_fonction;
    private boolean Consigne;
    private boolean Verifier;
    private int id_user_consigne;
    private String name_user_consigne;
    private String date_cons;
    private boolean Deconsigner;
    private String name_user_verifier;
    private String date_verification;
    private String name_user_deconsigne;
    private String date_deconsigne;
    private List<String> cadenas;
    private int id_user_cons2;
    private int id_user_verifier;
    private int id_user_deconsigne;

    public int getId_point() {
        return id_point;
    }

    public void setId_point(int id_point) {
        this.id_point = id_point;
    }

    public String getRepere() {
        return repere;
    }

    public void setRepere(String repere) {
        this.repere = repere;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getEtat_point() {
        return etat_point;
    }

    public void setEtat_point(String etat_point) {
        this.etat_point = etat_point;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getCharge_consignation() {
        return charge_consignation;
    }

    public void setCharge_consignation(String charge_consignation) {
        this.charge_consignation = charge_consignation;
    }

    public boolean isConsigne() {
        return Consigne;
    }

    public void setConsigne(boolean consigne) {
        Consigne = consigne;
    }

    public int getId_plan() {
        return id_plan;
    }

    public void setId_plan(int id_plan) {
        this.id_plan = id_plan;
    }

    public int getId_user_consigne() {
        return id_user_consigne;
    }

    public void setId_user_consigne(int id_user_consigne) {
        this.id_user_consigne = id_user_consigne;
    }

    public int getId_fonction() {
        return id_fonction;
    }

    public void setId_fonction(int id_fonction) {
        this.id_fonction = id_fonction;
    }

    public String getName_user_consigne() {
        return name_user_consigne;
    }

    public void setName_user_consigne(String name_user_consigne) {
        this.name_user_consigne = name_user_consigne;
    }

    public String getDate_cons() {
        return date_cons;
    }

    public void setDate_cons(String date_cons) {
        this.date_cons = date_cons;
    }

    public boolean isVerifier() {
        return Verifier;
    }

    public void setVerifier(boolean verifier) {
        Verifier = verifier;
    }

    public boolean isDeconsigner() {
        return Deconsigner;
    }

    public void setDeconsigner(boolean deconsigner) {
        Deconsigner = deconsigner;
    }

    public String getName_user_verifier() {
        return name_user_verifier;
    }

    public void setName_user_verifier(String name_user_verifier) {
        this.name_user_verifier = name_user_verifier;
    }

    public String getDate_verification() {
        return date_verification;
    }

    public void setDate_verification(String date_verification) {
        this.date_verification = date_verification;
    }

    public String getName_user_deconsigne() {
        return name_user_deconsigne;
    }

    public void setName_user_deconsigne(String name_user_deconsigne) {
        this.name_user_deconsigne = name_user_deconsigne;
    }

    public String getDate_deconsigne() {
        return date_deconsigne;
    }

    public void setDate_deconsigne(String date_deconsigne) {
        this.date_deconsigne = date_deconsigne;
    }

    public List<String> getCadenas() {
        return cadenas;
    }

    public void setCadenas(List<String> cadenas) {
        this.cadenas = cadenas;
    }

    public int getId_user_cons2() {
        return id_user_cons2;
    }

    public void setId_user_cons2(int id_user_cons2) {
        this.id_user_cons2 = id_user_cons2;
    }

    public int getId_user_verifier() {
        return id_user_verifier;
    }

    public void setId_user_verifier(int id_user_verifier) {
        this.id_user_verifier = id_user_verifier;
    }

    public int getId_user_deconsigne() {
        return id_user_deconsigne;
    }

    public void setId_user_deconsigne(int id_user_deconsigne) {
        this.id_user_deconsigne = id_user_deconsigne;
    }
}
