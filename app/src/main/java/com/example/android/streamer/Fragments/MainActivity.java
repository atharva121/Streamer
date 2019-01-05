package com.example.android.streamer.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.streamer.Client.MediaBrowserHelper;
import com.example.android.streamer.Models.Artist;
import com.example.android.streamer.R;
import com.example.android.streamer.Services.MediaService;
import com.example.android.streamer.Util.MainActivityFragmentManager;
import com.google.firestore.admin.v1beta1.Progress;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IMainActivity
{

    private static final String TAG = "MainActivity";
    private ProgressBar mProgressBar;
    private MediaBrowserHelper mMediaBrowserHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progress_bar);
        mMediaBrowserHelper = new MediaBrowserHelper(this, MediaService.class);
        if (savedInstanceState == null) {
            loadFragment(HomeFragment.newInstance(), true);
        }
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

    private void loadFragment(Fragment fragment, boolean lateralMovement){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (lateralMovement){
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        }
        String tag = "";
        if (fragment instanceof HomeFragment){
            tag = getString(R.string.fragment_home);
        }
        else if (fragment instanceof CategoryFragment){
            tag = getString(R.string.fragment_category);
            transaction.addToBackStack(tag);
        }
        else if (fragment instanceof PlaylistFragment){
            tag = getString(R.string.fragment_playlist);
            transaction.addToBackStack(tag);
        }
        transaction.add(R.id.main_container, fragment, tag);
        transaction.commit();
        MainActivityFragmentManager.getInstance().addFragment(fragment);
        showFragment(fragment, false);
    }

    private void showFragment(Fragment fragment, boolean backwardsMovement){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (backwardsMovement){
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        }
        transaction.show(fragment);
        transaction.commit();
        for (Fragment f : MainActivityFragmentManager.getInstance().getFragments()){
            if (f != null){
                if (!f.getTag().equals(fragment.getTag())){
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
        if (fragments.size() > 1){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(fragments.get(fragments.size() - 1));
            transaction.commit();
            MainActivityFragmentManager.getInstance().removeFragment(fragments.size()  - 1);
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
    public void setActinBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}