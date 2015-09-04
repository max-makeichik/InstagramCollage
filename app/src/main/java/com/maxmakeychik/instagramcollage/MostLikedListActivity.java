package com.maxmakeychik.instagramcollage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.maxmakeychik.instagramcollage.fragments.CollageFragment;
import com.maxmakeychik.instagramcollage.fragments.MediaFragment;
import com.maxmakeychik.instagramcollage.model.Media;

import java.util.ArrayList;

public class MostLikedListActivity extends AppCompatActivity {

    private static final String TAG_MEDIA_FRAGMENT = "MEDIA_FRAGMENT";
    private static final String MEDIA_LIST_KEY = "MEDIA_LIST";
    private static final String COLLAGES_DIRECTORY_NAME = "InstagramCollage";
    private static final String TAG = "MostLikedListActivity";
    private ArrayList<Media> mediaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media);

        if(savedInstanceState != null)
            mediaList = savedInstanceState.getParcelableArrayList(MEDIA_LIST_KEY);

        Button makeCollageButton = (Button) findViewById(R.id.makeCollageButton);
        makeCollageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCollage();
            }
        });

        ImageButton toolbarBackButton = (ImageButton) findViewById(R.id.toolbar_back_button);
        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        handleIntent(getIntent());
    }

    public void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if(intent.getParcelableArrayListExtra(MEDIA_LIST_KEY) != null)
            mediaList = intent.getParcelableArrayListExtra(MEDIA_LIST_KEY);
        Log.d(TAG, "mediaList " + mediaList);

        Fragment mediaFragment = new MediaFragment();
        Bundle mediaBundle = new Bundle();
        mediaBundle.putParcelableArrayList(MEDIA_LIST_KEY, mediaList);
        mediaFragment.setArguments(mediaBundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mediaFragment, null)
                .commit();
    }

    private void makeCollage() {
        Fragment collageFragment = new CollageFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, collageFragment, null)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getSupportFragmentManager().getBackStackEntryCount() != 0)
            getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MEDIA_LIST_KEY, mediaList);
    }
}