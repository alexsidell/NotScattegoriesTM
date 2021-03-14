package com.example.notscattergories;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class Timer {

    private TextView timerView;
    private ProgressBar progressBar;
    private GameActivity context;

    private long timeLeft;
    private int mDuration;
    private CountDownTimer countTimer = null;
    AlertDialog.Builder alertDialogBuilder;

    private final int TIMER_TICK = 1000;

    private boolean running = false;
    private boolean isFinished = true;

    private long timeRemaining;
    private Button button;

    public Timer(int duration, TextView timerView, ProgressBar progressBar, GameActivity gameActivity, Button button){
        mDuration = duration;
        this.timerView = timerView;
        this.progressBar = progressBar;
        this.context = gameActivity;
        this.button = button;

        initTimesUpDialogue();

        int seconds = (int)(mDuration / 1000);
        progressBar.setMax(seconds);
        progressBar.setProgress(seconds);
        isFinished = false;
    }

    public void start(){
        play(mDuration);
    }

    private void play(long timerDuration){
        if (!running) {
            isFinished = false;
            running = true;

            button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause, 0, 0, 0);


            countTimer = new CountDownTimer(timerDuration, TIMER_TICK) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeft = millisUntilFinished;

                    updateUI(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    timerView.setText("Play Again");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    progressBar.setProgress(0);
                    running = false;
                    isFinished = true;
                    button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play, 0, 0, 0);
                }

            }.start();
        }
    }
    public void pause(){
        countTimer.cancel();
        countTimer = null;
        running = false;
        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play, 0, 0, 0);
    }

    public void resume(){
        play(timeLeft);
    }

    public void restart(){
        if (countTimer != null) {
            pause();
            timeLeft = mDuration;
            updateUI(mDuration);
        }
    }

    public boolean isRunning(){
        return running;
    }

    public boolean isFinished(){
        return isFinished;
    }

    private void updateUI(long millisUntilFinished){
        timerView.setText(Long.toString(millisUntilFinished / 1000));
        int progress = (int) ((millisUntilFinished) / 1000);
        progressBar.setProgress(progress);
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
