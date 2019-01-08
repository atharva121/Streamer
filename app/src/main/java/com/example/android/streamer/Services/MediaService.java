package com.example.android.streamer.Services;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.streamer.MyApplication;
import com.example.android.streamer.Players.MediaPlayerAdapter;
import com.example.android.streamer.Players.PlaybackInfoListener;
import com.example.android.streamer.Players.PlayerAdapter;
import com.example.android.streamer.Util.MediaLibrary;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.streamer.Util.Constants.MEDIA_QUEUE_POSITION;
import static com.example.android.streamer.Util.Constants.QUEUE_NEW_PLAYLIST;

public class MediaService extends MediaBrowserServiceCompat {
    private static final String TAG = "MediaService";
    private MediaSessionCompat mSession;
    private PlayerAdapter mPlayback;
    private MyApplication mMyApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mMyApplication = MyApplication.getInstance();
        mSession = new MediaSessionCompat(this, TAG);
        mSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS
        );
        mSession.setCallback(new MediaSessionCallback());
        setSessionToken(mSession.getSessionToken());
        mPlayback = new MediaPlayerAdapter(this, new MediaPlayerListener());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        mPlayback.stop();
        stopSelf();
    }

    @Override
    public void onDestroy() {
        mSession.release();
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String s, int i, @Nullable Bundle bundle) {
        if (s.equals(getApplicationContext().getPackageName())){
            return new BrowserRoot("some_real_playlist", null);
        }
        return new BrowserRoot("empty_media", null);
    }

    @Override
    public void onLoadChildren(@NonNull String s, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        if (TextUtils.equals("empty_media", s)){
            result.sendResult(null);
            return;
        }
        result.sendResult(mMyApplication.getMediaItems());
    }

    public class MediaSessionCallback extends MediaSessionCompat.Callback{

        private final List<MediaSessionCompat.QueueItem> mPlaylist = new ArrayList<>();
        private int mQueueIndex = -1;
        private MediaMetadataCompat mPreparedMedia;

        private void resetPlaylist(){
            mPlaylist.clear();
            mQueueIndex = -1;
        }

        @Override
        public void onPrepare() {
            if (mQueueIndex < 0 && mPlaylist.isEmpty()){
                return;
            }
            String mediaId = mPlaylist.get(mQueueIndex).getDescription().getMediaId();
            mPreparedMedia = mMyApplication.getMediaItem(mediaId);
            mSession.setMetadata(mPreparedMedia);
            if (!mSession.isActive()){
                mSession.setActive(true);
            }
        }

        @Override
        public void onPlay() {
            if (!isReadyToPlay()){
                return;
            }
            if (mPreparedMedia == null){
                onPrepare();
            }
            mPlayback.playFromMedia(mPreparedMedia);
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            Log.d(TAG, "onPlayFromMediaId: Called.");
            if (extras.getBoolean(QUEUE_NEW_PLAYLIST, false)){
                resetPlaylist();
            }
            mPreparedMedia = mMyApplication.getMediaItem(mediaId);
            mSession.setMetadata(mPreparedMedia);
            if (!mSession.isActive()){
                mSession.setActive(true);
            }
            mPlayback.playFromMedia(mPreparedMedia);
            int newQueuePosition = extras.getInt(MEDIA_QUEUE_POSITION, -1);
            if (newQueuePosition == -1){
                mQueueIndex++;
            }
            else {
                mQueueIndex = newQueuePosition;
            }
        }

        @Override
        public void onPause() {
            mPlayback.pause();
        }

        @Override
        public void onSkipToNext() {
            Log.d(TAG, "onSkipToNext: SKIP TO NEXT");
            mQueueIndex = (++mQueueIndex % mPlaylist.size());
            Log.d(TAG, "onSkipToNext: queue index: " + mQueueIndex);
            mPreparedMedia = null;
            onPlay();
        }

        @Override
        public void onSkipToPrevious() {
            Log.d(TAG, "onSkipToPrevious: SKIPPING TO PREVIOUS");
            mQueueIndex = mQueueIndex > 0 ? mQueueIndex - 1 : mPlaylist.size() - 1;
            mPreparedMedia = null;
            onPlay();
        }

        @Override
        public void onStop() {
            mPlayback.stop();
            mSession.setActive(false);
        }

        @Override
        public void onSeekTo(long pos) {
            mPlayback.seekTo(pos);
        }

        @Override
        public void onAddQueueItem(MediaDescriptionCompat description) {
            Log.d(TAG, "onAddQueueItem: called: position in list: " + mPlaylist.size());
            mPlaylist.add(new MediaSessionCompat.QueueItem(description, description.hashCode()));
            mQueueIndex = (mQueueIndex == -1) ? 0 : mQueueIndex;
            mSession.setQueue(mPlaylist);
        }

        @Override
        public void onRemoveQueueItem(MediaDescriptionCompat description) {
            Log.d(TAG, "onRemoveQueueItem: called: position in list: " + mPlaylist.size());
            mPlaylist.remove(new MediaSessionCompat.QueueItem(description, description.hashCode()));
            mQueueIndex = (mPlaylist.isEmpty()) ? -1 : mQueueIndex;
            mSession.setQueue(mPlaylist);
        }

        private boolean isReadyToPlay(){
            return(!mPlaylist.isEmpty());
        }
    }

    private class MediaPlayerListener implements PlaybackInfoListener{
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            mSession.setPlaybackState(state);
        }
    }
}
