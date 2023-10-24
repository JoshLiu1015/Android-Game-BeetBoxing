package com.cs407.beet_boxing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button startButton;
    private ImageButton setting;
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
    }
}