package com.cs407.beet_boxing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> goToActivity());
    }

    private void goToActivity() {
        Intent intent=new Intent(this, ActivityTiltGame.class);
        startActivity(intent);

    }
}