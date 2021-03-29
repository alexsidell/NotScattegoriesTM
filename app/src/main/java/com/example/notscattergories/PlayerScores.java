package com.example.notscattergories;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerScores extends AppCompatActivity implements View.OnClickListener, AddPlayerDialog.AddPlayerDialogListener {
    HashMap<String, Integer> scores = new HashMap<String, Integer>();
    String name;
    int score;
    public static HashMap<String,Integer> playerList = new HashMap();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        Button back = findViewById(R.id.btnBackToGame);
        Button add_player = findViewById(R.id.btnAddPlayer);

        back.setOnClickListener(this);
        add_player.setOnClickListener(this);
        //For testing
        displayScores();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBackToGame:
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                break;
            case R.id.btnAddPlayer:
                addPlayer();
                break;
            default:
                int num = v.getId();
                TextView playerView;
                TextView scoreView;
                if((num+1)%5==0){
                    //Add a point
                    scoreView = findViewById(num-1);
                    playerView = findViewById(num-3);
                    String playerName = playerView.getText().toString();
                    Integer playerScore = Integer.parseInt(scoreView.getText().toString());
                    playerList.put(playerName,playerScore+1);
                }
                else if (num%5==0){
                    playerView = findViewById(num-4);
                    String playerName = playerView.getText().toString();
                    playerList.remove(playerName);
                }
                else {
                    //Subtract a point
                    scoreView = findViewById(num+1);
                    playerView = findViewById(num-1);
                    String playerName = playerView.getText().toString();
                    Integer playerScore = Integer.parseInt(scoreView.getText().toString());
                    playerList.put(playerName,playerScore-1);
                }
                System.out.println("button pressed");
                System.out.println("id: "+num);
                displayScores();

        }
    }

    private void addPlayer(){
        //get player name from pop up
        //create new textview
        DialogFragment addPlayerDialog = new AddPlayerDialog();
        addPlayerDialog.show(getSupportFragmentManager(), "addPlayer");
    }

    private void clearNames(){}

    private void displayScores() {
        LinearLayout playerListInner = (LinearLayout) findViewById(R.id.playerListInner);
        playerListInner.removeAllViews();
        ArrayList<String> arrayListVersion = new ArrayList<String>(playerList.keySet());
        int idCounter = 1;
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT);


        for (String name:playerList.keySet()) {
            LinearLayout playerLayout = new LinearLayout(getApplicationContext());
            playerLayout.setOrientation(LinearLayout.HORIZONTAL);

            String player = name;
            //Player Name
            EditText playerView = new EditText(this);
            playerView.setId(idCounter);
            idCounter++;
            playerView.setText(player);
            playerLayout.addView(playerView);
            //Subtract Points Button
            Button subPoint = new Button(this);
            subPoint.setOnClickListener(this);
            subPoint.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove,0,0,0);
            subPoint.setLayoutParams(buttonParams);
            subPoint.setBackgroundColor(Color.TRANSPARENT);
            subPoint.setId(idCounter);
            idCounter++;
            playerLayout.addView(subPoint);
            //Player Score
            EditText scoreView = new EditText(this);
            scoreView.setInputType(InputType.TYPE_CLASS_NUMBER);
            scoreView.setId(idCounter);
            idCounter++;
            scoreView.setText(playerList.get(player).toString());
            playerLayout.addView(scoreView);
            //Add Point Button
            Button addPoint = new Button(this);
            addPoint.setOnClickListener(this);
            addPoint.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add,0,0,0);
            addPoint.setLayoutParams(buttonParams);
            addPoint.setBackgroundColor(Color.TRANSPARENT);
            addPoint.setId(idCounter);
            idCounter++;
            playerLayout.addView(addPoint);
            //Remove player button
            Button removePlayer = new Button(this);
            removePlayer.setOnClickListener(this);
            removePlayer.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove_player,0,0,0);
            removePlayer.setId(idCounter);
            removePlayer.setLayoutParams(buttonParams);
            removePlayer.setBackgroundColor(Color.TRANSPARENT);
            idCounter++;
            playerLayout.addView(removePlayer);


            playerLayout.setWeightSum(2);
            playerView.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            scoreView.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            playerListInner.addView(playerLayout);
        }

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment, String newPlayerName) {
        //Add player to player list
//        try {
//            playerList.get(newPlayerName);
//            DialogFragment addPlayerError = new AddPlayerError();
//            addPlayerError.show(getSupportFragmentManager(), "addPlayerError");
//        }catch (Exception e){System.out.println("Name does not exist already in Hashmap");
//            try {
//                playerList.put(newPlayerName, 0);
//            } catch (Exception f) {
//                System.out.println("Not added to Hashmap");
//            }
//        }
//        displayScores();

        if (playerList.containsKey(newPlayerName)){
            DialogFragment addPlayerError = new AddPlayerError();
            addPlayerError.show(getSupportFragmentManager(), "addPlayerError");
        }
        else{
            playerList.put(newPlayerName, 0);

        }
        displayScores();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {
        //Action cancelled
        dialogFragment.getDialog().cancel();
    }
}
