package com.example.hw2.Class;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

public class DBManager {
    private final String SCORES_KEY = "scores";

    private ArrayList<LocationAndScore> locationAndScores = new ArrayList<>();

    public DBManager() { }

    public ArrayList<LocationAndScore> getRecords() {
        return locationAndScores;
    }

    public DBManager setScores(ArrayList<LocationAndScore> records) {
        this.locationAndScores = records;
        return this;
    }

    public ArrayList<LocationAndScore> getScoresFromDB(){
        String scores_str = MSPV.getMe().getString(SCORES_KEY, "");

        if(scores_str != ""){
            Gson gson = new Gson();
            this.locationAndScores = gson.fromJson(scores_str, DBManager.class).getRecords();
        }else {

            this.locationAndScores = new ArrayList<LocationAndScore>();
        }

        return this.locationAndScores;
    }

    public void saveScoresToDB(){
        Gson gson = new Gson();
        String json_str = gson.toJson(this);
        MSPV.getMe().putString(SCORES_KEY, json_str);
    }

    public ArrayList<LocationAndScore> topTen() {
        Collections.sort(this.locationAndScores);
        ArrayList<LocationAndScore> topTenScores = new ArrayList<>();
        for(int i =0 ; i<10  && i < this.locationAndScores.size(); i++){
            topTenScores.add(this.locationAndScores.get(i));
        }

        return topTenScores;
    }
}
