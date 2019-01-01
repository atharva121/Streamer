package com.example.android.streamer.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.streamer.Models.Artist;
import com.example.android.streamer.R;
import com.google.firestore.admin.v1beta1.Progress;

public class MainActivity extends AppCompatActivity implements IMainActivity
{

    private static final String TAG = "MainActivity";
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progress_bar);
        loadFragment(HomeFragment.newInstance(), true);
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
        }
        else if (fragment instanceof PlaylistFragment){
            tag = getString(R.string.fragment_playlist);
        }
        transaction.add(R.id.main_container, fragment, tag);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }
}