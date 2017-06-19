package com.example.princess.popularmovies.rest;

import com.example.princess.popularmovies.models.MoviesResponse;
import com.example.princess.popularmovies.models.ReviewsResponse;
import com.example.princess.popularmovies.models.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Princess on 6/15/2017.
 */

public interface ApiService {

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<MoviesResponse> getMoviesDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<TrailersResponse> getMovieTrailers(@Path("movie_id") String id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewsResponse> getMovieReviews(@Path("movie_id") String id, @Query("api_key") String apiKey);
}
