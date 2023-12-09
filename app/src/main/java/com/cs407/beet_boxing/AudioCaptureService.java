package com.cs407.beet_boxing;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class AudioCaptureService extends Service {
    private static final int NOTIFICATION_ID = 1;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = createNotification();
        startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION);
        // Start capturing...
        return START_STICKY;
    }

    private Notification createNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // For Android Oreo and above, Notification Channel is required.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_01", "Audio Capture Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_01")
                .setContentTitle("Audio Capture Service")
                .setContentText("Capturing audio playback...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder.build();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop capturing...
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

