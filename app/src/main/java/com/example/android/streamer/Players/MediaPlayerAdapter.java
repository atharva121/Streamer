package com.example.android.streamer.Players;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaMetadataCompat;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MediaPlayerAdapter extends PlayerAdapter {

    private Context mContext;
    private MediaMetadataCompat mCurrentMedia;
    private boolean mCurrentMediaPlayedToCompletion;
    private SimpleExoPlayer mExoPlayer;
    private TrackSelector mTrackSelector;
    private DefaultRenderersFactory mRenderer;
    private DataSource.Factory mDataSourceFactory;

    public MediaPlayerAdapter(@NonNull Context context) {
        super(context);
    }

    private void initializeExoPlayer(){
        if (mExoPlayer == null) {
            mTrackSelector = new DefaultTrackSelector();
            mRenderer = new DefaultRenderersFactory(mContext);
            mDataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "Streamer"));
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mRenderer, mTrackSelector, new DefaultLoadControl());
        }
    }

    private void release(){
        if (mExoPlayer != null){
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    protected void onPlay() {

    }

    @Override
    protected void onPause() {

    }

    @Override
    public void playFromMedia(MediaMetadataCompat metadata) {
        startTrackingPlayback();
        playFile(metadata);
    }

    @Override
    public MediaMetadataCompat getCurrentMedia() {
        return null;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    protected void onStop() {

    }

    @Override
    public void seekTo(long position) {

    }

    @Override
    public void setVolume(float volume) {

    }


    private void playFile(MediaMetadataCompat metadata) {
        String mediaId = metadata.getDescription().getMediaId();
        boolean mediaChanged = (mCurrentMedia == null) || !mediaId.equals(mCurrentMedia.getDescription().getMediaId());
        if (mCurrentMediaPlayedToCompletion){
            mediaChanged = true;
            mCurrentMediaPlayedToCompletion = false;
        }
        if (!mediaChanged){
            if (!isPlaying()){
                play();
            }
            return;
        }
        else {
            release();
        }
        mCurrentMedia = metadata;
        initializeExoPlayer();
        try {
            MediaSource audioSource =
                    new ExtractorMediaSource.Factory(mDataSourceFactory)
                            .createMediaSource(Uri.parse(mCurrentMedia.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)));
        }catch (Exception e){
            throw new RuntimeException("Failed to play media url: "
            + mCurrentMedia.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI), e);
        }
        play();
    }

    private void startTrackingPlayback() {

    }
}
