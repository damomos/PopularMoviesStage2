package com.example.princess.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Princess on 6/15/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {
    //Database name
    private static final String DATABASE_NAME = "popularMovies.db";

    //Database version
    private static final int VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    static final String CREATE_TABLE_DETAILS = " CREATE TABLE " + MoviesContentProvider.MovieEntry.TABLE_DETAILS + "(" +
            MoviesContentProvider.MovieEntry._ID                + " INTEGER PRIMARY KEY, " +
            MoviesContentProvider.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
            MoviesContentProvider.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
            MoviesContentProvider.MovieEntry.COLUMN_DATE + " TEXT NOT NULL, " +
            MoviesContentProvider.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            MoviesContentProvider.MovieEntry.COLUMN_RATING + " TEXT NOT NULL);";

    static final String CREATE_TABLE_FAVOURITE = " CREATE TABLE " + MoviesContentProvider.MovieEntry.TABLE_FAVOURITE + "(" +
            MoviesContentProvider.MovieEntry._ID                + " INTEGER PRIMARY KEY, " +
            MoviesContentProvider.MovieEntry.COLUMN_POSTERPATH + " TEXT NOT NULL, " +
            MoviesContentProvider.MovieEntry.COLUMN_FAVOURITE + " TEXT NOT NULL);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DETAILS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContentProvider.MovieEntry.TABLE_DETAILS);
        onCreate(db);

    }
}
