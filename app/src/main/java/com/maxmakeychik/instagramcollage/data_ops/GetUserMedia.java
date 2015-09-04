package com.maxmakeychik.instagramcollage.data_ops;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.maxmakeychik.instagramcollage.model.Media;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public abstract class GetUserMedia extends AsyncTask<String, Void, Pair<String, ArrayList<Media>>> {

    private static final String BASE_URL = "https://api.instagram.com/v1/users/%s/media/recent";
    private static final String CLIENT_ID_TAG = "client_id";
    private static final String CLIENT_ID = "a4b3cde66ca64b7e9b6c390220f21085";
    private static final String NEXT_URL_TAG = "next_url";
    private static final String DATA_KEY = "data";
    private static final String PAGINATION_KEY = "pagination";
    private static final String TAG = "GetUserMedia";

    private Uri.Builder uriBuilder;

    public GetUserMedia() {
    }

    @Override
    protected Pair<String, ArrayList<Media>> doInBackground(String... params) {
        if (params[1] == null) {
            uriBuilder = Uri.parse(String.format(BASE_URL, params[0])).buildUpon();
            uriBuilder.appendQueryParameter(CLIENT_ID_TAG, CLIENT_ID);
        }
        else
            uriBuilder = Uri.parse(params[1]).buildUpon();
        Log.d(TAG, "GetUserMedia " + uriBuilder.toString());

        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder().url(uriBuilder.toString()).build();
        try {
            Response response = httpClient.newCall(request).execute();
            JSONObject json = new JSONObject(response.body().string());
            String nextPageUrl = json.getJSONObject(PAGINATION_KEY).optString("next_url", null);

            ArrayList<Media> mediaList = new ArrayList<>();

            JSONArray jsonArray = json.getJSONArray(DATA_KEY);
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if ("image".equals(jsonObject.getString("type")))
                        mediaList.add(new Media(jsonObject));
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