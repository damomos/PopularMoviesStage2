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
            // context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movie.toContentValues());
            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID_KEY, movie.getId());
            context.getContentResolver().insert(MoviesContract.FavoriteEntry.CONTENT_URI, contentValues);
        }

        public void removeFromFavorites(Movies movie) {
            context.getContentResolver().delete(
                    MoviesContract.FavoriteEntry.CONTENT_URI,
                    MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID_KEY + " = " + movie.getId(),
                    null
            );
        }

        public boolean isFavorite(Movies movie) {
            boolean favorite = false;
            Cursor cursor = context.getContentResolver().query(
                    MoviesContract.FavoriteEntry.CONTENT_URI,
                    null,
                    MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID_KEY + " = " + movie.getId(),
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
