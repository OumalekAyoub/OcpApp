package com.example.ocpapp.module;

public class fluide {
    private int id_fluide;
    private String fluide;

    public int getId_fluide() {
        return id_fluide;
    }

    public void setId_fluide(int id_fluide) {
        this.id_fluide = id_fluide;
    }

    public String getFluide() {
        return fluide;
    }

    public void setFluide(String fluide) {
        this.fluide = fluide;
    }

    @Override
    public String toString() {
        return this.fluide;
    }
}
