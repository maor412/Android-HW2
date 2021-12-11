package com.example.hw2.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.hw2.Class.LocationAndScore;
import com.example.hw2.Fragment.Fragment_Map;
import com.example.hw2.Fragment.Fragment_ScoreList;
import com.example.hw2.Interface.Score_Callback;
import com.example.hw2.R;


public class MapAndScoresActivity extends AppCompatActivity {
    private Fragment_ScoreList fragment_scoreList;
    private Fragment_Map fragment_map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        removeActionBar();
        fragment_scoreList = new Fragment_ScoreList();
        fragment_scoreList.setActivity(this);
        fragment_scoreList.setCallBackList(_scoreCallback);
        getSupportFragmentManager().beginTransaction().add(R.id.score_frame_ScoreList, fragment_scoreList).commit();

        fragment_map = new Fragment_Map();
        fragment_map.setActivity(this);
        getSupportFragmentManager().beginTransaction().add(R.id.score_frame_Map, fragment_map).commit();


    }

    Score_Callback _scoreCallback = new Score_Callback() {
        @Override
        public void showInMap(LocationAndScore locationAndScore) {
            if(locationAndScore.isMark()){
                locationAndScore.setMarker(fragment_map.setMarker(locationAndScore));
            }
            else
                fragment_map.removeMarker(locationAndScore);
        }
    };

    public void removeActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

}