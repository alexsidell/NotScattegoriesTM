package com.example.notscattergories;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

/**
 * A class to represent the in game timer. This can start, pause, resume and reset.
 * This class also updates relevant views.
 */
public class Timer {

    private TextView mTimerView;
    private TextView mCountDownView;
    private ProgressBar mProgressBar;

    private GameActivity mContext;
    private Button mPlayPauseButton;
    private LinearLayout mCategoryView;

    private ObjectAnimator progressAnimator;

    private long mTimeLeft;
    private int mDuration;
    private CountDownTimer mCountTimer = null;
    AlertDialog.Builder mAlertDialogBuilder;

    private final int TIMER_TICK = 1000;

    private boolean mRunning = false;
    private boolean mCountDownRunning = false;
    private boolean mFinished = true;

    /**
     * A constructor for a timer object.
     * @param duration Duration for the timer
     * @param timerView TextView to display timer information
     * @param progressBar ProgressBar to display timer information
     * @param context Context for displaying alert message.
     * @param playPauseButton Button from GameActivity to be updated as pressed.
     */
    public Timer(int duration, TextView timerView, TextView countDownView, ProgressBar progressBar, LinearLayout categoryView,
                 Button playPauseButton, GameActivity context){
        mDuration = duration;
        mTimerView = timerView;
        mCountDownView = countDownView;
        mProgressBar = progressBar;
        mCategoryView = categoryView;
        mPlayPauseButton = playPauseButton;
        mContext = context;

        initTimesUpDialogue();

        int seconds = (int)(mDuration / 1000);
        progressBar.setMax(seconds);
        progressBar.setProgress(seconds);
        progressAnimator = ObjectAnimator.ofInt(seconds, "progress", 0,1);
        progressAnimator.setDuration(mDuration);
        progressAnimator.start();



        mFinished = false;
    }

    /**
     * A method to start the Timer. Calls play with mDuration from constructor.
     * Play is not always called with this value, for example if after a pause.
     */
    public void start(){
        countIn(mDuration);
        //play(mDuration);
    }

    /**
     * A method to start a timer.
     * @param timerDuration long length of timer.
     */
    private void play(long timerDuration){
        if (!mRunning) {
            mFinished = false;
            mRunning = true;

            mCategoryView.setVisibility(View.VISIBLE);
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
            mCategoryView.setVisibility(View.INVISIBLE);
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
        countIn(mTimeLeft);
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
     * Getter method for the mCountdownRunning variable.
     * @return true if countdown timer has finished
     */
    public boolean isCountDownRunning() { return mCountDownRunning; }

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

    private void countIn(long timerDuration){
        mCategoryView.setVisibility(View.INVISIBLE);
        mCountDownView.setVisibility(View.VISIBLE);
        mCountDownRunning = true;

        CountDownTimer countIn = new CountDownTimer(3000, TIMER_TICK) {
            /**
             * A method that is called on every timer tick. This updates the UI, and
             * stores the time left in class variable mTimeLeft
             * @param millisUntilFinished
             */
            @Override
            public void onTick(long millisUntilFinished) {
                mCountDownView.setText(Long.toString(millisUntilFinished / 1000));
            }

            /**
             * A method that is called when the timer is finished. This updates parts of the UI,
             * and creates and alert dialogue. It also updates the button's logo.
             */
            @Override
            public void onFinish() {
                mCountDownView.setVisibility(View.INVISIBLE);
                mCountDownRunning = false;
                play(timerDuration);
            }

        }.start();
    }



    /**
     * A method to set up a 'Times up' dialogue.
     */
    private void initTimesUpDialogue() {
        mAlertDialogBuilder = new AlertDialog.Builder(mContext);
        String title = "Time's up!!!!";
        mAlertDialogBuilder.setTitle("Time's up!!!!");
        mAlertDialogBuilder.setMessage("Remember to mark down scores!");
        mAlertDialogBuilder.setCancelable(true);
        mAlertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
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
