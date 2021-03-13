package com.example.notscattergories;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView timer;
    private TextView letter;
    final private char[] LETTERS = "ABCDEFGHIJKLMNOPRSTW".toCharArray();
    private CountDownTimer countTimer;
    private ArrayList<String> categoriesArray;
    private TextView[] categoriesListView;
    private ProgressBar progressBar;

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

        //generateCatagories();
        categoriesArray = new ArrayList<>();
        categoriesArray.add("VIDEO GAMES");
        categoriesArray.add("ELECTRONIC GADGETS");
        categoriesArray.add("BOARD GAMES");
        categoriesArray.add("THINGS THAT USE A REMOTE");
        categoriesArray.add("CARD GAMES");
        categoriesArray.add("INTERNET LINGO");
        categoriesArray.add("WIRELESS THINGS");
        categoriesArray.add("COMPUTER PARTS");
        categoriesArray.add("SEAFOOD");
        categoriesArray.add("WEEKEND ACTIVITIES");
        categoriesArray.add("SPORTS PLAYED INDOORS");
        categoriesArray.add("THINGS YOU SEE AT THE ZOO");
        categoriesArray.add("MATH TERMS");
        categoriesArray.add("WORDS WITH DOUBLE LETTERS");
        categoriesArray.add("WORDS ENDING WITH -LY");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.countDownTimer:
                startGame(60000, 1000);
                break;
            case R.id.letterView:
                reset();
                break;
            default:
                break;
        }
    }

    public void startTimer(int time, int tick) {
        reset();
        int seconds = (time/1000);
        progressBar.setMax(seconds);
        progressBar.setProgress(seconds);

        countTimer = new CountDownTimer(time, tick) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(Long.toString(millisUntilFinished / 1000));
                int progress = (int) ((millisUntilFinished)/1000);
                progressBar.setProgress(progress);

            }

            @Override
            public void onFinish() {
                timer.setText("FINISHED!!");
                progressBar.setProgress(0);
            }
        }.start();
    }

    public void generateLetter() {
        Random rand = new Random();
        int index = rand.nextInt(LETTERS.length);
        letter.setText(Character.toString(LETTERS[index]));
    }

    public void startGame(int time, int tick) {
        generateLetter();
        displayRandCats();
        startTimer(time, tick);
    }

    public void reset() {
        if (countTimer != null) {
            countTimer.cancel();
        }
    }

    public void generateCatagories() {
        Scanner s = null;
        try {
            s = new Scanner(new File("Catagories.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        categoriesArray = new ArrayList<String>();

        while(s.hasNextLine()) {
            categoriesArray.add(s.nextLine());
        }
        s.close();

    }

    public int[] generateArrayIndexes () {
        int[] catNumbers = {-1, -1, -1, -1 ,-1};
        int length = catNumbers.length;
        int index = 0;
        Random rand = new Random();

        while (catNumbers[length - 1] == -1) {
            int num = rand.nextInt(categoriesArray.size());
            if (!containsInt(catNumbers, num)) {
                catNumbers[index] = num;
                index++;
            }
        }
        return catNumbers;
    }

    public boolean containsInt(int[] array, int number) {
        for(int i=0; i<array.length; i++) {
            if (array[i] == number) {
                return true;
            }
        }
        return false;
    }

    public void displayRandCats() {
        int[] cats = generateArrayIndexes();

        for (int i=0; i<cats.length; i++) {
            categoriesListView[i].setText(categoriesArray.get(cats[i]));
        }
    }
}