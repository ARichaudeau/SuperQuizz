package com.example.formation10.superquizz;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Question implements Parcelable {
    private String intitule;
    private ArrayList<String> propositions;
    private String bonneReponse;

    public Question(){
        this.propositions = new ArrayList<>();
    }

    public Question(String intitule) {
        super();
        this.intitule = intitule;
        this.propositions = new ArrayList<>();

    }

    protected Question(Parcel in) {
        intitule = in.readString();
        propositions = in.createStringArrayList();
        bonneReponse = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(intitule);
        dest.writeStringList(propositions);
        dest.writeString(bonneReponse);
    }
}

