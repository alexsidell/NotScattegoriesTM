package com.example.notscattergories;

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
                Intent backToGame = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(backToGame);
                break;
            case R.id.btnAddPlayer:
                //send pop up
                addPlayer();
                break;
            default:
                int num = v.getId();
                TextView playerView;
                TextView scoreView;
                if(num%4==0){
                    //Add a point
                    scoreView = findViewById(num-1);
                    playerView = findViewById(num-3);
                    String playerName = playerView.getText().toString();
                    Integer playerScore = Integer.parseInt(scoreView.getText().toString());
                    playerList.put(playerName,playerScore+1);
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
            addPoint.setOnClickListener(this::onClick);
            addPoint.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add,0,0,0);
            addPoint.setId(idCounter);
            idCounter++;
            playerLayout.addView(addPoint);


            playerLayout.setWeightSum(2);
            playerView.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            scoreView.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            playerListInner.addView(playerLayout);
        }

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment, String newPlayerName) {
        //Add player to player list
        try {
            playerList.put(newPlayerName,0);
        }catch (Exception e){System.out.println("Not added to Hashmap");}
        displayScores();

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {
        //Action cancelled
        dialogFragment.getDialog().cancel();
    }

}
