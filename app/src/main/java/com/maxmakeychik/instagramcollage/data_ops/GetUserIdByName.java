package com.maxmakeychik.instagramcollage.data_ops;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public abstract class GetUserIdByName extends AsyncTask<String, Void, Integer> {

    private static final String TAG = "GetIdByName";
    private static final String BASE_URL = "https://api.instagram.com/v1/users/search";
    private static final String CLIENT_ID_TAG = "client_id";
    private static final String USERNAME_TAG = "username";
    private static final String CLIENT_ID = "a4b3cde66ca64b7e9b6c390220f21085";
    private static final String DATA_KEY = "data";

    protected Uri.Builder uriBuilder;

    public GetUserIdByName() {
        uriBuilder = Uri.parse(BASE_URL).buildUpon();
    }

    @Override
    protected Integer doInBackground(String... params) {
        String userName = params[0];
        uriBuilder.appendQueryParameter(CLIENT_ID_TAG, CLIENT_ID);
        uriBuilder.appendQueryParameter("q", userName);
        Log.d(TAG, "getIdByName " + uriBuilder.toString());

        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder().url(uriBuilder.toString()).build();
        try {
            Response response = httpClient.newCall(request).execute();
            JSONObject json = new JSONObject(response.body().string());
            JSONArray jsonArray = json.getJSONArray(DATA_KEY);
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (userName.equalsIgnoreCase(jsonObject.getString(USERNAME_TAG)))
                        return jsonObject.getInt("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
