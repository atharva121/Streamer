package com.example.android.streamer.Util;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

public class MainActivityFragmentManager {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private static MainActivityFragmentManager instance;
    public static MainActivityFragmentManager getInstance(){
        if (instance == null){
            instance = new MainActivityFragmentManager();
        }
        return instance;
    }

    public void addFragment(Fragment fragment){
        mFragments.add(fragment);
    }

    private void removeFragment(Fragment fragment){
        mFragments.remove(fragment);
    }

    private void removeFragment(int postion){
        mFragments.remove(postion);
    }

    private ArrayList<Fragment> getFragments(){
        return mFragments;
    }

    public void removeAllFragments(){
        mFragments.clear();
    }
}
