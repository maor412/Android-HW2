package com.example.hw2.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hw2.Class.LocationAndScore;
import com.example.hw2.Class.DBManager;
import com.example.hw2.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private final int HEARTS_NUM = 3;
    private final int COLS = 5; // Cols amount
    private final int RATE = 3; // rate to add rocks in screen
    private final int ROWS = 15; // Rows amount
    private final int MAX_LINES_NUM = 5;
    private final int MIN_LINES_NUM = 1;
    private ImageView game_leftArrow, game_rightArrow;
    private TextView game_score_layout_txt;
    private String mode;
    private LinearLayout carBox;
    private ImageView[] hearts;
    private int carPositionNum; // car's path number
    private TableLayout rocks;
    private Timer playTimer;
    private int index;
    private int accidentCount; // Counter of accidents
    private int SPEED = 1000; // speed of rocks move
    private Random rnd;
    private LocationAndScore player;
    private SensorManager sensorManager;
    private Sensor accSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        removeActionBar();
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        if(intent.getStringExtra("speed").equals(MainActivity.SPEED_FAST)){
            SPEED = 500;
        }
        InitializeVariables();
        findViews();
        prepareGameMode();
        addArrowsClickListener();

    }

    private void prepareGameMode() {
        if (mode.equals(MainActivity.SENSOR_MODE)) {
            game_leftArrow.setVisibility(View.INVISIBLE);
            game_rightArrow.setVisibility(View.INVISIBLE);
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else {
            game_leftArrow.setVisibility(View.VISIBLE);
            game_rightArrow.setVisibility(View.VISIBLE);
        }
    }

    private SensorEventListener accSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if(x > 4 && carPositionNum > MIN_LINES_NUM){
                JumpLeft();
            }else if(x < -4 && carPositionNum < MAX_LINES_NUM){
                JumpRight();
            }
            System.out.println("x = "+ x +" y= "+y + "z = "+ z);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    private void findViews() {
        game_leftArrow = findViewById(R.id.game_imgLeftArrow);
        game_rightArrow = findViewById(R.id.game_imgRightArrow);
        hearts[0] = findViewById(R.id.game_image_Heart_1);
        hearts[1] = findViewById(R.id.game_image_Heart_2);
        hearts[2] = findViewById(R.id.game_image_Heart_3);
        game_score_layout_txt = findViewById(R.id.game_score_layout_txt);
        carBox = findViewById(R.id.game_carBox);
        rocks = findViewById(R.id.game_table_rocks);

    }

    private void addArrowsClickListener() {
        game_rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carPositionNum < MAX_LINES_NUM) {
                    JumpRight();
                }
            }
        });

        game_leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carPositionNum > MIN_LINES_NUM) {
                    JumpLeft();
                }
            }
        });
    }

    public void JumpLeft() {
        carPositionNum--;
        updateCarPosition();
    }

    public void JumpRight() {
        carPositionNum++;
        updateCarPosition();
    }

    private void updateCarPosition() {
        for (int i = 0; i < carBox.getChildCount(); i++) {
            ImageView car = (ImageView) carBox.getChildAt(i);
            car.setVisibility(View.INVISIBLE);
        }

        ((ImageView) carBox.getChildAt(carPositionNum - 1)).setVisibility(View.VISIBLE);
    }


    private void InitializeVariables() {
        player = new LocationAndScore();
        carPositionNum = 3; // sides: 1 - Left     3 - Center      5 - Right
        index = 0;
        rnd = new Random();
        hearts = new ImageView[HEARTS_NUM];
        accidentCount = 0;

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (locationGPS != null) {
            double lat = locationGPS.getLatitude();
            double longi = locationGPS.getLongitude();
            player.setLatitude(lat).setLongitude(longi);
        }
    }

    public void playSound(int soundId){
        //play sound effect
        MediaPlayer mPlayer = MediaPlayer.create(this, soundId);
        mPlayer.start();
    }


    private void showRock(int i, int j) {
        // visible rock image in i,j index
        TableRow row = (TableRow) rocks.getChildAt(i);
        ImageView img  = (ImageView) row.getChildAt(j);
        img.setVisibility(View.VISIBLE);
    }


    public int getRandomRockPos(){
        //get random number of cols
        return rnd.nextInt(COLS);
    }

    private boolean checkCrashInRow(TableRow row){
        boolean isCrashed = false;
        for(int i = 0; i < row.getChildCount(); i++){
            ImageView img = (ImageView) row.getChildAt(i);
            //car is crashed
            if(img.getVisibility() == View.VISIBLE && carPositionNum == i+1){
                accidentCount += 1;
                isCrashed = true;
                // if still have hears decrement by one
                if(accidentCount < HEARTS_NUM) {
                    playSound(R.raw.crash_sound);// play crash sound
                    hearts[HEARTS_NUM - accidentCount].setVisibility(View.INVISIBLE);
                }
                else{
                    // no hears left then game over
                    hearts[0].setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Game Over", Toast.LENGTH_SHORT).show();
                    playTimer.cancel();
                    playSound(R.raw.game_over_sound);//play game over sound
                    updateTop10Scores();
                    finish();
                }
            }
        }
        return isCrashed;
    }

    private void updateTop10Scores(){
        DBManager DBManager = new DBManager();
        ArrayList<LocationAndScore> locationAndScores = DBManager.getScoresFromDB();
        locationAndScores.add(player);
        DBManager.setScores(locationAndScores);
        DBManager.saveScoresToDB();

    }

    private void checkCrash() {
        //check if car crashed
        TableRow row = (TableRow) rocks.getChildAt(ROWS - 5);
        boolean isCrashed = checkCrashInRow(row);
    }

    private void updateRocks() {
        //update rocks location
        for(int i = index%RATE; i < rocks.getChildCount(); i+=RATE){
            TableRow row = (TableRow) rocks.getChildAt(i);
            for(int j = 0 ; j < row.getChildCount(); j++){
                ImageView img = (ImageView) row.getChildAt(j);
                //image visible then invisible and visible the image below it
                if(img.getVisibility() == View.VISIBLE){
                    img.setVisibility(View.INVISIBLE);
                    if(i + 1 < rocks.getChildCount())
                        showRock(i+1, j);
                }
            }
        }
    }

    private void GenerateRocks() {
        playTimer = new Timer();
        playTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, SPEED);
    }


    private void TimerMethod() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                //This method runs in the same thread as the UI.
                if(index % RATE == 0 ) {
                    showRock(0, getRandomRockPos());
                }
                updateRocks();
                checkCrash();
                index++;
                player.addScore(10);
                game_score_layout_txt.setText(""+player.getScore());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GenerateRocks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mode.equals(MainActivity.SENSOR_MODE))
            sensorManager.registerListener(accSensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sensorManager != null){
            sensorManager.unregisterListener(accSensorEventListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        playTimer.cancel();
    }

    public void removeActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }


}