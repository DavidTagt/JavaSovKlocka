package com.gesall.mat_och_sov_klocka.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.gesall.mat_och_sov_klocka.R;
import com.gesall.mat_och_sov_klocka.activities.RingActivity;

import static com.gesall.mat_och_sov_klocka.application.App.CHANNEL_ID;
import static com.gesall.mat_och_sov_klocka.broadcastreceiver.AlarmBroadcastReceiver.TITLE;

public class AlarmService extends Service {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setLooping(true);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, RingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        String alarmTitle = String.format("%s alarm", intent.getStringExtra(TITLE));


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("CLICK ME!")
                .setContentText("RING RING! " + alarmTitle)
                .setSmallIcon(R.drawable.ic_baseline_alarm_black_24)
                .setContentIntent(pendingIntent)
                .build();

        mediaPlayer.start();

        long[] pattern = {0, 100, 1000};
        vibrator.vibrate(pattern, 0);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelName = "Alarm Notifications (required to function, please don't disable)";
            NotificationChannel chan = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
            Notification notificationO = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.ic_baseline_alarm_black_24)
                    .setContentTitle("CLICK ME!")
                    .setContentText("RING RING! " + alarmTitle)
                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(2, notificationO);
        } else {
            startForeground(1, notification);
        }


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
