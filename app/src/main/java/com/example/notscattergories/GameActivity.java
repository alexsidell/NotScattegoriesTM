package com.example.notscattergories;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView timerView;
    private TextView letterView;
    private TextView[] categoriesListView;

    private LinearLayout categoryView;

    private final int TIMER_TICK = 1000;

    private ArrayList<String> allCategories; //Stores all categories from file.

    private CountDownTimer countTimer;
    private ProgressBar progressBar;

    AlertDialog.Builder alertDialogBuilder;

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

        countTimer = null;


        categoryView = findViewById(R.id.categoryLayoutView);

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

    private void startGame(int time) {
        Game game = new Game(5, 7, allCategories.size());
        game.start();

        displayLetter(game.getLetter());
        displayCategories(game.getCategoryIndexes());
        startTimer(time);
    }

    private void startTimer(int time) {
        reset();
        int seconds = (time/1000);
        progressBar.setMax(seconds);
        progressBar.setProgress(seconds);

        countTimer = new CountDownTimer(time, TIMER_TICK) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerView.setText(Long.toString(millisUntilFinished / 1000));
                int progress = (int) ((millisUntilFinished)/1000);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                timerView.setText("Play Again");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                progressBar.setProgress(0);

            }
        }.start();
    }

    private void displayLetter(String letter) {
        letterView.setText(letter);

    }

    private void reset() {
        if (countTimer != null) {
            countTimer.cancel();
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

    private void displayCategories(int[] categoryIndexes) {
        int[] cats = categoryIndexes;
        categoryView.removeAllViews();

        for (int i=0; i<cats.length; i++) {
            TextView temp = new TextView(this);
            temp.setText(allCategories.get(cats[i]));
            categoryView.addView(temp);
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