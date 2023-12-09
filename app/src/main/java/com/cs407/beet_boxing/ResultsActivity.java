package com.cs407.beet_boxing;

import static com.cs407.beet_boxing.persistence.PersistentInfo.GSON;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.cs407.beet_boxing.persistence.GameData;
import com.cs407.beet_boxing.persistence.PersistentInfo;
import com.cs407.beet_boxing.util.DummyInventory;
import com.cs407.beet_boxing.util.EnumProduceType;

import java.util.Map;

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
        GameData gameData = PersistentInfo.getGameData();
        assert gameData != null;

        if (score > gameData.getHighScore()) {
            gameData.setHighScore(score);
        }
        TextView highScoreText = findViewById(R.id.highScore);
        highScoreText.setText("(Best: " + gameData.getHighScore() + ")");

        DummyInventory inventory = GSON.fromJson(intent.getStringExtra("collected"), DummyInventory.class);
        updateQuantityTextViews(inventory);

        // Save the new produce items to the PersistentInfo
        for (Map.Entry<EnumProduceType, Integer> entry : inventory.collected.entrySet()) {
            int previousAmount = gameData.getInventory().getOrDefault(entry.getKey(), 0);
            gameData.getInventory().put(entry.getKey(), previousAmount + entry.getValue());
        }

        // exit path
        Button goToGarden = findViewById(R.id.gardenButton);
        goToGarden.setOnClickListener( event ->
            startActivity(new Intent(this, ActivityGarden.class))
        );
    }

    private void updateQuantityTextViews(DummyInventory inventory) {
        for (Map.Entry<EnumProduceType, Integer> entry : inventory.collected.entrySet()) {
            switch (entry.getKey()) {
                case CARROT -> {
                    TextView carrotQuantity = findViewById(R.id.carrotResultAmount);
                    carrotQuantity.setText(entry.getValue().toString());
                }
                case BANANA -> {
                    TextView bananaQuantity = findViewById(R.id.bananaResultAmount);
                    bananaQuantity.setText(entry.getValue().toString());
                }
                case APPLE -> {
                    TextView appleQuantity = findViewById(R.id.appleResultAmount);
                    appleQuantity.setText(entry.getValue().toString());
                }
                case POTATO -> {
                    TextView potatoQuantity = findViewById(R.id.potatoResultAmount);
                    potatoQuantity.setText(entry.getValue().toString());
                }
                case ONION -> {
                    TextView onionQuantity = findViewById(R.id.onionResultAmount);
                    onionQuantity.setText(entry.getValue().toString());
                }
                case ORANGE -> {
                    TextView orangeQuantity = findViewById(R.id.orangeResultAmount);
                    orangeQuantity.setText(entry.getValue().toString());
                }
                case MELON -> {
                    TextView melonQuantity = findViewById(R.id.melonResultAmount);
                    melonQuantity.setText(entry.getValue().toString());
                }
                case GINGER -> {
                    TextView gingerQuantity = findViewById(R.id.gingerResultAmount);
                    gingerQuantity.setText(entry.getValue().toString());
                }
                case BEET -> {
                    TextView beetQuantity = findViewById(R.id.beetResultAmount);
                    beetQuantity.setText(entry.getValue().toString());
                }
            }
        }
    }
}