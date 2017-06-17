package com.example.princess.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static android.R.attr.id;

/**
 * Created by Princess on 6/15/2017.
 */

public class TrailersResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<Trailers> trailers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailers> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailers> trailers) {
        this.trailers = trailers;
    }
}
