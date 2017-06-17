package com.example.princess.popularmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Princess on 6/15/2017.
 */

public class MoviesResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Movies> results;

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<com.example.princess.popularmovies.models.Movies> getResults() {
        return results;
    }

    public void setResults(List<com.example.princess.popularmovies.models.Movies> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

}
