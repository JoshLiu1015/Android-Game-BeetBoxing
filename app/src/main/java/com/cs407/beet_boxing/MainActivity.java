package com.cs407.beet_boxing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.cs407.beet_boxing.persistence.PersistentInfo;

public class MainActivity extends AppCompatActivity {
    private Button startButton;
    private ImageButton setting;
    private Button skip;
    private ImageView help;

    private Context context;

    @Override   
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setting = findViewById(R.id.setting);
        startButton = findViewById(R.id.startButton);
        skip = findViewById(R.id.skip);

        setting.setOnClickListener(this::openSetting);
        startButton.setOnClickListener(this::startGame);
        skip.setOnClickListener(this::skip);
        help = findViewById(R.id.help);

        setting.setOnClickListener(this::openSetting);
        startButton.setOnClickListener(this::startGame);
        help.setOnClickListener(this::openHelp);

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

    public void openHelp(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    public void skip(View view){
        Intent intent = new Intent(this, RecordingMode.class);
        startActivity(intent);
    }
}