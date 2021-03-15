package com.example.notscattergories;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText noOfSecondsInput;
    private EditText noOfCategoriesInput;
    private Button submitBtn;

    private int noOfSeconds;
    private int noOfCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);

        noOfSecondsInput = findViewById(R.id.editTimer);
        noOfCategoriesInput = findViewById(R.id.editCategories);

        submitBtn = findViewById(R.id.confirmSettingsBtn);
        submitBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmSettingsBtn:
                noOfSeconds = Integer.parseInt(noOfSecondsInput.getText().toString());
                noOfCategories = Integer.parseInt(noOfCategoriesInput.getText().toString());

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("time", (noOfSeconds * 1000));
                editor.putInt("categories", (noOfCategories));
                editor.commit();
                Toast.makeText(getApplicationContext(), "DONE", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}