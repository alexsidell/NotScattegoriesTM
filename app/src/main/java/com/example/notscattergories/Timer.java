package com.example.notscattergories;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class Timer {

    private TextView timerView;
    private ProgressBar progressBar;
    private GameActivity context;

    private int timerDuration;
    private CountDownTimer countTimer = null;
    AlertDialog.Builder alertDialogBuilder;

    private final int TIMER_TICK = 1000;

    private boolean running = false;

    private int pauseTimeRemaining;


    public Timer(int duration, TextView timerView, ProgressBar progressBar, GameActivity gameActivity){
        this.timerDuration = duration;
        this.timerView = timerView;
        this.progressBar = progressBar;
        this.context = gameActivity;

        initTimesUpDialogue();
    }

    public void play(){
        if (!running) {
            running = true;
            int seconds = (timerDuration / 1000);
            progressBar.setMax(seconds);
            progressBar.setProgress(seconds);

            countTimer = new CountDownTimer(timerDuration, TIMER_TICK) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerView.setText(Long.toString(millisUntilFinished / 1000));
                    int progress = (int) ((millisUntilFinished) / 1000);
                    progressBar.setProgress(progress);
                }

                @Override
                public void onFinish() {
                    timerView.setText("Play Again");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    progressBar.setProgress(0);
                    running = false;
                }

            }.start();
        }
    }
    public void pause(){

    }

    public void restart(){
        if (countTimer != null) {
            countTimer.cancel();
            countTimer = null;
            running = false;

            play();
        }
    }

    public boolean isRunning(){
        return running;
    }


    private void initTimesUpDialogue() {
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Time's up!!!!");
        alertDialogBuilder.setMessage("How did you do?");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Can do something when button is pressed.
            }
        });
    }
}
