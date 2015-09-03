package com.maxmakeychik.instagramcollage.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Media implements Parcelable {

    private int id;
    private String title;
    private String date;
    private int likesCount;
    private String imageUrl;
    private int imageWidth;
    private int imageHeight;

    @Override
    public String toString() {
        return "PlaylistItem{" +
                ", title='" + title + '\'' +
                ", likesCount='" + likesCount + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    private Media(Parcel in) {
        id = in.readInt();
        title = in.readString();
        date = in.readString();
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
        out.writeInt(id);
        out.writeString(title);
        out.writeString(date);
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