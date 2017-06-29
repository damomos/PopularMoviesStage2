package com.example.princess.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Princess on 6/15/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    //Database name
    public static final String DATABASE_NAME = "favoriteMovies.db";
    //Database version
    public static final int VERSION = 1;


    public static final String CREATE_FAVORITE_TABLE =
            " CREATE TABLE " + MoviesContract.FavoriteEntry.TABLE_FAVOURITE + "(" +
                    MoviesContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                    MoviesContract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    MoviesContract.FavoriteEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                    MoviesContract.FavoriteEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    MoviesContract.FavoriteEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                    MoviesContract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    MoviesContract.FavoriteEntry.COLUMN_RATING + " TEXT NOT NULL);";


    private static final String TAG = "DBHelper";
    private Context context;


    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavoriteEntry.TABLE_FAVOURITE);
        onCreate(db);
    }
}

