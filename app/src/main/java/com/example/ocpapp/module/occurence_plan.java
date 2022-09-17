package com.example.ocpapp.module;

import java.util.List;

public class occurence_plan {
    private int id_occurence_plan;
    private String date_occurence_plan;
    private int id_plan;
    private List<String> interventions;
    private boolean isFinish;

    public int getId_occurence_plan() {
        return id_occurence_plan;
    }

    public void setId_occurence_plan(int id_occurence_plan) {
        this.id_occurence_plan = id_occurence_plan;
    }

    public String getDate_occurence_plan() {
        return date_occurence_plan;
    }

    public void setDate_occurence_plan(String date_occurence_plan) {
        this.date_occurence_plan = date_occurence_plan;
    }

    public int getId_plan() {
        return id_plan;
    }

    public void setId_plan(int id_plan) {
        this.id_plan = id_plan;
    }

    public List<String> getInterventions() {
        return interventions;
    }

    public void setInterventions(List<String> interventions) {
        this.interventions = interventions;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }
}
