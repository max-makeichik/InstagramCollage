package com.maxmakeychik.instagramcollage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.maxmakeychik.instagramcollage.fragments.CollageFragment;
import com.maxmakeychik.instagramcollage.fragments.MediaFragment;
import com.maxmakeychik.instagramcollage.model.Media;

import java.util.ArrayList;

public class MediaActivity extends AppCompatActivity {

    private static final String TAG_MEDIA_FRAGMENT = "MEDIA_FRAGMENT";
    private static final String MEDIA_LIST_KEY = "MEDIA_LIST";
    private static final String CHECKED_MEDIA_LIST_KEY = "CHECKED_MEDIA_LIST";
    private ArrayList<Media> mediaList, checkedMediaList = new ArrayList<>();
    private TextView toolbarTitle;
    private static final String TAG = "MediaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media);

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        ImageButton toolbarBackButton = (ImageButton) findViewById(R.id.toolbar_back_button);
        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(savedInstanceState != null) {
            mediaList = savedInstanceState.getParcelableArrayList(MEDIA_LIST_KEY);
            checkedMediaList = savedInstanceState.getParcelableArrayList(CHECKED_MEDIA_LIST_KEY);
        }
        setToolbarTitle();

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
        showMedia();
    }

    private void showMedia() {
        Fragment mediaFragment = new MediaFragment();
        Bundle mediaBundle = new Bundle();
        mediaBundle.putParcelableArrayList(MEDIA_LIST_KEY, mediaList);
        mediaFragment.setArguments(mediaBundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mediaFragment, null)
                .commit();
    }

    private void setToolbarTitle(){
        toolbarTitle.setText(String.format(getString(R.string.toolbar_title), checkedMediaList.size()));
    }

    public void changeTitle(Media media, boolean checked) {
        if(checked) {
            if (!checkedMediaList.contains(media))
                checkedMediaList.add(media);
        }
        else if(checkedMediaList.contains(media))
                checkedMediaList.remove(media);
        setToolbarTitle();
    }

    public void makeCollage() {
        if(checkedMediaList.size() != 0) {
            Fragment collageFragment = new CollageFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(MEDIA_LIST_KEY, checkedMediaList);
            collageFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, collageFragment, null)
                    .addToBackStack(null)
                    .commit();
        }
        else
            Toast.makeText(this, getString(R.string.error_collage_number_of_photos), Toast.LENGTH_SHORT).show();
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
        outState.putParcelableArrayList(CHECKED_MEDIA_LIST_KEY, checkedMediaList);
    }
}