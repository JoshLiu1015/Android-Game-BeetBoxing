package com.cs407.beet_boxing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.HashMap;

public class share_recording extends AppCompatActivity {

    MediaPlayer player;
    Button playButton;
    SeekBar seekBar;
    Handler handler = new Handler();
    Runnable runnable;

    private static final int[] BUTTON_PRODUCE_IDS = {
            R.id.button_placeholder_1, R.id.button_placeholder_2, R.id.button_placeholder_3,
            R.id.button_placeholder_4, R.id.button_placeholder_5, R.id.button_placeholder_6,
            R.id.button_placeholder_7, R.id.button_placeholder_8, R.id.button_placeholder_9
    };

    private static final String[] PLACEHOLDER_NAME = {
            "placeholder_1", "placeholder_2", "placeholder_3",
            "placeholder_4", "placeholder_5", "placeholder_6",
            "placeholder_7", "placeholder_8", "placeholder_9"
    };

    private static final int[] ICON_IDS = {
            R.id.icon_carrot, R.id.icon_banana, R.id.icon_apple,
            R.id.icon_potato, R.id.icon_onion, R.id.icon_orange,
            R.id.icon_melon, R.id.icon_ginger, R.id.icon_beet
    };

    private static final String[] PRODUCE_NAME = {
            "carrot", "banana", "apple",
            "potato", "onion", "orange",
            "melon", "ginger", "beet"
    };

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


        // get the hashMap passed from ActivityGarden
        Intent intent = getIntent();
        HashMap<Integer, Integer> recordMap = (HashMap<Integer, Integer>) intent.getSerializableExtra("recordMap");

        if (recordMap != null) {
            // loop over all the produce IDs to see if any is in the hash map
            for (int i = 0; i < ICON_IDS.length; i++) {
                // if produce found
                if (recordMap.containsKey(ICON_IDS[i])) {
                    // check its name
                    System.out.println(PRODUCE_NAME[i] + " is in the garden");

                    // loop over all the placeholder IDs
                    for (int k = 0; k < BUTTON_PRODUCE_IDS.length; k++) {
                        // if the placeholder that contains the produce is found
                        if (recordMap.get(ICON_IDS[i]) == BUTTON_PRODUCE_IDS[k]) {
                            // check its name
                            System.out.println(PRODUCE_NAME[i] + "'s corresponding placeholder is " + PLACEHOLDER_NAME[k]);
                        }
                    }
                }
            }
        }
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
