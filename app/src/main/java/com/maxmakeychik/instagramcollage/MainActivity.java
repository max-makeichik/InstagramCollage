package com.maxmakeychik.instagramcollage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.widget.EditText;
import android.widget.Toast;

import com.maxmakeychik.instagramcollage.data_ops.GetUserIdByName;
import com.maxmakeychik.instagramcollage.data_ops.GetUserMedia;
import com.maxmakeychik.instagramcollage.model.Media;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private static final String MEDIA_LIST_KEY = "MEDIA_LIST";
    private boolean isLoading = false;
    EditText nickEditText;
    private static final String COLLAGES_DIRECTORY_NAME = "InstagramCollage";
    private static final int NUMBER_OF_PHOTOS_FOR_COLLAGE = 3;
    Context ctx;
    protected static final String PAGINATION_KEY = "pagination";
    private ArrayList<Media> mediaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ctx = this;
        nickEditText = (EditText) findViewById(R.id.nickEditText);
        nickEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (!isLoading) {
                    isLoading = true;
                    getUserIdByName(nickEditText.getText().toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    private void getUserIdByName(String userName) {
        new GetUserIdByName() {
            @Override
            public void onPostExecute(Integer userId) {
                if (userId != null) {
                    getMedia(userId, null);
                } else
                    Toast.makeText(ctx, getString(R.string.error_finding_user), Toast.LENGTH_SHORT).show();
            }
        }.execute(userName);
    }

    private void getMedia(final int userId, String nextUrl) {   //  3 or less more liked media
        new GetUserMedia() {
            @Override
            public void onPostExecute(Pair<String, ArrayList<Media>> pair) {
                if(pair != null) {
                    if(pair.second != null && pair.second.size() != 0)
                        mediaList.addAll(pair.second);
                    if (pair.first != null) //  next url exists
                        getMedia(userId, pair.first);
                    else{
                        Intent intent = new Intent(ctx, MostLikedListActivity.class);
                        if(mediaList.size() > 0)
                            intent.putParcelableArrayListExtra(MEDIA_LIST_KEY, getMostLikedMedia());
                        startActivity(intent);
                    }
                }
                else
                    Toast.makeText(ctx, getString(R.string.error_finding_user), Toast.LENGTH_SHORT).show();
            }
        }.execute(String.valueOf(userId), nextUrl);
    }

    private ArrayList<Media> getMostLikedMedia() {
        Comparator<Media> comparator = new Comparator<Media>() {
            @Override
            public int compare(Media media1, Media media2) {
                return media1.getLikesCount() - media2.getLikesCount();
            }
        };
        ArrayList<Media> mostLikedMediaList = new ArrayList<>(NUMBER_OF_PHOTOS_FOR_COLLAGE);
        for (int i = 0; i < NUMBER_OF_PHOTOS_FOR_COLLAGE; i++) {
            if (mediaList.size() == 0) {
                return mostLikedMediaList;
            }
            Media mostLikedMedia = Collections.max(mediaList, comparator);
            mostLikedMediaList.add(mostLikedMedia);
            mediaList.remove(mostLikedMedia);
        }
        return mostLikedMediaList;
    }
}