package com.example.android.streamer.Fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

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
        testHomeFragment();

    }

    private void testHomeFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, HomeFragment.newInstance()).commit();
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