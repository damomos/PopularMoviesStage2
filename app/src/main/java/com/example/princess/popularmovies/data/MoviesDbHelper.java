package com.example.princess.popularmovies.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.example.princess.popularmovies.data.MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID_KEY;

/**
 * Created by Princess on 6/15/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    //Database name
    public static final String DATABASE_NAME = "popularMovies.db";
    //Database version
    public static final int VERSION = 1;

    public static final String CREATE_MOVIE_TABLE =
            " CREATE TABLE " + MoviesContract.MovieEntry.TABLE_DETAILS + "(" +
                    MoviesContract.MovieEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MoviesContract.MovieEntry.COLUMN_MOVIE_ID                + " INTEGER NOT NULL, " +
                    MoviesContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                    MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                    MoviesContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                    MoviesContract.MovieEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                    MoviesContract.MovieEntry.COLUMN_MOVIE_CATEGORY + " TEXT NOT NULL, " +
                    MoviesContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    MoviesContract.MovieEntry.COLUMN_RATING + " TEXT NOT NULL);";


    public static final String CREATE_FAVORITE_TABLE =
            "CREATE TABLE " + MoviesContract.FavoriteEntry.TABLE_FAVOURITE + " (" +
                    MoviesContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MOVIE_ID_KEY + " INTEGER NOT NULL, " +
                    " FOREIGN KEY (" + COLUMN_MOVIE_ID_KEY + ") REFERENCES " +
                    MoviesContract.MovieEntry.TABLE_DETAILS + " (" + MoviesContract.MovieEntry._ID + ") " +
                    " );";


    public static final String[] MAIN_MOVIE_ROJECTION = {
            MoviesContract.MovieEntry.COLUMN_MOVIE_ID,
            MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieEntry._ID
    };

    public static final String[] MOVIE_DETAIL_ROJECTION = {
            MoviesContract.MovieEntry.COLUMN_TITLE,
            MoviesContract.MovieEntry.COLUMN_RATING,
            MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MoviesContract.MovieEntry.COLUMN_DATE,
            MoviesContract.MovieEntry.COLUMN_OVERVIEW,
            MoviesContract.MovieEntry.COLUMN_MOVIE_CATEGORY,
            MoviesContract.MovieEntry._ID,
            MoviesContract.MovieEntry.COLUMN_MOVIE_ID
    };


    private static final String TAG = "DBHelper";
    private static MoviesDbHelper mInstance = null;
    private Context context;

    public static synchronized MoviesDbHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MoviesDbHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @VisibleForTesting
    public static void clearInstance() {
        mInstance = null;
    }

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOVIE_TABLE);
        db.execSQL(CREATE_FAVORITE_TABLE);

        Log.v(TAG, "Database created Successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavoriteEntry.TABLE_FAVOURITE);
        onCreate(db);
//        Log.e(TAG, "Updating table from " + oldVersion + " to " + newVersion);
//        // You will not need to modify this unless you need to do some android specific things.
//        // When upgrading the database, all you need to do is add a file to the assets folder and name it:
//        // from_1_to_2.sql with the version that you are upgrading to as the last version.
//        for (int i = oldVersion; i < newVersion; ++i) {
//            String migrationName = String.format("from_%d_to_%d.sql", i, (i + 1));
//            Log.d(TAG, "Looking for migration file: " + migrationName);
//            readAndExecuteSQLScript(db, context, migrationName);
    }


//            db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_DETAILS);
//            db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavoriteEntry.TABLE_FAVOURITE);
//            onCreate(db);


//
//    @Override
//    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//    }
//
//    private void readAndExecuteSQLScript(SQLiteDatabase db, Context ctx, String fileName) {
//        if (TextUtils.isEmpty(fileName)) {
//            Log.d(TAG, "SQL script file name is empty");
//            return;
//        }
//
//        Log.d(TAG, "Script found. Executing...");
//        AssetManager assetManager = ctx.getAssets();
//        BufferedReader reader = null;
//
//        try {
//            InputStream is = assetManager.open(fileName);
//            InputStreamReader isr = new InputStreamReader(is);
//            reader = new BufferedReader(isr);
//            executeSQLScript(db, reader);
//        } catch (IOException e) {
//            Log.e(TAG, "IOException:", e);
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    Log.e(TAG, "IOException:", e);
//                }
//            }
//        }
//    }
//
//    private void executeSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
//        String line;
//        StringBuilder statement = new StringBuilder();
//        while ((line = reader.readLine()) != null) {
//            statement.append(line);
//            statement.append("\n");
//            if (line.endsWith(";")) {
//                db.execSQL(statement.toString());
//                statement = new StringBuilder();
//            }
//        }
//    }
}

