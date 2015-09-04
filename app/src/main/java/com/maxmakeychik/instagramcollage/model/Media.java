package com.maxmakeychik.instagramcollage.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Media implements Parcelable {

    private String id;
    private String text;
    private long date;
    private int likesCount;
    private String imageUrl;
    private int imageWidth;
    private int imageHeight;
    private static final String TAG = "Media";

    @Override
    public String toString() {
        return "Media{" +
                ", date='" + getDate() + '\'' +
                ", text='" + text + '\'' +
                ", likesCount='" + likesCount + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public Media(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getString("id");
        JSONObject caption = jsonObject.optJSONObject("caption");
        if(caption != null)
            text = caption.getString("text");
        likesCount = jsonObject.getJSONObject("likes").getInt("count");
        date = jsonObject.getLong("created_time");
        JSONObject images = jsonObject.getJSONObject("images");
        imageUrl = images.getJSONObject("standard_resolution").getString("url");
        imageWidth = images.getJSONObject("standard_resolution").getInt("width");
        imageHeight = images.getJSONObject("standard_resolution").getInt("height");
        Log.d(TAG, "text " + text + ", likesCount " + likesCount + ", date " + date);
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        long timestampLong = date * 1000;
        Date d = new Date(timestampLong);
        DateFormat df = new SimpleDateFormat("d MMM yyyy");

        System.out.println(df.format(d));
        return df.format(d).toLowerCase();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public String getLikesCountString() {
        return String.valueOf(likesCount);
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    private Media(Parcel in) {
        id = in.readString();
        text = in.readString();
        date = in.readLong();
        likesCount = in.readInt();
        imageUrl = in.readString();
        imageWidth = in.readInt();
        imageHeight = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id);
        out.writeString(text);
        out.writeLong(date);
        out.writeInt(likesCount);
        out.writeString(imageUrl);
        out.writeInt(imageWidth);
        out.writeInt(imageHeight);
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

}