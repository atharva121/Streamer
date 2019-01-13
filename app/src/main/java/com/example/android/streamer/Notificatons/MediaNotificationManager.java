package com.example.android.streamer.Notificatons;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.android.streamer.Services.MediaService;

public class MediaNotificationManager {
    private static final String TAG = "MediaNotificationManage";
    private static final String CHANNEL_ID = "com.example.android.streamer.musicplayer.channel";
    private final MediaService mMediaService;
    private NotificationManager mNotificationManager;

    public MediaNotificationManager(MediaService mediaService) {
        this.mMediaService = mediaService;
        mNotificationManager = (NotificationManager)mMediaService.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public NotificationManager getNotificationManager(){
        return mNotificationManager;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel(){
        if (mNotificationManager.getNotificationChannel(CHANNEL_ID) == null){
            CharSequence name = "MediaSession";
            String description = "MediaSession for the MediaPlayer";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 100});
            mNotificationManager.createNotificationChannel(mChannel);
            Log.d(TAG, "createChannel: new notification channel created!");
        }
        else {
            Log.d(TAG, "createChannel: notification channel already exists!");
        }
    }

    private boolean isAndroidOOrHigher(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
}
