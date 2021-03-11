package com.example.notscattergories;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView timer;
    private TextView letter;
    private char[] letters;
    private CountDownTimer countTimer;
    private ArrayList<String> catagoriesArray;
    private TextView[] catagoriesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        timer = findViewById(R.id.countDownTimer);
        timer.setOnClickListener(this);

        letter = findViewById(R.id.letterView);
        letter.setOnClickListener(this);

        letters = "ABCDEFGHIJKLMNOPRSTW".toCharArray();
        countTimer = null;

        catagoriesListView = new TextView[]{
        findViewById(R.id.Cat1),
        findViewById(R.id.Cat2),
        findViewById(R.id.Cat3),
        findViewById(R.id.Cat4),
        findViewById(R.id.Cat5)};

        //generateCatagories();
        catagoriesArray = new ArrayList<>();
        catagoriesArray.add("VIDEO GAMES");
        catagoriesArray.add("ELECTRONIC GADGETS");
        catagoriesArray.add("BOARD GAMES");
        catagoriesArray.add("THINGS THAT USE A REMOTE");
        catagoriesArray.add("CARD GAMES");
        catagoriesArray.add("INTERNET LINGO");
        catagoriesArray.add("WIRELESS THINGS");
        catagoriesArray.add("COMPUTER PARTS");
        catagoriesArray.add("SEAFOOD");
        catagoriesArray.add("WEEKEND ACTIVITIES");
        catagoriesArray.add("SPORTS PLAYED INDOORS");
        catagoriesArray.add("THINGS YOU SEE AT THE ZOO");
        catagoriesArray.add("MATH TERMS");
        catagoriesArray.add("WORDS WITH DOUBLE LETTERS");
        catagoriesArray.add("WORDS ENDING WITH -LY");


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

        countTimer = new CountDownTimer(time, tick) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(Long.toString(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                timer.setText("FINISHED!!");
            }
        }.start();
    }

    public void generateLetter() {
        Random rand = new Random();
        int index = rand.nextInt(letters.length);
        letter.setText(Character.toString(letters[index]));
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
        catagoriesArray = new ArrayList<String>();

        while(s.hasNextLine()) {
            catagoriesArray.add(s.nextLine());
        }
        s.close();

    }

    public int[] generateArrayIndexes () {
        int[] catNumbers = {-1, -1, -1, -1 ,-1};
        int length = catNumbers.length;
        int index = 0;
        Random rand = new Random();

        while (catNumbers[length - 1] == -1) {
            int num = rand.nextInt(catagoriesArray.size());
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
            catagoriesListView[i].setText(catagoriesArray.get(cats[i]));
        }
    }
}