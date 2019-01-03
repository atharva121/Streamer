package com.example.android.streamer.Services;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;

import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;

import java.util.List;

public class MediaService extends MediaBrowserServiceCompat {
    private static final String TAG = "MediaService";
    private MediaSessionCompat mSession;

    public MediaService() {
        mSession = new MediaSessionCompat(this, TAG);
        mSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS
                );
        mSession.setCallback(new MediaSessionCallback());
        setSessionToken(mSession.getSessionToken());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSession.release();
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String s, int i, @Nullable Bundle bundle) {
        if (s.equals(getApplicationContext().getPackageName())){

        }
        return new BrowserRoot("empty_media", null);
    }

    @Override
    public void onLoadChildren(@NonNull String s, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        if (TextUtils.equals("empty_media", s)){
            result.sendResult(null);
            return;
        }
        result.sendResult(null);
    }

    public class MediaSessionCallback extends MediaSessionCompat.Callback{
        @Override
        public void onPrepare() {
            super.onPrepare();
        }

        @Override
        public void onPlay() {
            super.onPlay();
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            super.onPlayFromMediaId(mediaId, extras);
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
        }

        @Override
        public void onAddQueueItem(MediaDescriptionCompat description) {
            super.onAddQueueItem(description);
        }

        @Override
        public void onRemoveQueueItem(MediaDescriptionCompat description) {
            super.onRemoveQueueItem(description);
        }
    }
}
