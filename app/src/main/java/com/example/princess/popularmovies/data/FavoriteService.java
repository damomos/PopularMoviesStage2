package com.example.princess.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.princess.popularmovies.models.Movies;

/**
 * Created by Princess on 6/19/2017.
 */

public class FavoriteService {

    private final Context context;

    public FavoriteService(Context context) {
        this.context = context.getApplicationContext();
    }

    public void addToFavorites(Movies movie) {
        ContentValues cv = new ContentValues();
        cv.put(MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID, movie.getId());
        cv.put(MoviesContract.FavoriteEntry.COLUMN_TITLE, movie.getTitle());
        cv.put(MoviesContract.FavoriteEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        cv.put(MoviesContract.FavoriteEntry.COLUMN_RATING, movie.getRating());
        cv.put(MoviesContract.FavoriteEntry.COLUMN_DATE, movie.getDate(context));
        cv.put(MoviesContract.FavoriteEntry.COLUMN_OVERVIEW, movie.getOverview());
        cv.put(MoviesContract.FavoriteEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());

        context.getContentResolver().insert(MoviesContract.FavoriteEntry.CONTENT_URI, cv);
    }

    public void removeFromFavorites(Movies movie) {
        context.getContentResolver().delete(
                MoviesContract.FavoriteEntry.CONTENT_URI,
                MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID + " = " + movie.getId(),
                null
        );
    }

    public boolean isFavorite(Movies movie) {
        boolean favorite = false;
        Cursor cursor = context.getContentResolver().query(
                MoviesContract.FavoriteEntry.CONTENT_URI,
                null,
                MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID + " = " + movie.getId(),
                null,
                null
        );
        if (cursor != null) {
            favorite = cursor.getCount() != 0;
            cursor.close();
        }
        return favorite;
    }

}


