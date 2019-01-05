package com.example.android.streamer.Fragments;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaMetadataCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.streamer.MediaSeekBar;
import com.example.android.streamer.R;

public class MediaControllerFragment extends Fragment implements
        View.OnClickListener
{


    private static final String TAG = "MediaControllerFragment";
    private TextView mSongTitle;
    private ImageView mPlayPause;
    private MediaSeekBar mSeekBarAudio;
    private IMainActivity mIMainActivity;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_media_controller, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mSongTitle = view.findViewById(R.id.media_song_title);
        mPlayPause = view.findViewById(R.id.play_pause);
        mSeekBarAudio = view.findViewById(R.id.seekbar_audio);
        mPlayPause.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.play_pause){
            mIMainActivity.playPause();
        }
    }

    public  void setIsPlaying(boolean isPlaying){
        if (isPlaying){
            Glide.with(getActivity())
                    .load(R.drawable.ic_pause_circle_outline_white_24dp)
                    .into(mPlayPause);
        }
        else{
            Glide.with(getActivity())
                    .load(R.drawable.ic_play_circle_outline_white_24dp)
                    .into(mPlayPause);
        }
    }

    public void setMediaTitle(MediaMetadataCompat mediaItem){
        mSongTitle.setText(mediaItem.getDescription().getTitle());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIMainActivity = (IMainActivity) getActivity();
    }
}

