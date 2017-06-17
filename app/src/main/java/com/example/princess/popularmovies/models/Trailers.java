package com.example.princess.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static android.R.attr.id;

/**
 * Created by Princess on 6/15/2017.
 */

public class Trailers implements Parcelable{

    @SerializedName("id")
    private String id;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;


    protected Trailers(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
    }

    public static final Creator<Trailers> CREATOR = new Creator<Trailers>() {
        @Override
        public Trailers createFromParcel(Parcel in) {
            return new Trailers(in);
        }

        @Override
        public Trailers[] newArray(int size) {
            return new Trailers[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
