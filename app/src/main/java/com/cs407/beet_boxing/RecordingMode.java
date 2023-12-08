package com.cs407.beet_boxing;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.List;


public class RecordingMode extends AppCompatActivity {
    private TextView countdownTimerTextView;

    private Button recordButton;
    private ProgressBar timerProgressBar;
    // Declare variable
    private boolean isRecording = false;
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

    private static final int AUDIO_CAPTURE_PERMISSION_REQUEST = 1;
    private static final int MEDIA_PROJECTION_REQUEST = 2;

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private AudioRecord audioRecord;
    private boolean isCapturing = false;
    private Thread capturingThread;

    private File audioFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_mode);
        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        audioFile = new File(getFilesDir(), "captured_audio1.pcm");


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
        Log.d("AudioCapture", "startCapturing() - Setting up AudioRecord");
        int myUid = getApplicationContext().getApplicationInfo().uid;
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
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
            timerProgressBar.setVisibility(View.GONE);
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            // Convert the PCM file to WAV format after recording is stopped
            File wavFile = new File(getFilesDir(), "captured_audio1.wav");
            try {
                convertPcmToWav(audioFile, wavFile, 44100, 2, 16);
                Log.d("AudioCapture", "WAV file saved to: " + wavFile.getAbsolutePath());
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
