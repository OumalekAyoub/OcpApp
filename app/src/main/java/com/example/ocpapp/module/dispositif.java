package com.example.ocpapp.module;

public class dispositif {
    private int id_disposotif;
    private String libelle_dispositif;

    public int getId_disposotif() {
        return id_disposotif;
    }

    public void setId_disposotif(int id_disposotif) {
        this.id_disposotif = id_disposotif;
    }

    public String getLibelle_dispositif() {
        return libelle_dispositif;
    }

    public void setLibelle_dispositif(String libelle_dispositif) {
        this.libelle_dispositif = libelle_dispositif;
    }

    @Override
    public String toString(){
        return this.libelle_dispositif;
    }
}
