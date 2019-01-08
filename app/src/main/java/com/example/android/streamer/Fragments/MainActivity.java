package com.example.android.streamer.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.streamer.Client.MediaBrowserHelper;
import com.example.android.streamer.Client.MediaBrowserHelperCallback;
import com.example.android.streamer.Models.Artist;
import com.example.android.streamer.MyApplication;
import com.example.android.streamer.R;
import com.example.android.streamer.Services.MediaService;
import com.example.android.streamer.Util.MainActivityFragmentManager;
import com.example.android.streamer.Util.MyPreferenceManager;
import com.google.firestore.admin.v1beta1.Progress;

import java.util.ArrayList;

import static com.example.android.streamer.Util.Constants.MEDIA_QUEUE_POSITION;
import static com.example.android.streamer.Util.Constants.QUEUE_NEW_PLAYLIST;

public class MainActivity extends AppCompatActivity implements
        IMainActivity,
        MediaBrowserHelperCallback
{

    private static final String TAG = "MainActivity";
    private ProgressBar mProgressBar;
    private MediaBrowserHelper mMediaBrowserHelper;
    private MyApplication mMyApplication;
    private MyPreferenceManager mMyPrefManager;
    private boolean mIsPlaying;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progress_bar);
        mMyPrefManager = new MyPreferenceManager(this);
        mMediaBrowserHelper = new MediaBrowserHelper(this, MediaService.class);
        mMediaBrowserHelper.setMediaBrowserHelperCallback(this);
        mMyApplication = MyApplication.getInstance();
        if (savedInstanceState == null) {
            loadFragment(HomeFragment.newInstance(), true);
        }
    }

    @Override
    public void playPause() {
        if (mIsPlaying) {
            mMediaBrowserHelper.getTransportControls().skipToNext();
        } else {
            mMediaBrowserHelper.getTransportControls().play();
            mIsPlaying = true;
        }
    }

    @Override
    public MyApplication getMyApplication() {
        return mMyApplication;
    }

    @Override
    public void onMediaSelected(String playlistId, MediaMetadataCompat mediaItem, int queuePosition) {
        if (mediaItem != null){
            Log.d(TAG, "onMediaSelected: Called: " + mediaItem.getDescription().getMediaId());
            String currentPlaylistId = getMyPrefManager().getPlaylistId();
            Bundle bundle = new Bundle();
            bundle.putInt(MEDIA_QUEUE_POSITION, queuePosition);
            if (playlistId.equals(currentPlaylistId)){
                mMediaBrowserHelper.getTransportControls().playFromMediaId(mediaItem.getDescription().getMediaId(), bundle);
            }
            else {
                bundle.putBoolean(QUEUE_NEW_PLAYLIST, true);
                mMediaBrowserHelper.subscribeToNewPlaylist(playlistId);
                mMediaBrowserHelper.getTransportControls().playFromMediaId(mediaItem.getDescription().getMediaId(), bundle);
            }

        }
        else {
            Toast.makeText(this, "select something to play", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public MyPreferenceManager getMyPrefManager() {
        return mMyPrefManager;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMediaBrowserHelper.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaBrowserHelper.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("active_fragments", MainActivityFragmentManager.getInstance().getFragments().size());
    }

    private void loadFragment(Fragment fragment, boolean lateralMovement) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (lateralMovement) {
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        }
        String tag = "";
        if (fragment instanceof HomeFragment) {
            tag = getString(R.string.fragment_home);
        } else if (fragment instanceof CategoryFragment) {
            tag = getString(R.string.fragment_category);
            transaction.addToBackStack(tag);
        } else if (fragment instanceof PlaylistFragment) {
            tag = getString(R.string.fragment_playlist);
            transaction.addToBackStack(tag);
        }
        transaction.add(R.id.main_container, fragment, tag);
        transaction.commit();
        MainActivityFragmentManager.getInstance().addFragment(fragment);
        showFragment(fragment, false);
    }

    private void showFragment(Fragment fragment, boolean backwardsMovement) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (backwardsMovement) {
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        }
        transaction.show(fragment);
        transaction.commit();
        for (Fragment f : MainActivityFragmentManager.getInstance().getFragments()) {
            if (f != null) {
                if (!f.getTag().equals(fragment.getTag())) {
                    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                    t.hide(f);
                    t.commit();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        ArrayList<Fragment> fragments = new ArrayList<>(MainActivityFragmentManager.getInstance().getFragments());
        if (fragments.size() > 1) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(fragments.get(fragments.size() - 1));
            transaction.commit();
            MainActivityFragmentManager.getInstance().removeFragment(fragments.size() - 1);
            showFragment(fragments.get(fragments.size() - 2), true);
        }
        super.onBackPressed();
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCategorySelected(String category) {
        loadFragment(CategoryFragment.newInstance(category), true);
    }

    @Override
    public void onArtistSelected(String category, Artist artist) {
        loadFragment(PlaylistFragment.newInstance(category, artist), true);
    }

    @Override
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metaData) {

    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        mIsPlaying = state != null &&
                state.getState() == PlaybackStateCompat.STATE_PLAYING;
    }
}