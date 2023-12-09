package com.cs407.beet_boxing;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordingsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<File> recordingsList = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private int playingPosition = -1;
    private Button currentPlayingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings_list);

        recyclerView = findViewById(R.id.recyclerView_recordings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadRecordings();
        recyclerView.setAdapter(new RecordingsAdapter());

        Button btnBackToGarden = findViewById(R.id.btn_back_to_garden);
        btnBackToGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordingsListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadRecordings() {
        File directory = getFilesDir();
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".wav"));
        if (files != null) {
            recordingsList = Arrays.asList(files);
        }
    }

    private void playRecording(File recordingFile, Button playButton) {
        if (mediaPlayer != null && playingPosition == recordingsList.indexOf(recordingFile)) {
            // Recording is currently playing, so stop it
            mediaPlayer.release();
            mediaPlayer = null;
            playingPosition = -1;
            playButton.setText("Play");
            currentPlayingButton = null;
        } else {
            if (mediaPlayer != null) {
                // Stop the current playing recording
                mediaPlayer.release();
                mediaPlayer = null;
                if (currentPlayingButton != null) {
                    currentPlayingButton.setText("Play");
                }
            }

            // Start a new playback
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(recordingFile.getAbsolutePath());
                mediaPlayer.prepare();
                mediaPlayer.start();
                playingPosition = recordingsList.indexOf(recordingFile);
                playButton.setText("Stop");
                currentPlayingButton = playButton;

                mediaPlayer.setOnCompletionListener(mp -> {
                    stopPlaying();
                    playButton.setText("Play");
                });

            } catch (IOException e) {
                Toast.makeText(RecordingsListActivity.this, "Unable to play recording", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            playingPosition = -1;
            if (currentPlayingButton != null) {
                currentPlayingButton.setText("Play");
            }
        }
    }

    private class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recording_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            File recording = recordingsList.get(position);
            holder.tvRecordingName.setText(recording.getName());
            holder.btnPlay.setText(position == playingPosition ? "Stop" : "Play");
            holder.btnPlay.setOnClickListener(v -> {
                playRecording(recording, holder.btnPlay);
            });
        }

        @Override
        public int getItemCount() {
            return recordingsList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvRecordingName;
            Button btnPlay;

            ViewHolder(View itemView) {
                super(itemView);
                tvRecordingName = itemView.findViewById(R.id.tvRecordingName);
                btnPlay = itemView.findViewById(R.id.btnPlay);
            }
        }
    }
}
