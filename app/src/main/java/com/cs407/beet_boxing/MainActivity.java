package com.cs407.beet_boxing;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cs407.beet_boxing.persistence.PersistentInfo;
import com.cs407.beet_boxing.util.EnumProduceType;

public class MainActivity extends AppCompatActivity {

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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