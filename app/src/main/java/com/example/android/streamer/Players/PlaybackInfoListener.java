package com.example.android.streamer.Players;

import android.support.v4.media.session.PlaybackStateCompat;

public interface PlaybackInfoListener {
    void onPlaybackStateChanged(PlaybackStateCompat state);
}
