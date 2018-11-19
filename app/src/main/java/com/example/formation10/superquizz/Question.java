package com.example.formation10.superquizz;

import java.util.ArrayList;

public class Question {
    private String intitule;
    private ArrayList<String> propositions;
    private String bonneReponse;

    public Question(String intitule) {
        super();
        this.intitule = intitule;
        this.propositions = new ArrayList<>();
    }

    public boolean verifierReponse(String verif) {
        return verif.equals(bonneReponse);
    }

    public void addProposition(String proposition) {
        propositions.add(proposition);
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public ArrayList<String> getPropositions() {
        return propositions;
    }

    public void setPropositions(ArrayList<String> propositions) {
        this.propositions = propositions;
    }

    public String getBonneReponse() {
        return bonneReponse;
    }

    public void setBonneReponse(String bonneReponse) {
        this.bonneReponse = bonneReponse;
    }

}

