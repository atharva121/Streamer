package com.example.android.streamer.Fragments;

import com.example.android.streamer.Models.Artist;
import com.example.android.streamer.MyApplication;

public interface IMainActivity {
    void hideProgressBar();
    void showProgressBar();
    void onCategorySelected(String category);
    void onArtistSelected(String category, Artist artist);
    void setActinBarTitle(String title);
    void playPause();
    MyApplication getMyApplication();
}
