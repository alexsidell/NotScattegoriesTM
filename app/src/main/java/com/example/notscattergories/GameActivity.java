package com.example.notscattergories;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;


/**
 * A class for activity_game.xml
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private TextView mTimerView; //Store the Timer text view
    private TextView mLetterView; //Store letterView
    private TextView mCountView; //Store countInView
    private ProgressBar mProgressBar; //Store ProgressBar

    private Button btnPlayers; //Store the Players button
    private Button btnPlayPause; //Store the Play/Pause Button
    private Button btnRestart; //Store the restart Button
    private Button btnSettings; //Store the settings button

    //private SharedPreferences sharedPref;

    AlertDialog.Builder mWelcomeDialogBuilder;

    private LinearLayout categoryView; //LinearLayout to store list of TextViews as categories.

    private ArrayList<String> allCategories; //Stores all categories from file.

    private Timer timer; //Stores a timer object.

    private final int GAME_TIME = 90000;
    private final int NUMBER_OF_CATS = 12;
    private SoundPool soundPool;
    private int sound1;

    private GuideView mGuideView;
    private GuideView.Builder builder;

    private boolean eyesOpen = true;

    private static GameActivity mInstance;

    /**
     * A method that is called when activity is created.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mTimerView = findViewById(R.id.countDownTimer);
        mTimerView.setOnClickListener(this);
        mProgressBar = findViewById(R.id.progressBar);
        mLetterView = findViewById(R.id.letterView);
        mLetterView.setOnClickListener(this);
        mCountView = findViewById(R.id.countInTextView);

        allCategories = new ArrayList<>(); //Stores all categories from the categories.txt

        categoryView = findViewById(R.id.categoryLayoutView);

        btnPlayers = findViewById(R.id.btnPlayers);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnRestart = findViewById(R.id.btnRestart);
        btnSettings = findViewById(R.id.btnSettings);

        btnPlayers.setOnClickListener(this);
        btnPlayPause.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnRestart.setOnClickListener(this);

        btnRestart.setOnLongClickListener(this);

        mInstance = this;

        getCategoriesFromFile();
        initialiseWelcomeDialogue();
        initialiseSharedPreferences();
        clearAllViews(); //Ensures consistency in apps display

        startTutorialIfFirstTime();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        sound1 = soundPool.load(this, R.raw.sound1, 1);

    }

    /**
     * A method to listen for button presses, and perform actions based on that.
     *
     * @param v The view being pressed.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.countDownTimer:
                if (!gameInProgress()) {
                    startGame();
                }
                break;
            case R.id.letterView:
                blink();
                break;
            case R.id.btnPlayers:
                //Show player info
                Intent playerScores = new Intent(getApplicationContext(), PlayerScores.class);
                startActivity(playerScores);
                break;
            case R.id.btnPlayPause:
                //Used to start, play, and pause the timer.
                if (!gameInProgress() && !countDownInProgess()) {
                    startGame();
                    Toast.makeText(getApplicationContext(), "Starting Game", Toast.LENGTH_SHORT).show();
                    soundPool.play(sound1, 1, 1, 0, 0, 1);
                } else if (timer.isRunning()) {
                    timer.pause();

                    Toast.makeText(getApplicationContext(), "Game Paused", Toast.LENGTH_SHORT).show();
                } else if (!countDownInProgess()) {
                    timer.resume();

                    Toast.makeText(getApplicationContext(), "Resuming Game", Toast.LENGTH_SHORT).show();
                    soundPool.play(sound1, 1, 1, 0, 0, 1);
                }

                break;
            case R.id.btnRestart:
                Toast.makeText(this, "Hold to Restart", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnSettings:
                //open settings popup
                if (gameInProgress()) {
                    timer.pause();

                }
                Intent settingsPop = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsPop);
                break;

            default:

                break;
        }
    }

    /**
     * A method to listen for long button presses, and perform actions based on that.
     *
     * @param v The view being pressed.
     */
    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.btnRestart:
                //Used to restart
                if (timer != null && !countDownInProgess()) {
                    timer.restart();
                    timer = null;
                    clearAllViews();
                }

                break;
            default:
                break;
        }
        return false;
    }

    /**
     * A method to start a game of NotScattegories.
     */
    private void startGame() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int time = sharedPref.getInt("time", GAME_TIME);
        int noOfCats = sharedPref.getInt("categories", NUMBER_OF_CATS);
        if (timer == null) {
            //Create a new timer object if one does not exist. Timer will be null if it has finished.
            timer = new Timer(time, mTimerView, mCountView, mProgressBar, categoryView, btnPlayPause, this);
        }

        if (!timer.isRunning()) {
            //If game is not running, start a new game.
            Game game = new Game(noOfCats, allCategories.size());
            game.start();

            timer.start();

            displayLetter(game.getLetter());
            displayCategories(game.getCategoryIndexes());
        }
    }


    /**
     * A method to clear all views. This allows for user consistency.
     */
    private void clearAllViews() {
        mTimerView.setTextSize(50);
        mTimerView.setText("*");
        mLetterView.setText("*");
        categoryView.removeAllViews();
    }

    private void blink() {
        if (!gameInProgress()) {
            if (eyesOpen) {
                mTimerView.setText("_");
                mLetterView.setText("_");
                eyesOpen = false;
            } else {
                mTimerView.setText("*");
                mLetterView.setText("*");
                eyesOpen = true;
            }
        }
    }

    /**
     * A method to check whether a game is in progress. It checks whether the timer has finished.
     * This will return true if the game has been paused.
     *
     * @return True if game is in progress. False if game is not in progress.
     */
    private boolean gameInProgress() {
        if (timer != null) {
            return !timer.isFinished();
        } else {
            return false;
        }
    }

    private boolean countDownInProgess() {
        if (timer != null) {
            return timer.isCountDownRunning();
        } else {
            return false;
        }
    }

    /**
     * A method to get a list of categories from the categories.txt file.
     */
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


    /**
     * A method to display a letter in the letterView.
     *
     * @param letter letter to be displayed.
     */
    private void displayLetter(String letter) {
        mLetterView.setText(letter);
    }

    /**
     * A method to display categories. Dynamically allocates to a the LinearLayout category view
     *
     * @param categoryIndexes The indexes within the allCategories array to be displayed
     */
    private void displayCategories(int[] categoryIndexes) {
        int[] cats = categoryIndexes;
        categoryView.removeAllViews();

        for (int i = 0; i < cats.length; i++) {
            TextView temp = new TextView(this);
            temp.setText(allCategories.get(cats[i]));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                temp.setTextAppearance(R.style.catText);
            }
            categoryView.addView(temp);
        }

    }

    private void initialiseSharedPreferences() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("time", GAME_TIME);
        editor.putInt("categories", NUMBER_OF_CATS);
        if (!sharedPref.contains("first_time")) {
            editor.putBoolean("first_time", true);
        }
        editor.commit();
    }

    private void initialiseWelcomeDialogue() {
        mWelcomeDialogBuilder = new AlertDialog.Builder(this);
        mWelcomeDialogBuilder.setTitle("Welcome!");
        mWelcomeDialogBuilder.setMessage("Welcome to NotScattergories, would you like to take a tour?");
        mWelcomeDialogBuilder.setCancelable(true);
        mWelcomeDialogBuilder.setPositiveButton("Tour", new DialogInterface.OnClickListener() {
            /**
             * A method to listen for user input in the dialogue box.
             * @param dialog The current dialogue interface.
             * @param which which option has been pressed.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startTour();
            }
        });
    }

    public void startTour() {
        //Simulates a game for the tour
        Game game = new Game(6, allCategories.size());
        game.start();

        displayLetter(game.getLetter());
        displayCategories(game.getCategoryIndexes());

        //Start of tour code
        builder = new GuideView.Builder(this);
        builder.setTitle("Letter");
        builder.setContentText("The selected letter will be displayed here. ");
        builder.setGravity(smartdevelop.ir.eram.showcaseviewlib.config.Gravity.center);
        builder.setDismissType(DismissType.anywhere);
        builder.setCircleIndicatorSize(5);

        builder.setTargetView(mLetterView).build();

        builder.setGuideListener(new GuideListener() {
            @Override
            public void onDismiss(View view) {
                switch (view.getId()) {
                    case R.id.letterView:
                        builder.setTitle("Countdown timer");
                        builder.setContentText("The time left will be displayed here");
                        builder.setTargetView(mTimerView).build();

                        break;
                    case R.id.countDownTimer:
                        builder.setTitle("Progress Bar");
                        builder.setContentText("A visual representation of time left.");
                        builder.setTargetView(mProgressBar).build();

                        break;
                    case R.id.progressBar:
                        builder.setTitle("Categories");
                        builder.setContentText("A list of categories will be displayed here.");
                        builder.setIndicatorHeight(5);
                        builder.setTargetView(categoryView).build();

                        break;
                    case R.id.categoryLayoutView:
                        builder.setTitle("Players");
                        builder.setContentText("Here you can input player names and scores.");
                        builder.setIndicatorHeight(50);
                        builder.setTargetView(btnPlayers).build();

                        break;
                    case R.id.btnPlayers:
                        builder.setTitle("Play/Pause");
                        builder.setContentText("Press to start and pause a game.");
                        builder.setTargetView(btnPlayPause).build();

                        break;
                    case R.id.btnPlayPause:
                        builder.setTitle("Restart");
                        builder.setContentText("Hold to restart the game");
                        builder.setTargetView(btnRestart).build();

                        break;
                    case R.id.btnRestart:
                        builder.setTitle("Settings");
                        builder.setContentText("Adjust timer duration and number of categories. " +
                                "You can also take this tour again");
                        builder.setTargetView(btnSettings).build();

                        break;
                    default:
                        clearAllViews();
                        return;

                }

                mGuideView = builder.build();
                mGuideView.show();
            }

        });
        mGuideView = builder.build();
        mGuideView.show();

    }

    private boolean isUsersFirstTime() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPrefs.getBoolean("first_time", true);
    }
     private void startTutorialIfFirstTime() {
         if (isUsersFirstTime()) {
             AlertDialog alertDialog = mWelcomeDialogBuilder.create();
             alertDialog.show();
             SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
             SharedPreferences.Editor editor = sharedPref.edit();
             editor.putBoolean("first_time", false);
             editor.commit();
         }

     }

     public static GameActivity getInstance(){
        return mInstance;
     }

     public void restartActivity(){
         finish();
         Intent runTutorial = new Intent(getApplicationContext(), GameActivity.class);
         startActivity(runTutorial);
     }

}