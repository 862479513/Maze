package com.example.stepan.maze04;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, Switch.OnCheckedChangeListener {
    TextView textViewCols, textViewRows;

    public static boolean light = false, enemies = false, collection = false, teleportation = false;
    public static int COLS = 17, ROWS = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        textViewCols = findViewById(R.id.textViewCols);
        textViewRows = findViewById(R.id.textViewRows);

        Button buttonStart = findViewById(R.id.buttonStart);
        Button buttonMoreRows = findViewById(R.id.buttonMoreRows);
        Button buttonLessRows = findViewById(R.id.buttonLessRows);
        Button buttonMoreCols = findViewById(R.id.buttonMoreCols);
        Button buttonLessCols = findViewById(R.id.buttonLessCols);

        Switch switchLight = findViewById(R.id.switchLight);
        Switch switchCollectibles = findViewById(R.id.switchCollectibles);
        Switch switchTeleports = findViewById(R.id.switchTeleports);
        Switch switchEnemies = findViewById(R.id.switchEnemies);

        buttonStart.setOnClickListener(this);
        buttonLessCols.setOnClickListener(this);
        buttonLessRows.setOnClickListener(this);
        buttonMoreCols.setOnClickListener(this);
        buttonMoreRows.setOnClickListener(this);

        switchLight.setOnCheckedChangeListener(this);
        switchCollectibles.setOnCheckedChangeListener(this);
        switchTeleports.setOnCheckedChangeListener(this);
        switchEnemies.setOnCheckedChangeListener(this);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        int amountCols;
        int amountRows;

        switch (v.getId()) {
            case R.id.buttonStart:
                Intent i = new Intent(SettingsActivity.this, GameActivity.class);
                startActivity(i);
                break;
            case R.id.buttonMoreCols:
                amountCols = Integer.parseInt((String) textViewCols.getText()) + 1;
                if (amountCols > 30) {
                    amountCols = 30;
                }
                COLS = amountCols;
                textViewCols.setText(Integer.toString(amountCols));
                break;
            case R.id.buttonLessCols:
                amountCols = Integer.parseInt((String) textViewCols.getText()) - 1;
                if (amountCols < 3) {
                    amountCols = 3;
                }
                COLS = amountCols;
                textViewCols.setText(Integer.toString(amountCols));
                break;
            case R.id.buttonMoreRows:
                amountRows = Integer.parseInt((String) textViewRows.getText()) + 1;
                if (amountRows > 30) {
                    amountRows = 30;
                }
                ROWS = amountRows;
                textViewRows.setText(Integer.toString(amountRows));
                break;
            case R.id.buttonLessRows:
                amountRows = Integer.parseInt((String) textViewRows.getText()) - 1;
                if (amountRows < 3) {
                    amountRows = 3;
                }
                ROWS = amountRows;
                textViewRows.setText(Integer.toString(amountRows));
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switchLight:
                light = isChecked;
                break;
            case R.id.switchCollectibles:
                collection = isChecked;
                break;
            case R.id.switchTeleports:
                teleportation = isChecked;
                break;
            case  R.id.switchEnemies:
                enemies = isChecked;
                break;
        }

    }
}
