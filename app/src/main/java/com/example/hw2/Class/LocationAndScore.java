package com.example.hw2.Class;

import com.google.android.gms.maps.model.Marker;

public class LocationAndScore implements Comparable<LocationAndScore>{
    private int score;
    private double latitude;
    private double longitude;
    private boolean IsMark;
    private Marker marker;
    public LocationAndScore(){
        this.score = 0;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.marker = null;
    }

    public LocationAndScore(int score, double latitude, double longitude){
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getScore() {
        return score;
    }

    public LocationAndScore setScore(int score) {
        this.score = score;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public LocationAndScore setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public LocationAndScore setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public void addScore(int value){
        this.score += value;
    }

    public boolean isMark() {
        return IsMark;
    }

    public LocationAndScore setMark(boolean isMark) {
        this.IsMark = isMark;
        return this;
    }

    public Marker getMarker() {
        return marker;
    }

    public LocationAndScore setMarker(Marker marker) {
        this.marker = marker;
        return this;
    }

    @Override
    public int compareTo(LocationAndScore locationAndScore) {
        if( this.score > locationAndScore.getScore())
            return -1;

        if(this.score < locationAndScore.getScore())
            return 1;

        return 0;
    }
}
