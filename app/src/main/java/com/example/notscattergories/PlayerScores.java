package com.example.notscattergories;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class PlayerScores extends AppCompatActivity implements View.OnClickListener {
    HashMap<String, Integer> scores = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        Button back = findViewById(R.id.btnBackToGame);

        back.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBackToGame:
                Intent backToGame = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(backToGame);
                break;
            case R.id.btnAnimalSound:
                //Intent webSocket = new Intent(getApplicationContext(), AnimalSounds.class);
                //startActivity(webSocket);
        }
    }

    private void displayScores() {

    }


}
