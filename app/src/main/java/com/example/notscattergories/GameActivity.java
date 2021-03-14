package com.example.notscattergories;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView timer;
    private TextView letter;
    private TextView[] categoriesListView;

    private final int TIMER_TICK = 1000;

    private CountDownTimer countTimer;
    private ArrayList<String> categoriesArray; //Stores all categories from file.
    private ProgressBar progressBar;

    AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        timer = findViewById(R.id.countDownTimer);
        timer.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);

        letter = findViewById(R.id.letterView);
        letter.setOnClickListener(this);

        countTimer = null;

        categoriesListView = new TextView[]{
        findViewById(R.id.Cat1),
        findViewById(R.id.Cat2),
        findViewById(R.id.Cat3),
        findViewById(R.id.Cat4),
        findViewById(R.id.Cat5)};

        categoriesArray = new ArrayList<>();
        getCategoriesFromFile();

        initTimesUpDialogue();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.countDownTimer:
                startGame(6000);
                break;
            case R.id.letterView:
                reset();
                break;
            default:
                break;
        }
    }

    public void startTimer(int time) {
        reset();
        int seconds = (time/1000);
        progressBar.setMax(seconds);
        progressBar.setProgress(seconds);

        countTimer = new CountDownTimer(time, TIMER_TICK) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(Long.toString(millisUntilFinished / 1000));
                int progress = (int) ((millisUntilFinished)/1000);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                timer.setText("Play Again");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                progressBar.setProgress(0);

            }
        }.start();
    }



    public void startGame(int time) {
        getCategoriesFromFile();

        Game game = new Game(5, 5, categoriesArray.size());

        game.start();
        letter.setText(game.getLetter());
        game.generateCategories();
        displayCategories(game.getCategoryIndexes());
        startTimer(time);
    }

    public void reset() {
        if (countTimer != null) {
            countTimer.cancel();
        }
    }

    public void getCategoriesFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("categories.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                categoriesArray.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void displayCategories(int[] categoryIndexes) {
        int[] cats = categoryIndexes;

        for (int i=0; i<cats.length; i++) {
            categoriesListView[i].setText(categoriesArray.get(cats[i]));
        }
    }

    private void initTimesUpDialogue() {
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Time's up!!!!");
        alertDialogBuilder.setMessage("How did you do?");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
}