package com.cs407.beet_boxing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cs407.beet_boxing.persistence.PersistentInfo;

public class MainActivity extends AppCompatActivity {
    private Button startButton;
    private ImageButton setting;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setting = findViewById(R.id.setting);
        startButton = findViewById(R.id.startButton);

        setting.setOnClickListener(this::openSetting);
        startButton.setOnClickListener(this::startGame);

        context = getApplicationContext();
        int loadResult = PersistentInfo.init(context);
        switch (loadResult) {
            case 1 -> Log.i("ERROR", "Error loading config.");
            case 2 -> Log.i("ERROR", "Error loading game data.");
            case 3 -> Log.i("INFO", "Config already initialized.");
        }
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, ActivityTiltGame.class);
        startActivity(intent);
    }

    public void openSetting(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        PersistentInfo.saveGameData(context);
        PersistentInfo.saveConfig(context);
    }
}