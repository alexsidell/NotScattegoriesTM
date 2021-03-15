package com.example.notscattergories;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

/**
 * A class to represent the in game timer. This can start, pause, resume and reset.
 * This class also updates relevant views.
 */
public class Timer {

    private TextView mTimerView;
    private ProgressBar mProgressBar;

    private GameActivity mContext;
    private Button mPlayPauseButton;

    private long mTimeLeft;
    private int mDuration;
    private CountDownTimer mCountTimer = null;
    AlertDialog.Builder mAlertDialogBuilder;

    private final int TIMER_TICK = 1000;

    private boolean mRunning = false;
    private boolean mFinished = true;

    /**
     * A constructor for a timer object.
     * @param duration Duration for the timer
     * @param timerView TextView to display timer information
     * @param progressBar ProgressBar to display timer information
     * @param gameActivity Context for displaying alert message.
     * @param button Button from GameActivity to be updated as pressed.
     */
    public Timer(int duration, TextView timerView, ProgressBar progressBar, GameActivity gameActivity, Button button){
        mDuration = duration;
        mTimerView = timerView;
        mProgressBar = progressBar;
        mContext = gameActivity;
        mPlayPauseButton = button;

        initTimesUpDialogue();

        int seconds = (int)(mDuration / 1000);
        progressBar.setMax(seconds);
        progressBar.setProgress(seconds);
        mFinished = false;
    }

    /**
     * A method to start the Timer. Calls play with mDuration from constructor.
     * Play is not always called with this value, for example if after a pause.
     */
    public void start(){
        play(mDuration);
    }

    /**
     * A method to start a timer.
     * @param timerDuration long length of timer.
     */
    private void play(long timerDuration){
        if (!mRunning) {
            mFinished = false;
            mRunning = true;

            //Draw a pause button as timer is currently playing.
            mPlayPauseButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause, 0, 0, 0);

            mCountTimer = new CountDownTimer(timerDuration, TIMER_TICK) {
                /**
                 * A method that is called on every timer tick. This updates the UI, and
                 * stores the time left in class variable mTimeLeft
                 * @param millisUntilFinished
                 */
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimeLeft = millisUntilFinished;

                    updateUI(millisUntilFinished);
                }

                /**
                 * A method that is called when the timer is finished. This updates parts of the UI,
                 * and creates and alert dialogue. It also updates the button's logo.
                 */
                @Override
                public void onFinish() {
                    mTimerView.setText("Play Again");
                    AlertDialog alertDialog = mAlertDialogBuilder.create();
                    alertDialog.show();
                    mProgressBar.setProgress(0);
                    mRunning = false;
                    mFinished = true;
                    //Draw play button as timer is finished
                    mPlayPauseButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play, 0, 0, 0);
                }

            }.start();
        }
    }

    /**
     * A method to pause the timer, every tick the timer's value is stored in the class variable
     * mTimeLeft. This can be later retrieved when resuming.
     */
    public void pause(){
        if (mCountTimer != null) {
            mCountTimer.cancel();
            mCountTimer = null;
            mRunning = false;
            //Draw play button at timer is currently paused
            mPlayPauseButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play, 0, 0, 0);
        }
    }

    /**
     * A method to resume the timer, calls the play function passing the class variable mTimeLeft.
     * This creates another timer from where the previous one was paused.
     */
    public void resume(){
        play(mTimeLeft);
    }

    /**
     * A method to restart the timer. This calls, pause() and then updates mTimeLeft to be the duration
     * from the constructor, essentially wiping the time left variable.
     * mFinished is also set to true, this tells GameActivity to start a new Game when the player
     * presses the play button.
     */
    public void restart(){
            pause();
            mTimeLeft = mDuration;
            updateUI(mDuration);
            mFinished = true;
    }

    /**
     * Getter method for the mRunning variable.
     * @return true if timer is currently running. False if timer is not running.
     */
    public boolean isRunning(){
        return mRunning;
    }

    /**
     * Getter method for the mFinished variable.
     * @return true if timer has finished (i.e. will not be resumed). False if timer can be resumed.
     */
    public boolean isFinished(){
        return mFinished;
    }

    /**
     * A method to update the UI. This also adjusts milliseconds to seconds, and updates the
     * progress bar.
     * @param millisUntilFinished Time left on timer.
     */
    private void updateUI(long millisUntilFinished){
        mTimerView.setText(Long.toString(millisUntilFinished / 1000));
        int progress = (int) ((millisUntilFinished) / 1000);
        mProgressBar.setProgress(progress);
    }


    /**
     * A method to set up a 'Times up' dialogue.
     */
    private void initTimesUpDialogue() {
        mAlertDialogBuilder = new AlertDialog.Builder(mContext);
        String title = "Time's up!!!!";
        mAlertDialogBuilder.setTitle("Time's up!!!!");
        mAlertDialogBuilder.setMessage("How did you do?");
        mAlertDialogBuilder.setCancelable(true);
        mAlertDialogBuilder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            /**
             * A method to listen for user input in the dialogue box.
             * @param dialog The current dialogue interface.
             * @param which which option has been pressed.
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Can do something when button is pressed.
            }
        });
    }
}
