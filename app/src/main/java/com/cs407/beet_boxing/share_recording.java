package com.cs407.beet_boxing;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class share_recording extends AppCompatActivity {

    MediaPlayer player;
    Button playButton;
    SeekBar seekBar;
    Handler handler = new Handler();
    Runnable runnable;

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
