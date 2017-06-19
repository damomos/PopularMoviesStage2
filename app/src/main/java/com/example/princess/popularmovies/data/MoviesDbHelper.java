package com.example.princess.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.princess.popularmovies.data.MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID_KEY;

/**
 * Created by Princess on 6/15/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String MOVIE_HELPER_TAG = MoviesDbHelper.class.getSimpleName();

    //Database name
    private static final String DATABASE_NAME = "popularMovies.db";

    //Database version
    private static final int VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static final String CREATE_MOVIE_TABLE =
            " CREATE TABLE " + MoviesContract.MovieEntry.TABLE_DETAILS + "(" +
            MoviesContract.MovieEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MoviesContract.MovieEntry.COLUMN_MOVIE_ID                + " INTEGER NOT NULL, " +
            MoviesContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
            MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
            MoviesContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
            MoviesContract.MovieEntry.COLUMN_DATE + " TEXT NOT NULL, " +
            MoviesContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            MoviesContract.MovieEntry.COLUMN_RATING + " REAL NOT NULL);";


    public static final String CREATE_FAVORITE_TABLE =
            "CREATE TABLE " + MoviesContract.FavoriteEntry.TABLE_FAVOURITE + " (" +
                    MoviesContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID_KEY + " INTEGER NOT NULL, " +

                    " FOREIGN KEY (" + COLUMN_MOVIE_ID_KEY + ") REFERENCES " +
                    MoviesContract.MovieEntry.TABLE_DETAILS + " (" + MoviesContract.MovieEntry._ID + ") " +

                    " );";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOVIE_TABLE);
        db.execSQL(CREATE_FAVORITE_TABLE);

        Log.v(MOVIE_HELPER_TAG, "Database created Successfully");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavoriteEntry.TABLE_FAVOURITE);
        onCreate(db);

    }
}
