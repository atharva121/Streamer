package com.example.android.streamer;

import android.app.Application;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class  MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static MyApplication mInstance;
    private List<MediaBrowserCompat.MediaItem> mMediaItems = new ArrayList<>();
    private TreeMap<String, MediaMetadataCompat> mTreeMap = new TreeMap<>();

    public static MyApplication getInstance(){
        if (mInstance == null){
            mInstance = new MyApplication();
        }
        return mInstance;
    }

    public void setMediaItems(List<MediaMetadataCompat> mediaItems){
        mMediaItems.clear();
        for (MediaMetadataCompat item : mediaItems){
            mMediaItems.add(
                    new MediaBrowserCompat.MediaItem(
                            item.getDescription(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                    )
            );
            mTreeMap.put(item.getDescription().getMediaId(), item);
        }
    }

    public List<MediaBrowserCompat.MediaItem> getMediaItems(){
        return mMediaItems;
    }

    public TreeMap<String , MediaMetadataCompat> getTreeMap(){
        return mTreeMap;
    }

    public MediaMetadataCompat getMediaItem(String mediaId){
        return mTreeMap.get(mediaId);
    }
}
