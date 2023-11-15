package com.cs407.beet_boxing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.cs407.beet_boxing.persistence.PersistentInfo;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Update fields to reflect results
        Intent intent = getIntent();
        TextView scoreText = findViewById(R.id.score);
        int score = intent.getIntExtra("score", -1);
        scoreText.setText("Score: " + score);
        if (score > PersistentInfo.gameData.highScore) {
            PersistentInfo.gameData.highScore = score;
        }
        TextView highScoreText = findViewById(R.id.highScore);
        highScoreText.setText("(Best: " + PersistentInfo.gameData.highScore + ")");

        // exit path
        Button goToGarden = findViewById(R.id.gardenButton);
        goToGarden.setOnClickListener( event ->
            startActivity(new Intent(this, ActivityGarden.class))
        );
    }
}