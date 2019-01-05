package com.example.android.streamer.Fragments;

import android.support.v4.media.MediaMetadataCompat;

import com.example.android.streamer.Models.Artist;
import com.example.android.streamer.MyApplication;

public interface IMainActivity {
    void hideProgressBar();
    void showProgressBar();
    void onCategorySelected(String category);
    void onArtistSelected(String category, Artist artist);
    void setActionBarTitle(String title);
    void playPause();
    MyApplication getMyApplication();
    void onMediaSelected(MediaMetadataCompat mediaItem);
}
