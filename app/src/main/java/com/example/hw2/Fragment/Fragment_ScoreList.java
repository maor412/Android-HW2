package com.example.hw2.Fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hw2.AdapterScore;
import com.example.hw2.Class.LocationAndScore;
import com.example.hw2.Class.DBManager;
import com.example.hw2.Interface.Score_Callback;
import com.example.hw2.R;

import java.util.ArrayList;


public class Fragment_ScoreList extends Fragment {

    private RecyclerView fragScoreList_LST_scores;

    private AppCompatActivity activity;

    private Score_Callback _scoreCallback;



    AdapterScore adapter_score;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCallBackList(Score_Callback callBackList) {
        this._scoreCallback = callBackList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_list, container, false);
        findViews(view);
        buildScoreList();
        initViews();

        return view;
    }

    private void initViews() {

        adapter_score.setScoreItemClickListener(new AdapterScore.ScoreItemClickListener() {
            @Override
            public void scoreItemClicked(LocationAndScore locationAndScore, int position) {
                fragScoreList_LST_scores.getAdapter().notifyItemChanged(position);
                _scoreCallback.showInMap(locationAndScore);
            }
        });

    }

    private void buildScoreList() {
        DBManager DBManager = new DBManager();
        DBManager.getScoresFromDB();
        ArrayList<LocationAndScore> locationAndScores = DBManager.topTen();

        adapter_score = new AdapterScore(activity, locationAndScores);

        // List in vertical
        fragScoreList_LST_scores.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        fragScoreList_LST_scores.setHasFixedSize(true);
        fragScoreList_LST_scores.setItemAnimator(new DefaultItemAnimator());
        fragScoreList_LST_scores.setAdapter(adapter_score);
    }

    private void findViews(View view) {
        fragScoreList_LST_scores = view.findViewById(R.id.fragScoreList_LST_scores);
    }


}