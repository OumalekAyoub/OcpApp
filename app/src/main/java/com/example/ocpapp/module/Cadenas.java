package com.example.ocpapp.module;

public class Cadenas {
    private int id_cadenas;
    private String num_cadenas;

    public int getId_cadenas() {
        return id_cadenas;
    }

    public void setId_cadenas(int id_cadenas) {
        this.id_cadenas = id_cadenas;
    }

    public String getNum_cadenas() {
        return num_cadenas;
    }

    public void setNum_cadenas(String num_cadenas) {
        this.num_cadenas = num_cadenas;
    }

    @Override
    public String toString() {
        return this.num_cadenas;
    }
}
