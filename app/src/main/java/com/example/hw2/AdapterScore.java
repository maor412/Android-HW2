package com.example.hw2;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hw2.Class.LocationAndScore;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;


public class AdapterScore extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Activity activity;
    private ArrayList<LocationAndScore> locationAndScores = new ArrayList<>();
    private ScoreItemClickListener scoreItemClickListener;

    public AdapterScore(Activity activity, ArrayList<LocationAndScore> locationAndScores) {
        this.activity = activity;
        this.locationAndScores = locationAndScores;
    }

    public AdapterScore setScoreItemClickListener(ScoreItemClickListener scoreItemClickListener) {
        this.scoreItemClickListener = scoreItemClickListener;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder
    onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scroe_list, viewGroup, false);
        return new ScoreViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ScoreViewHolder scoreViewHolder = (ScoreViewHolder) holder;
        LocationAndScore locationAndScore = getItem(position);

        scoreViewHolder.score_list_order.setText("top " +(position+1));


        scoreViewHolder.score_TXT_score.setText(locationAndScore.getScore()+"");
        if(locationAndScore.isMark()){
            scoreViewHolder.score_IMG_mark.setImageResource(R.drawable.ic_fill_mark);
        }else{
            scoreViewHolder.score_IMG_mark.setImageResource(R.drawable.ic_empty_mark);
        }

    }

    @Override
    public int getItemCount() {
        return locationAndScores.size();
    }

    private LocationAndScore getItem(int position) {
        return locationAndScores.get(position);
    }

    public interface ScoreItemClickListener {
        void scoreItemClicked(LocationAndScore locationAndScore, int position);
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder {

        public TextView score_list_order;
        public MaterialTextView score_TXT_score;
        public AppCompatImageView score_IMG_mark;

        public ScoreViewHolder(final View itemView) {
            super(itemView);
            this.score_list_order = itemView.findViewById(R.id.score_list_order);
            this.score_TXT_score = itemView.findViewById(R.id.score_TXT_score);
            this.score_IMG_mark = itemView.findViewById(R.id.score_IMG_mark);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocationAndScore locationAndScore = getItem(getAdapterPosition());
                    locationAndScore.setMark(!locationAndScore.isMark());
                    scoreItemClickListener.scoreItemClicked(locationAndScore, getAdapterPosition());
                }
            });
        }
    }
}

