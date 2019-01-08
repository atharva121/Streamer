package com.example.android.streamer.Client;

import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.util.List;

public class MediaBrowserHelper {
    private static final String TAG = "MediaBrowserHelper";
    private final Context mContext;
    private final Class<? extends MediaBrowserServiceCompat> mMediaBrowserServiceClass;
    private MediaBrowserCompat mMediaBrowser;
    private MediaControllerCompat mMediaController;
    private MediaBrowserConnectionCallback mMediaBrowserConnectionCallback;
    private MediaBrowserSubscriptionCallback mMediaBrowserSubscriptionCallback;
    private MediaControllerCallback mMediaControllerCallback;
    private MediaBrowserHelperCallback mMediaBrowserCallback;

    public MediaBrowserHelper(Context context, Class<? extends MediaBrowserServiceCompat> mediaBrowserServiceClass) {
        this.mContext = context;
        this.mMediaBrowserServiceClass = mediaBrowserServiceClass;
        mMediaBrowserConnectionCallback = new MediaBrowserConnectionCallback();
        mMediaBrowserSubscriptionCallback = new MediaBrowserSubscriptionCallback();
    }

    public void setMediaBrowserHelperCallback(MediaBrowserHelperCallback browserHelperCallback){
        mMediaBrowserCallback = browserHelperCallback;
    }

    public void onStop(){
        if (mMediaController != null){
            mMediaController.unregisterCallback(mMediaControllerCallback);
            mMediaController = null;
        }
        if (mMediaBrowser != null && mMediaBrowser.isConnected()){
            mMediaBrowser.disconnect();
            mMediaBrowser = null;
        }
        Log.d(TAG, "onStop: disconnecting from the service");
    }

    public void subscribeToNewPlaylist(String playlistId){
        mMediaBrowser.subscribe(playlistId, mMediaBrowserSubscriptionCallback);
    }

    public void onStart(){
        if (mMediaBrowser == null){
            mMediaBrowser = new MediaBrowserCompat(
                    mContext,
                    new ComponentName(mContext, mMediaBrowserServiceClass),
                    mMediaBrowserConnectionCallback,
                    null);
            mMediaBrowser.connect();
            Log.d(TAG, "onStart: connecting to the service");
        }
    }

    private class MediaControllerCallback extends MediaControllerCompat.Callback{
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            Log.d(TAG, "onPlaybackStateChanged: called.");
            if (mMediaBrowserCallback != null){
                mMediaBrowserCallback.onPlaybackStateChanged(state);
            }
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            Log.d(TAG, "onMetadataChanged: called.");
            if (mMediaBrowserCallback != null){
                mMediaBrowserCallback.onMetadataChanged(metadata);
            }
        }
    }

    private class MediaBrowserConnectionCallback extends MediaBrowserCompat.ConnectionCallback{
        @Override
        public void onConnected() {
            Log.d(TAG, "onConnected: called.");
            try {
                mMediaController = new MediaControllerCompat(mContext, mMediaBrowser.getSessionToken());
                mMediaController.registerCallback(mMediaControllerCallback);
            }catch (RemoteException e){
                Log.d(TAG, "onConnected: ocnnection problem: " + e.toString());
            }
            mMediaBrowser.subscribe(mMediaBrowser.getRoot(), mMediaBrowserSubscriptionCallback);
        }
    }
    public class MediaBrowserSubscriptionCallback extends MediaBrowserCompat.SubscriptionCallback{
        @Override
        public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
            Log.d(TAG, "onChildrenLoaded: called: " + parentId + ", " + children.toString());
            for (final MediaBrowserCompat.MediaItem mediaItem : children){
                mMediaController.addQueueItem(mediaItem.getDescription());
            }
        }
    }

    public MediaControllerCompat.TransportControls getTransportControls(){
        if (mMediaController == null){
            throw new IllegalStateException("Media controller is null!");
        }
        return mMediaController.getTransportControls();
    }
}
