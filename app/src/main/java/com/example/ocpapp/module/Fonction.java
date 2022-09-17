package com.example.ocpapp.module;

public class Fonction {
    private int id_fonction;
    private String libelle_fonction;

    public int getId_fonction() {
        return id_fonction;
    }

    public void setId_fonction(int id_fonction) {
        this.id_fonction = id_fonction;
    }

    public String getLibelle_fonction() {
        return libelle_fonction;
    }

    public void setLibelle_fonction(String libelle_fonction) {
        this.libelle_fonction = libelle_fonction;
    }

    @Override
    public String toString() {
        return this.libelle_fonction;
    }
}
