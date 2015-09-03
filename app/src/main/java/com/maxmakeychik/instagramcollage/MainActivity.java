package com.maxmakeychik.instagramcollage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.widget.EditText;
import android.widget.Toast;

import com.maxmakeychik.instagramcollage.model.Media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean isLoading = false;
    EditText nickEditText;
    private static final String COLLAGES_DIRECTORY_NAME = "InstagramCollage";
    private static final int NUMBER_OF_PHOTOS_FOR_COLLAGE = 3;
    Context ctx;
    protected static final String RESPONSE_PAGINATION_KEY = "pagination";

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
                    final List<Media> images = new ArrayList<>();
                    List<Media> newImages = getImages(userId);
                }
                else
                    Toast.makeText(ctx, getString(R.string.error_finding_user), Toast.LENGTH_SHORT).show();
            }
        }.execute(userName);
    }

    private List<Media> getImages(final int userId) {
        new GetUserMedia() {
            @Override
            public void onPostExecute(Pair<String, List<Media>> pair) {
                if(pair != null)
                    getImages(userId);
                else
                    Toast.makeText(ctx, getString(R.string.error_finding_user), Toast.LENGTH_SHORT).show();
            }
        }.execute(String.valueOf(userId));
        Intent intent = new Intent(this, MostLikedListActivity.class);
    }
}