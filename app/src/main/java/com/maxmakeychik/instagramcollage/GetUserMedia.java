package com.maxmakeychik.instagramcollage;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.maxmakeychik.instagramcollage.model.Image;
import com.maxmakeychik.instagramcollage.model.Media;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class GetUserMedia extends AsyncTask<String, Void, Pair<String, List<Media>>> {

    private static final String TAG = "GetUserMedia";
    private static final String BASE_URL = "https://api.instagram.com/v1/users/%s/media/recent";
    private static final String CLIENT_ID_TAG = "client_id";
    private static final String CLIENT_ID = "a4b3cde66ca64b7e9b6c390220f21085";
    private static final String MAX_ID_TAG = "max_id";
    private static final String DATA_KEY = "data";
    private static final String PAGINATION_KEY = "pagination";

    protected Uri.Builder uriBuilder;

    public GetUserMedia() {}

    @Override
    protected Pair<String, List<Media>> doInBackground(String... params) {
        uriBuilder = Uri.parse(String.format(BASE_URL, params[0])).buildUpon();
        if(params[1] != null)
            uriBuilder.appendQueryParameter(MAX_ID_TAG, params[1]);
        uriBuilder.appendQueryParameter(CLIENT_ID_TAG, CLIENT_ID);
        Log.d(TAG, "GetUserMedia " + uriBuilder.toString());

        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder().url(uriBuilder.toString()).build();
        try {
            Response response = httpClient.newCall(request).execute();
            JSONObject json = new JSONObject(response.body().string());
            String nextPageUrl = json.getJSONObject(PAGINATION_KEY).optString("next_url", null);

            List<Media> mediaList = new ArrayList<>();

            JSONArray jsonArray = json.getJSONArray(DATA_KEY);
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return new Pair<>(nextPageUrl, mediaList);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}