package com.cs407.beet_boxing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class share_recording extends AppCompatActivity {

    MediaPlayer player;
    Button playButton;
    SeekBar seekBar;
    Handler handler = new Handler();
    Runnable runnable;
    private String recordedFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_recording);

        playButton = findViewById(R.id.playButton);
        seekBar = findViewById(R.id.seekBar);
        playButton.setTag("PLAY");

        runnable = new Runnable() {
            @Override
            public void run() {
                if (player != null && player.isPlaying()) {
                    seekBar.setProgress(player.getCurrentPosition());
                }
                handler.postDelayed(this, 500);
            }
        };

        recordedFilePath = getIntent().getStringExtra("RECORDED_FILE_PATH");
        if (recordedFilePath != null) {
            initializePlayer(recordedFilePath);
        }

        // Add buttons and listeners for save and discard actions
        Button btnSave = findViewById(R.id.save);
        Button btnDiscard = findViewById(R.id.record);

        btnSave.setOnClickListener(v -> showSaveFileDialog());
        btnDiscard.setOnClickListener(v -> discardRecording());
    }

    public void togglePlayStop(View v){
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.song);
            seekBar.setMax(player.getDuration());
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }

        if (playButton.getTag().equals("PLAY")) {
            player.start();
            playButton.setTag("STOP");
            playButton.setText("Stop");
            updateSeekBar();
        } else {
            if(player.isPlaying()){
                player.pause();
            }
            playButton.setTag("PLAY");
            playButton.setText("Play");
        }
    }

    private void updateSeekBar() {
        if (player != null) {
            seekBar.setProgress(player.getCurrentPosition());
            if (player.isPlaying()) {
                runnable.run();
            }
        }
    }

    private void initializePlayer(String filePath) {
        player = new MediaPlayer();
        try {
            player.setDataSource(filePath);
            player.prepare();
            seekBar.setMax(player.getDuration());
            player.setOnCompletionListener(mp -> stopPlayer());
        } catch (IOException e) {
            Toast.makeText(this, "Unable to play recording", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void showSaveFileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Recording File");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Enter file name");
        input.setBackground(ContextCompat.getDrawable(this, R.drawable.input_background));
        input.setPadding(32, 32, 32, 32);
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String filename = input.getText().toString();
                saveRecording(filename);
            }
        });

        builder.show();
    }

    private void saveRecording(String filename) {
        if (filename.isEmpty()) {
            Toast.makeText(this, "Filename cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        File originalFile = new File(recordedFilePath);
        File newFile = new File(getFilesDir(), filename + ".wav");

        if (originalFile.renameTo(newFile)) {
            Toast.makeText(this, "File saved: " + newFile.getName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RecordingsListActivity.class);
            startActivity(intent);
            // Optionally, navigate to another activity or update UI
        } else {
            Toast.makeText(this, "Error saving file", Toast.LENGTH_SHORT).show();
        }
    }

    private void discardRecording() {
        // Delete the recording
        File file = new File(recordedFilePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                Toast.makeText(this, "Recording discarded", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to discard recording", Toast.LENGTH_SHORT).show();
                return; // Return if unable to delete the file
            }
        } else {
            Toast.makeText(this, "Recording file not found", Toast.LENGTH_SHORT).show();
            return; // Return if the file does not exist
        }

        // Navigate back to RecordingMode activity
        Intent intent = new Intent(this, RecordingMode.class);
        startActivity(intent);
    }


    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
            playButton.setTag("PLAY");
            playButton.setText("Play");
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (player != null && fromUser) {
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Intentionally empty
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (player != null) {
                    if (!player.isPlaying()) {
                        player.start();
                        playButton.setTag("STOP");
                        playButton.setText("Stop");
                        updateSeekBar();
                    }
                }
            }
        });
    }
}