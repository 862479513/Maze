package com.example.stepan.maze04;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    public static float scl = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button settings = findViewById(R.id.settings_button);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                SettingsActivity.light = false;
                SettingsActivity.teleportation = false;
                SettingsActivity.enemies = false;
                SettingsActivity.collection = false;
                SettingsActivity.COLS = 17;
                SettingsActivity.ROWS = 25;
                startActivity(i);
            }
        });

        Button classic = findViewById(R.id.classic_button);
        classic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                SettingsActivity.light = true;
                SettingsActivity.teleportation = false;
                SettingsActivity.enemies = false;
                SettingsActivity.collection = false;
                SettingsActivity.COLS = 17;
                SettingsActivity.ROWS = 25;
                startActivity(i);
            }
        });

        Button hunted = findViewById(R.id.hunted_button);
        hunted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                SettingsActivity.light = true;
                SettingsActivity.teleportation = true;
                SettingsActivity.enemies = true;
                SettingsActivity.collection = false;
                SettingsActivity.COLS = 17;
                SettingsActivity.ROWS = 25;
                startActivity(i);
            }
        });

        Button night = findViewById(R.id.night_button);
        night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                SettingsActivity.light = false;
                SettingsActivity.teleportation = false;
                SettingsActivity.enemies = false;
                SettingsActivity.collection = false;
                SettingsActivity.COLS = 17;
                SettingsActivity.ROWS = 25;
                startActivity(i);
            }
        });

        Button collector = findViewById(R.id.collector_button);
        collector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                SettingsActivity.light = true;
                SettingsActivity.teleportation = true;
                SettingsActivity.enemies = false;
                SettingsActivity.collection = true;
                SettingsActivity.COLS = 17;
                SettingsActivity.ROWS = 25;
                startActivity(i);
            }
        });

        Button maze = findViewById(R.id.maze_button);
        maze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                SettingsActivity.light = true;
                SettingsActivity.teleportation = true;
                SettingsActivity.enemies = true;
                SettingsActivity.collection = true;
                SettingsActivity.COLS = 17;
                SettingsActivity.ROWS = 25;
                startActivity(i);
            }
        });

        Button ultimate = findViewById(R.id.ultimate_button);
        ultimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                SettingsActivity.light = false;
                SettingsActivity.teleportation = true;
                SettingsActivity.enemies = true;
                SettingsActivity.collection = true;
                SettingsActivity.COLS = 17;
                SettingsActivity.ROWS = 25;
                startActivity(i);
            }
        });

        SeekBar speed = findViewById(R.id.seekBar1);
        speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress){
                    case 0:
                        scl = 30;
                        break;
                    case 1:
                        scl = 28;
                        break;
                    case 2:
                        scl = 26;
                        break;
                    case 3:
                        scl = 24;
                        break;
                    case 4:
                        scl = 22;
                        break;
                    case 5:
                        scl = 20;
                        break;
                    case 6:
                        scl = 18;
                        break;
                    case 7:
                        scl = 16;
                        break;
                    case 8:
                        scl = 14;
                        break;
                    case 9:
                        scl = 12;
                        break;
                    case 10:
                        scl = 10;
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
