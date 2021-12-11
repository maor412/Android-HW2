package com.example.hw2.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.example.hw2.R;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    public static final String SENSOR_MODE = "sensor_mode";
    public static final String BUTTONS_MODE = "buttons_mode";
    public static final String SPEED_FAST = "fast";
    public static final String SPEED_SLOW = "slow";
    private MaterialButton btn_start_game;
    private MaterialButton btn_scores;
    private RadioGroup rBtn_group_modesOptions, rBtn_group_speedOptions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        removeActionBar();
        findViews();
        initViews();


    }

    private String getSelectedMode() {

        if(rBtn_group_modesOptions.getCheckedRadioButtonId() == R.id.rBtn_buttons)
            return BUTTONS_MODE;
        else
            return SENSOR_MODE;

    }

    private String getSelectedSpeed() {
        if(rBtn_group_speedOptions.getCheckedRadioButtonId() == R.id.rBtnSpeedFast)
            return SPEED_FAST;
        else
            return SPEED_SLOW;
    }

    private void findViews() {
        btn_scores = findViewById(R.id.btn_scores);
        btn_start_game = findViewById(R.id.btn_startGame);
        rBtn_group_modesOptions = findViewById(R.id.rBtnModeOptions);
        rBtn_group_speedOptions = findViewById(R.id.rBtnSpeedOptions);
    }

    private void initViews(){

        btn_start_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("mode", getSelectedMode());
                intent.putExtra("speed", getSelectedSpeed());

                startActivity(intent);
            }
        });

        btn_scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapAndScoresActivity.class);
                startActivity(intent);
            }
        });

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void removeActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}