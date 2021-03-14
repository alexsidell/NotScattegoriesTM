package com.example.notscattergories;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView timerView;
    private TextView letterView;
    private ProgressBar progressBar;

    private Button btnPlayers;
    private Button btnPlayPause;
    private Button btnRestart;
    private Button btnSettings;

    private LinearLayout categoryView;

    private ArrayList<String> allCategories; //Stores all categories from file.

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        timerView = findViewById(R.id.countDownTimer);
        timerView.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        letterView = findViewById(R.id.letterView);
        letterView.setOnClickListener(this);

        allCategories = new ArrayList<>();

        categoryView = findViewById(R.id.categoryLayoutView);


        btnPlayers = findViewById(R.id.btnPlayers);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnRestart = findViewById(R.id.btnRestart);
        btnSettings = findViewById(R.id.btnSettings);

        btnPlayers.setOnClickListener(this);
        btnPlayPause.setOnClickListener(this);
        btnRestart.setOnClickListener(this);
        btnSettings.setOnClickListener(this);

        getCategoriesFromFile();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.countDownTimer:
                //startGame(10000);
                break;
            case R.id.letterView:
                break;
            case R.id.btnPlayers:
                //Show player info
                break;
            case R.id.btnPlayPause:
                if(!gameInProgress()){
                    startGame(10000);
                }
                else if(timer.isRunning()){
                        timer.pause();
                    } else {
                        timer.resume();
                    }
                break;
            case R.id.btnRestart:
                if (timer != null) {
                    timer.restart();
                }
                break;
            case R.id.btnSettings:
                //open settings
                break;
            default:
                break;
        }
    }

    private void startGame(int time) {
        if (timer == null) {
            timer = new Timer(time, timerView, progressBar, this, btnPlayPause);
        }

        if (!timer.isRunning()) {
            Game game = new Game(7, allCategories.size());
            game.start();

            displayLetter(game.getLetter());
            displayCategories(game.getCategoryIndexes());
            timer.start();
        }
    }


    private boolean gameInProgress(){
        if (timer != null){
            return !timer.isFinished();
        } else {
            return false;
        }

    }

    private void getCategoriesFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("categories.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                allCategories.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayLetter(String letter) {
        letterView.setText(letter);
    }

    /**
     * A method to display categories. Dynamically allocates to a the LinearLayout category view
     * @param categoryIndexes The indexes within the allCategories array to be displayed
     */
    private void displayCategories(int[] categoryIndexes) {
        int[] cats = categoryIndexes;
        categoryView.removeAllViews();

        for (int i=0; i<cats.length; i++) {
            TextView temp = new TextView(this);
            temp.setText(allCategories.get(cats[i]));
            categoryView.addView(temp);
        }

    }


}