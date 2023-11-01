package com.cs407.beet_boxing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cs407.beet_boxing.persistence.PersistentInfo;
import com.cs407.beet_boxing.util.EnumProduceType;

public class MainActivity extends AppCompatActivity {
    private Button startButton;
    private ImageButton setting;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = findViewById(R.id.startButton);
        setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSetting();
            }
        });


    }

    public void startGame(View view){
        Log.i("INFO", "Button Pressed");
        Toast.makeText(this, "start game", Toast.LENGTH_SHORT).show();
    }

    public void openSetting(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        context = getApplicationContext();

        int loadResult = PersistentInfo.init(context);
        switch (loadResult) {
            case 1 -> Log.i("ERROR", "Error loading config.");
            case 2 -> Log.i("ERROR", "Error loading game data.");
        }

        // Example usage of setting values:
        // TODO: remove when implementing garden
        PersistentInfo.gameData.inventory.put(EnumProduceType.APPLE, 3);
        PersistentInfo.config.volumeMultiplier = 1f;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        PersistentInfo.saveGameData(context);
        PersistentInfo.saveConfig(context);
    }
}