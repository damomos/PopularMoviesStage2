package com.example.princess.popularmovies.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.princess.popularmovies.R;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Princess on 6/15/2017.
 */

public class Movies implements Parcelable {

    private static final String LOG_TAG = Movies.class.getSimpleName();

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private String date;

    @SerializedName("id")
    private String id;

    @SerializedName("original_title")
    private String title;

    @SerializedName("vote_average")
    private Double rating;

    @SerializedName("backdrop_path")
    private String backdropPath;


    //This method reads the details from the source and its protected so as not to be tampered with
    protected Movies(Parcel in) {
        posterPath = in.readString();
        overview = in.readString();
        date = in.readString();
        id = in.readString();
        title = in.readString();
        rating = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(date);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeDouble(rating);

    }

    //This method accesses the protected data and gets the details
    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies [size];
        }
    };

    public String getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }


    public String getOverview() {
        return overview;
    }

    public Double getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRating(Double rating) {

        this.rating = rating;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setId(String id) {
        this.id = id;

    }

    public String getDate(Context context) {
        String inputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        if(date != null && !date.isEmpty()){
            try {
                Date mDate = inputFormat.parse(date);
                return DateFormat.getDateInstance().format(mDate);
            }
            catch (ParseException e) {
                Log.e(LOG_TAG, "The Release data was not parsed successfully: " + date);
                // Return not formatted date
            }
        } else {
            date = context.getString(R.string.release_date_missing);
        }

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }
}
