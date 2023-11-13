package com.cs407.beet_boxing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Button goToGarden = findViewById(R.id.gardenButton);
        goToGarden.setOnClickListener( event ->
            startActivity(new Intent(this, ActivityGarden.class))
        );
    }
}