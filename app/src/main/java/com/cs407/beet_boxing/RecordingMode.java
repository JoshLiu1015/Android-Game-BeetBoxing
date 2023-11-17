package com.cs407.beet_boxing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;


import android.content.ClipData;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecordingMode extends AppCompatActivity {
    private Button getFile;
    private TextView countdownTimerTextView;

    private Button recordButton;
    private ProgressBar timerProgressBar;
    private static final int MY_PERMISSIONS_REQUEST = 1;
    // Declare variable
    private boolean isRecording = false;
    private MediaRecorder mediaRecorder;
    private String outputFile;
    private CountDownTimer countDownTimer;

    private ImageButton buttonProduce1;
    private ImageButton buttonProduce2;
    private ImageButton buttonProduce3;
    private ImageButton buttonProduce4;
    private ImageButton buttonProduce5;
    private ImageButton buttonProduce6;
    private ImageButton buttonProduce7;
    private ImageButton buttonProduce8;
    private ImageButton buttonProduce9;

    private HashMap<Integer, MediaPlayer> mediaPlayers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_mode);



        // Initialize
        timerProgressBar = findViewById(R.id.timer_progressbar);
        countdownTimerTextView = findViewById(R.id.countdown_timer);


        // Initialize buttons
        buttonProduce1 = findViewById(R.id.button_produce1);
        buttonProduce2 = findViewById(R.id.button_produce2);
        buttonProduce3 = findViewById(R.id.button_produce3);
        buttonProduce4 = findViewById(R.id.button_produce4);
        buttonProduce5 = findViewById(R.id.button_produce5);
        buttonProduce6 = findViewById(R.id.button_produce6);
        buttonProduce7 = findViewById(R.id.button_produce7);
        buttonProduce8 = findViewById(R.id.button_produce8);
        buttonProduce9 = findViewById(R.id.button_produce9);

        buttonProduce1.setImageResource(R.drawable.melon);
        buttonProduce2.setImageResource(R.drawable.onion);
        buttonProduce3.setImageResource(R.drawable.orange);
        buttonProduce4.setImageResource(R.drawable.potato);
        buttonProduce5.setImageResource(R.drawable.melon);
        buttonProduce6.setImageResource(R.drawable.onion);
        buttonProduce7.setImageResource(R.drawable.orange);
        buttonProduce8.setImageResource(R.drawable.potato);
        buttonProduce9.setImageResource(R.drawable.melon);


        buttonProduce1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSound(R.raw.drum_loop, buttonProduce1.getId());
            }
        });
        buttonProduce2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSound(R.raw.electric_guitar, buttonProduce2.getId());
            }
        });
        buttonProduce3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSound(R.raw.banjo, buttonProduce3.getId());
            }
        });
        buttonProduce4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSound(R.raw.beatbox, buttonProduce4.getId());
            }
        });
        buttonProduce5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSound(R.raw.drum_loop, buttonProduce5.getId());
            }
        });
        buttonProduce6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSound(R.raw.electric_guitar, buttonProduce6.getId());
            }
        });
        buttonProduce7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSound(R.raw.banjo, buttonProduce7.getId());
            }
        });
        buttonProduce8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSound(R.raw.beatbox, buttonProduce8.getId());
            }
        });
        buttonProduce9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSound(R.raw.drum_loop, buttonProduce9.getId());
            }
        });
        // Initialize mediaPlayers HashMap
        mediaPlayers = new HashMap<>();


        getFile = findViewById(R.id.get);
        getFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecordings();
            }
        });
        recordButton = findViewById(R.id.record); // Replace with your record button's ID
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is a simplified example. In actual implementation, handle permissions properly.
                if (ContextCompat.checkSelfPermission(RecordingMode.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RecordingMode.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
                }
                else{
                    if (!isRecording) {
                        startRecording();
                    } else {
                        stopRecording();
                    }
                }
            }
        });


    }


    private void toggleSound(int soundResourceId, int buttonId) {
        // Check if the button already has a MediaPlayer associated with it
        if (mediaPlayers.containsKey(buttonId)) {
            MediaPlayer player = mediaPlayers.get(buttonId);
            if (player.isPlaying()) {
                player.pause(); // Pause playback
            } else {
                player.start(); // Resume playback
            }
        } else {
            // No MediaPlayer for this button yet, create and start it
            MediaPlayer player = MediaPlayer.create(this, soundResourceId);
            player.setLooping(true);
            player.start();
            mediaPlayers.put(buttonId, player); // Store it in the map
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release all MediaPlayer resources
        for (MediaPlayer player : mediaPlayers.values()) {
            if (player != null) {
                if (player.isPlaying()) {
                    player.stop();
                }
                player.release();
            }
        }
        mediaPlayers.clear();
    }

    private void startRecording() {
        setupRecording();
        recordButton.setText("Recording...");
        recordButton.setEnabled(false); // Disable the button while recording
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            timerProgressBar.setVisibility(View.VISIBLE);
            timerProgressBar.setProgress(0);
            startCountDownTimer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            timerProgressBar.setVisibility(View.GONE);
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
        }
    }
    private void startCountDownTimer() {
        final int totalTime = 30000; // 30 seconds in milliseconds
        countDownTimer = new CountDownTimer(totalTime, 1000) {
            public void onTick(long millisUntilFinished) {
                int progress = (int) ((totalTime - millisUntilFinished) / 1000);
                timerProgressBar.setProgress(progress);
                countdownTimerTextView.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                stopRecording();
                stopAllMediaPlayers();
                recordButton.setText("Record");
                recordButton.setEnabled(true); // Re-enable the button
                countdownTimerTextView.setText("0");
                Toast.makeText(RecordingMode.this, "Recording has stopped", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void stopAllMediaPlayers() {
        for (MediaPlayer player : mediaPlayers.values()) {
            if (player != null && player.isPlaying()) {
                player.stop();
                player.release();
            }
        }
        mediaPlayers.clear();
    }

    private void setupRecording() {
        int fileNumber = countRecordings() + 1;
        String fileName = "recording_" + fileNumber + ".3gp"; // Unique file name
        File file = new File(getFilesDir(), fileName); // Saving in internal storage
        outputFile = file.getAbsolutePath();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(outputFile);
    }

    private int countRecordings() {
        File directory = getFilesDir();
        File[] files = directory.listFiles();
        int count = 0;
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".3gp")) {
                    count++;
                }
            }
        }
        return count;
    }

    private List<String> getRecordings() {
        File directory = getFilesDir();
        File[] files = directory.listFiles();
        List<String> fileNames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".3gp")) {
                    fileNames.add(file.getName());
                }
            }
        }
        return fileNames;
    }



}