package com.example.android.streamer.Client;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;

public interface MediaBrowserHelperCallback {
    void onMetadataChanged(final MediaMetadataCompat metaData);
    void onPlaybackStateChanged(PlaybackStateCompat state);
}
