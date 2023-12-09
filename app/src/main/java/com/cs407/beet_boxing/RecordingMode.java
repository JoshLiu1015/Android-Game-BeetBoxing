package com.cs407.beet_boxing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioPlaybackCaptureConfiguration;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecordingMode extends AppCompatActivity {
    private static final int SHARE_RECORDING_REQUEST = 1;

    private TextView countdownTimerTextView;

    private Button recordButton;
    private ProgressBar timerProgressBar;
    // Declare variable
    private boolean isRecording = false;
    private String outputFile;
    private CountDownTimer countDownTimer;


    private HashMap<Integer, MediaPlayer> mediaPlayers;
    private HashMap<Integer, Integer> recordMap;

    private static final int AUDIO_CAPTURE_PERMISSION_REQUEST = 1;
    private static final int MEDIA_PROJECTION_REQUEST = 2;

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private AudioRecord audioRecord;
    private boolean isCapturing = false;
    private Thread capturingThread;

    private File audioFile;

    private ImageButton buttonProduce1;
    private ImageButton buttonProduce2;
    private ImageButton buttonProduce3;
    private ImageButton buttonProduce4;
    private ImageButton buttonProduce5;
    private ImageButton buttonProduce6;
    private ImageButton buttonProduce7;
    private ImageButton buttonProduce8;
    private ImageButton buttonProduce9;

    private Button btnBackToGarden;


    private static final int[] ON_IMAGE_SRCS = {
            R.drawable.carroton, R.drawable.bananaon, R.drawable.appleon,
            R.drawable.potatoon, R.drawable.onionon, R.drawable.orangeon,
            R.drawable.melonon, R.drawable.gingeron, R.drawable.beeton
    };



    private static final int[] ICON_IDS = {
            R.id.icon_carrot, R.id.icon_banana, R.id.icon_apple,
            R.id.icon_potato, R.id.icon_onion, R.id.icon_orange,
            R.id.icon_melon, R.id.icon_ginger, R.id.icon_beet
    };


    private static final int[] SOUND_RESOURCE_IDS = {
            R.raw.violin_trimmed, R.raw.guitar_trimmed, R.raw.piano_trimmed,
            R.raw.drumset2_trimmed, R.raw.meow_trimmed, R.raw.stabs_trimmed,
            R.raw.xylo_trimmed, R.raw.synth_trimmed, R.raw.drumset1_trimmed
    };

    private static final int[] BUTTON_PRODUCE_IDS = {
            R.id.button_placeholder_1, R.id.button_placeholder_2, R.id.button_placeholder_3,
            R.id.button_placeholder_4, R.id.button_placeholder_5, R.id.button_placeholder_6,
            R.id.button_placeholder_7, R.id.button_placeholder_8, R.id.button_placeholder_9
    };

    private static final String[] PRODUCE_NAME = {
            "carrot", "banana", "apple",
            "potato", "onion", "orange",
            "melon", "ginger", "beet"
    };

    private static final String[] PLACEHOLDER_NAME = {
            "placeholder_1", "placeholder_2", "placeholder_3",
            "placeholder_4", "placeholder_5", "placeholder_6",
            "placeholder_7", "placeholder_8", "placeholder_9"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_mode);
        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        audioFile = new File(getFilesDir(), "captured_audio1.pcm");

        buttonProduce1 = findViewById(R.id.button_produce1);
        buttonProduce2 = findViewById(R.id.button_produce2);
        buttonProduce3 = findViewById(R.id.button_produce3);
        buttonProduce4 = findViewById(R.id.button_produce4);
        buttonProduce5 = findViewById(R.id.button_produce5);
        buttonProduce6 = findViewById(R.id.button_produce6);
        buttonProduce7 = findViewById(R.id.button_produce7);
        buttonProduce8 = findViewById(R.id.button_produce8);
        buttonProduce9 = findViewById(R.id.button_produce9);

        btnBackToGarden = findViewById(R.id.back);
        btnBackToGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAllMediaPlayers();
                Intent intent = new Intent(RecordingMode.this, ActivityGarden.class);
                startActivity(intent);
            }
        });

        // Retrieve the recordMap
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("recordMap")) {
            recordMap = (HashMap<Integer, Integer>) intent.getSerializableExtra("recordMap");
        } else{
            recordMap = new HashMap<>();
            recordMap.put(2131362079, 2131361903);
            recordMap.put(2131362077, 2131361904); // Note: keys should be unique
            recordMap.put(2131362076, 2131361905);
            recordMap.put(2131362086, 2131361906);
            recordMap.put(2131362084, 2131361907);
            recordMap.put(2131362085, 2131361908);
            recordMap.put(2131362083, 2131361909);
            recordMap.put(2131362081, 2131361910);
            recordMap.put(2131362078, 2131361911);
        }

        // Set up UI based on recordMap
        if (recordMap != null) {
            for (int j = 0; j<BUTTON_PRODUCE_IDS.length; j ++){
                Log.d("RecordingMode", "this should be hashmap value " + BUTTON_PRODUCE_IDS[j]);
            }

            for (int d = 0; d<ICON_IDS.length; d ++){
                Log.d("RecordingMode", "this should be hashmap key " + ICON_IDS[d]);
            }
            for (int i = 0; i < ICON_IDS.length; i++) {
                // if produce found
                if (recordMap.containsKey(ICON_IDS[i])) {
                    // check its name
                    Log.d("RecordingMode", PRODUCE_NAME[i] + " is in the garden");

                    // loop over all the placeholder IDs
                    for (int k = 0; k < BUTTON_PRODUCE_IDS.length; k++) {
                        // if the placeholder that contains the produce is found
                        if (recordMap.get(ICON_IDS[i]) == BUTTON_PRODUCE_IDS[k]) {
                            final int index = i;
                            Log.d("RecordingMode", PRODUCE_NAME[i] + "'s corresponding placeholder is " + PLACEHOLDER_NAME[k]);
                            if (PLACEHOLDER_NAME[k].equals("placeholder_1")){
                                buttonProduce1.setImageResource(ON_IMAGE_SRCS[i]);
                                buttonProduce1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce1.getId());
                                    }
                                });
                            } else if (PLACEHOLDER_NAME[k].equals("placeholder_2")) {
                                buttonProduce2.setImageResource(ON_IMAGE_SRCS[i]);
                                buttonProduce2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce2.getId());
                                    }
                                });
                            } else if (PLACEHOLDER_NAME[k].equals("placeholder_3")){
                                buttonProduce3.setImageResource(ON_IMAGE_SRCS[i]);
                                buttonProduce3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce3.getId());
                                    }
                                });
                            } else if (PLACEHOLDER_NAME[k].equals("placeholder_4")){
                                buttonProduce4.setImageResource(ON_IMAGE_SRCS[i]);
                                buttonProduce4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce4.getId());
                                    }
                                });
                            } else if (PLACEHOLDER_NAME[k].equals("placeholder_5")){
                                buttonProduce5.setImageResource(ON_IMAGE_SRCS[i]);
                                buttonProduce5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce5.getId());
                                    }
                                });
                            } else if (PLACEHOLDER_NAME[k].equals("placeholder_6")){
                                buttonProduce6.setImageResource(ON_IMAGE_SRCS[i]);
                                buttonProduce6.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce6.getId());
                                    }
                                });
                            } else if (PLACEHOLDER_NAME[k].equals("placeholder_7")){
                                buttonProduce7.setImageResource(ON_IMAGE_SRCS[i]);
                                buttonProduce7.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce7.getId());
                                    }
                                });
                            } else if (PLACEHOLDER_NAME[k].equals("placeholder_8")){
                                buttonProduce8.setImageResource(ON_IMAGE_SRCS[i]);
                                buttonProduce8.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce8.getId());
                                    }
                                });
                            } else{
                                buttonProduce9.setImageResource(ON_IMAGE_SRCS[i]);
                                buttonProduce9.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce9.getId());
                                    }
                                });
                            }
                            // check its name
                        }
                    }
                }
            }
        }

        // Initialize
        timerProgressBar = findViewById(R.id.timer_progressbar);
        countdownTimerTextView = findViewById(R.id.countdown_timer);


        // Initialize mediaPlayers HashMap
        mediaPlayers = new HashMap<>();


        recordButton = findViewById(R.id.record); // Replace with your record button's ID
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    startAudioCapture();
                } else {
                    stopAudioCapture();
                }
            }
        });


    }





    private void startAudioCapture() {
        Log.d("AudioCapture", "startAudioCapture() called");
        Intent serviceIntent = new Intent(this, AudioCaptureService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
        if (mediaProjection == null) {
            Log.d("AudioCapture", "Requesting media projection");

            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), MEDIA_PROJECTION_REQUEST);
        } else {
            startCapturing();
        }
    }

    private void startCapturing() {
        Log.d("RecordingMode", "startCapturing() called");
        stopAllMediaPlayers();
        if (audioRecord != null) {
            Log.d("RecordingMode", "Existing audioRecord instance found. Releasing...");
            audioRecord.release();
            audioRecord = null;
        }
        Log.d("AudioCapture", "startCapturing() - Setting up AudioRecord");
        btnBackToGarden.setEnabled(false);
        recordButton.setText("Recording...");
        recordButton.setEnabled(false); // Disable the button while recording
        AudioPlaybackCaptureConfiguration config = new AudioPlaybackCaptureConfiguration.Builder(mediaProjection)
                .addMatchingUsage(AudioAttributes.USAGE_MEDIA)
                .build();

        int sampleRate = 44100;
        int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);


        audioRecord = new AudioRecord.Builder()
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(audioFormat)
                        .setSampleRate(sampleRate)
                        .setChannelMask(channelConfig)
                        .build())
                .setBufferSizeInBytes(minBufSize)
                .setAudioPlaybackCaptureConfig(config)
                .build();


        audioRecord.startRecording();
        isCapturing = true;
        isRecording = true;
        timerProgressBar.setVisibility(View.VISIBLE);
        timerProgressBar.setProgress(0);
        startCountDownTimer();

        capturingThread = new Thread(() -> {
            try (FileOutputStream outputStream = new FileOutputStream(audioFile)) {
                byte[] buffer = new byte[minBufSize];
                while (isCapturing) {
                    int read = audioRecord.read(buffer, 0, buffer.length);
                    if (read > 0) {
                        outputStream.write(buffer, 0, read);
                    }
                }
                Log.d("AudioCapture", "File saved to: " + audioFile.getAbsolutePath());
            } catch (IOException e) {
                Log.e("AudioCapture", "Error writing to file", e);
            }
        });
        capturingThread.start();
        Log.d("RecordingMode", "New AudioRecord instance started");
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Set the countdown timer text to "30" every time the activity resumes
        countdownTimerTextView.setText("30");
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
            MediaPlayer player = new MediaPlayer();

            // Set AudioAttributes to USAGE_MEDIA
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            player.setAudioAttributes(audioAttributes);

            // Set the data source
            AssetFileDescriptor afd = getResources().openRawResourceFd(soundResourceId);
            try {
                player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                player.prepare(); // Prepare the player (might take time, consider using prepareAsync)
            } catch (IOException e) {
                Log.e("AudioCapture", "Error setting data source", e);
            }

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

    private void stopAudioCapture() {
        Log.d("AudioCapture", "stopAudioCapture() called");
        stopService(new Intent(this, AudioCaptureService.class));
        isCapturing = false;
        isRecording = false;
        Log.d("RecordingMode", "stopAudioCapture() called");
        if (mediaProjection != null) {
            Log.d("RecordingMode", "Stopping MediaProjection");
            mediaProjection.stop();
            mediaProjection = null;  // Release the MediaProjection reference
        }
        if (audioRecord != null) {
            if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                Log.d("RecordingMode", "Stopping audioRecord");
                audioRecord.stop();
            }
            Log.d("RecordingMode", "Releasing audioRecord");
            audioRecord.release();
            audioRecord = null;
            // showSaveFileDialog();
            timerProgressBar.setVisibility(View.GONE);
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            // Convert the PCM file to WAV format after recording is stopped
            File wavFile = new File(getFilesDir(), "captured_audio1.wav");
            try {
                convertPcmToWav(audioFile, wavFile, 44100, 2, 16);
                Log.d("AudioCapture", "WAV file saved to: " + wavFile.getAbsolutePath());

                // Check if PCM file exists and delete it
                if (audioFile.exists()) {
                    boolean deleted = audioFile.delete();
                    if (deleted) {
                        Log.d("AudioCapture", "PCM file deleted successfully.");
                    } else {
                        Log.e("AudioCapture", "Failed to delete PCM file.");
                    }
                }

                File file = new File(getFilesDir(), "captured_audio1.wav");
                String filePath = file.getAbsolutePath();

                Intent intent = new Intent(this, share_recording.class);
                intent.putExtra("RECORDED_FILE_PATH", filePath);
                intent.putExtra("recordMap", recordMap); // Pass the recordMap to share_recording activity
                startActivityForResult(intent, SHARE_RECORDING_REQUEST);

            } catch (IOException e) {
                Log.e("AudioCapture", "Error converting PCM to WAV", e);
            }
        }
        capturingThread = null;
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
                stopAudioCapture();
                stopAllMediaPlayers();
                recordButton.setText("Record");
                btnBackToGarden.setEnabled(true);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("AudioCapture", "onActivityResult() - requestCode: " + requestCode + ", resultCode: " + resultCode);

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MEDIA_PROJECTION_REQUEST && resultCode == RESULT_OK) {
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
            startCapturing();
        } else if (requestCode == SHARE_RECORDING_REQUEST && resultCode == RESULT_OK && data != null) {
            // Retrieve the updated recordMap from the intent
            recordMap = (HashMap<Integer, Integer>) data.getSerializableExtra("recordMap");

            // Re-setup the UI based on the updated recordMap
            if (recordMap != null) {
                for (int j = 0; j<BUTTON_PRODUCE_IDS.length; j ++){
                    Log.d("RecordingMode", "this should be hashmap value " + BUTTON_PRODUCE_IDS[j]);
                }

                for (int d = 0; d<ICON_IDS.length; d ++){
                    Log.d("RecordingMode", "this should be hashmap key " + ICON_IDS[d]);
                }
                for (int i = 0; i < ICON_IDS.length; i++) {
                    // if produce found
                    if (recordMap.containsKey(ICON_IDS[i])) {
                        // check its name
                        Log.d("RecordingMode", PRODUCE_NAME[i] + " is in the garden");

                        // loop over all the placeholder IDs
                        for (int k = 0; k < BUTTON_PRODUCE_IDS.length; k++) {
                            // if the placeholder that contains the produce is found
                            if (recordMap.get(ICON_IDS[i]) == BUTTON_PRODUCE_IDS[k]) {
                                final int index = i;
                                Log.d("RecordingMode", PRODUCE_NAME[i] + "'s corresponding placeholder is " + PLACEHOLDER_NAME[k]);
                                if (PLACEHOLDER_NAME[k].equals("placeholder_1")){
                                    buttonProduce1.setImageResource(ON_IMAGE_SRCS[i]);
                                    buttonProduce1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce1.getId());
                                        }
                                    });
                                } else if (PLACEHOLDER_NAME[k].equals("placeholder_2")) {
                                    buttonProduce2.setImageResource(ON_IMAGE_SRCS[i]);
                                    buttonProduce2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce2.getId());
                                        }
                                    });
                                } else if (PLACEHOLDER_NAME[k].equals("placeholder_3")){
                                    buttonProduce3.setImageResource(ON_IMAGE_SRCS[i]);
                                    buttonProduce3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce3.getId());
                                        }
                                    });
                                } else if (PLACEHOLDER_NAME[k].equals("placeholder_4")){
                                    buttonProduce4.setImageResource(ON_IMAGE_SRCS[i]);
                                    buttonProduce4.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce4.getId());
                                        }
                                    });
                                } else if (PLACEHOLDER_NAME[k].equals("placeholder_5")){
                                    buttonProduce5.setImageResource(ON_IMAGE_SRCS[i]);
                                    buttonProduce5.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce5.getId());
                                        }
                                    });
                                } else if (PLACEHOLDER_NAME[k].equals("placeholder_6")){
                                    buttonProduce6.setImageResource(ON_IMAGE_SRCS[i]);
                                    buttonProduce6.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce6.getId());
                                        }
                                    });
                                } else if (PLACEHOLDER_NAME[k].equals("placeholder_7")){
                                    buttonProduce7.setImageResource(ON_IMAGE_SRCS[i]);
                                    buttonProduce7.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce7.getId());
                                        }
                                    });
                                } else if (PLACEHOLDER_NAME[k].equals("placeholder_8")){
                                    buttonProduce8.setImageResource(ON_IMAGE_SRCS[i]);
                                    buttonProduce8.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce8.getId());
                                        }
                                    });
                                } else{
                                    buttonProduce9.setImageResource(ON_IMAGE_SRCS[i]);
                                    buttonProduce9.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            toggleSound(SOUND_RESOURCE_IDS[index], buttonProduce9.getId());
                                        }
                                    });
                                }
                                // check its name
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("AudioCapture", "onRequestPermissionsResult() - requestCode: " + requestCode);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AUDIO_CAPTURE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startAudioCapture();
            } else {
                // Handle permission denial
            }
        }
    }

    private void convertPcmToWav(File pcmFile, File wavFile, int sampleRate, int channelCount, int bitDepth) throws IOException {
        long audioLength = pcmFile.length();
        long dataLength = audioLength + 36;
        long longSampleRate = sampleRate;
        int channels = channelCount;
        long byteRate = bitDepth * sampleRate * channels / 8;

        byte[] header = new byte[44];

        // RIFF/WAVE header
        header[0] = 'R';  header[1] = 'I';  header[2] = 'F';  header[3] = 'F';
        header[4] = (byte) (dataLength & 0xff);
        header[5] = (byte) ((dataLength >> 8) & 0xff);
        header[6] = (byte) ((dataLength >> 16) & 0xff);
        header[7] = (byte) ((dataLength >> 24) & 0xff);
        header[8] = 'W';  header[9] = 'A';  header[10] = 'V';  header[11] = 'E';
        header[12] = 'f';  header[13] = 'm';  header[14] = 't';  header[15] = ' ';
        header[16] = 16;  header[17] = 0;   header[18] = 0;   header[19] = 0;
        header[20] = 1;   header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (channels * bitDepth / 8);
        header[33] = 0;
        header[34] = (byte) bitDepth;
        header[35] = 0;
        header[36] = 'd';  header[37] = 'a';  header[38] = 't';  header[39] = 'a';
        header[40] = (byte) (audioLength & 0xff);
        header[41] = (byte) ((audioLength >> 8) & 0xff);
        header[42] = (byte) ((audioLength >> 16) & 0xff);
        header[43] = (byte) ((audioLength >> 24) & 0xff);

        FileOutputStream out = new FileOutputStream(wavFile);
        out.write(header, 0, 44);

        FileInputStream in = new FileInputStream(pcmFile);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }

        in.close();
        out.close();
    }






}