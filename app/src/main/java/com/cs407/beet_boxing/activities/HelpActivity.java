package com.cs407.beet_boxing.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.cs407.beet_boxing.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(e -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}