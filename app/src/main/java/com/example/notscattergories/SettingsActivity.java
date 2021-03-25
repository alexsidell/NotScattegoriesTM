package com.example.notscattergories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
                makeNewChanges();
                break;
            case R.id.rules:
                showRules();
            case R.id.tour:
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("first_time", true);
                editor.commit();
                finish();
                Intent runTutorial = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(runTutorial);
                break;
            default:
                break;
        }
    }

    private void showRules() {
        DialogFragment newFragment = new RulesDialog();
        newFragment.show(getSupportFragmentManager(), "rules");
    }

    private boolean isEmpty(EditText setNumber) {
        return !(setNumber.getText().toString().trim().length() > 0);
    }

    private void setNumberOfSeconds(SharedPreferences.Editor editor) {
        if (!isEmpty(noOfSecondsInput)) {
            noOfSeconds = Integer.parseInt(noOfSecondsInput.getText().toString());

            if (noOfSeconds < 10) {
                noOfSeconds = 10;
                Toast.makeText(getApplicationContext(), "Timer set to minimum (10 secs).", Toast.LENGTH_SHORT).show();
            } else if (noOfSeconds > 180) {
                noOfSeconds = 180;
                Toast.makeText(getApplicationContext(), "Timer set to maximum (180 secs).", Toast.LENGTH_SHORT).show();
            }

            editor.putInt("time", ((noOfSeconds) * 1000));
        }
    }

    private void setNumberOfCategories(SharedPreferences.Editor editor) {
        if (!isEmpty(noOfCategoriesInput)) {
            noOfCategories = Integer.parseInt(noOfCategoriesInput.getText().toString());

            if (noOfCategories < 3) {
                noOfCategories = 3;
                Toast.makeText(getApplicationContext(), "Number of categories set to minimum (3).", Toast.LENGTH_SHORT).show();
            } else if (noOfCategories > 12) {
                noOfCategories = 12;
                Toast.makeText(getApplicationContext(), "Number of categories set to maximum (12).", Toast.LENGTH_SHORT).show();
            }

            editor.putInt("categories", (noOfCategories));
        }
    }

    private void makeNewChanges() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        setNumberOfSeconds(editor);
        setNumberOfCategories(editor);
        editor.commit();
        Toast.makeText(getApplicationContext(), "Changes Made", Toast.LENGTH_SHORT).show();
    }

}