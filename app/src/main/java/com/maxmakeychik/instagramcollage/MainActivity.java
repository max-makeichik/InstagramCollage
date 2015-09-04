package com.maxmakeychik.instagramcollage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
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
    private static final int NUMBER_OF_PHOTOS_FOR_COLLAGE = 3;
    Context ctx;
    private ArrayList<Media> mediaList = new ArrayList<>();
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ctx = this;
        nickEditText = (EditText) findViewById(R.id.nickEditText);
        nickEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.d(TAG, "IME_ACTION_DONE ");
                    if (!isLoading) {
                        isLoading = true;
                        hideKeyboard();
                        getUserIdByName(nickEditText.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
        getUserIdByName("max.max.nemax");    //  test
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void getUserIdByName(final String userName) {
        new GetUserIdByName() {
            @Override
            public void onPostExecute(Integer userId) {
                Log.d(TAG, "getUserIdByName onPostExecute " + userId);
                if (userId != null) {
                    getMedia(userId, null);
                } else
                    Toast.makeText(ctx, getString(R.string.error_finding_user), Toast.LENGTH_SHORT).show();
                isLoading = false;
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
                    else
                        showMedia();
                }
                else
                    Toast.makeText(ctx, getString(R.string.error_finding_user), Toast.LENGTH_SHORT).show();
            }
        }.execute(String.valueOf(userId), nextUrl);
    }

    private void showMedia() {
        Intent intent = new Intent(ctx, MostLikedListActivity.class);
        Log.d(TAG, "mediaList: " + mediaList);
        ArrayList<Media> mostLikedImages = getMostLikedImages();
        Log.d(TAG, "getMostLikedImages " + mostLikedImages);
        if(mostLikedImages.size() > 0)
            intent.putParcelableArrayListExtra(MEDIA_LIST_KEY, mostLikedImages);
        startActivity(intent);
    }

    private ArrayList<Media> getMostLikedImages() {
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