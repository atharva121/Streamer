package com.example.android.streamer.Fragments;

import android.content.Context;
import android.media.MediaMetadata;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.streamer.Adapters.CategoryRecyclerAdapter;
import com.example.android.streamer.Adapters.PlaylistRecyclerAdapter;
import com.example.android.streamer.Models.Artist;
import com.example.android.streamer.R;

import java.util.ArrayList;

public class PlaylistFragment extends Fragment implements
        PlaylistRecyclerAdapter.IMediaSelector
{

    private static final String TAG = "PlaylistFragment";
    private RecyclerView mRecyclerView;
    private PlaylistRecyclerAdapter mAdapter;
    private ArrayList<MediaMetadataCompat> mMediaList = new ArrayList<>();
    private IMainActivity mIMainActivity;
    private String mSelectedCategory;
    private Artist mSelectedArtist;
    private MediaMetadata mSelectedMedia;

    public static PlaylistFragment newInstance(String category, Artist artist){
        PlaylistFragment playlistFragment = new PlaylistFragment();
        Bundle args = new Bundle();
        args.putString("category", category);
        args.putParcelable("artist", artist);
        playlistFragment.setArguments(args);
        return playlistFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mSelectedCategory = getArguments().getString("category");
            mSelectedArtist = getArguments().getParcelable("artist");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initRecyclerView(view);
    }

    private void updateDataSet(){
        mIMainActivity.hideProgressBar();
        mAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView(View view) {
        if (mRecyclerView == null){
            mRecyclerView = view.findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new PlaylistRecyclerAdapter(getActivity(), mMediaList, this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIMainActivity = (IMainActivity) getActivity();
    }

    @Override
    public void onMediaSelected(int position) {

    }
}
