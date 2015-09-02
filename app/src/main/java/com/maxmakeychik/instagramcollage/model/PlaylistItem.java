package com.maxmakeychik.instagramcollage.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlaylistItem implements Parcelable {

    String title;
    String date;
    int likes;
    String thumbnailUrl;

    private static final String TAG = "PlaylistItem";

    private PlaylistItem(Parcel in) {
        title = in.readString();
        date = in.readString();
        thumbnailUrl = in.readString();
        thumbnailUrl = in.readString();
    }

    @Override
    public String toString() {
        return "PlaylistItem{" +
                ", title='" + title + '\'' +
                ", likes='" + likes + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(title);
        out.writeString(date);
        out.writeInt(likes);
        out.writeString(thumbnailUrl);
    }

    public static final Creator<PlaylistItem> CREATOR = new Creator<PlaylistItem>() {
        public PlaylistItem createFromParcel(Parcel in) {
            return new PlaylistItem(in);
        }

        public PlaylistItem[] newArray(int size) {
            return new PlaylistItem[size];
        }
    };
}