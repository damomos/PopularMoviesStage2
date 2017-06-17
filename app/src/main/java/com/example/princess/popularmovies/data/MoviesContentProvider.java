package com.example.princess.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.princess.popularmovies.data.MoviesContentProvider.MovieEntry.TABLE_DETAILS;

/**
 * Created by Princess on 6/15/2017.
 */

public class MoviesContentProvider extends ContentProvider {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.princess.popularmovies";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {

        // MovieEntry content URI = base content URI + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        //Movie Tables name
        public static final String TABLE_DETAILS = "details";
        public static final String TABLE_FAVOURITE = "favourite";

        //Movies Columns names
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RATING = "rating";

        //This creates the columns for the favourite table
        public static final String COLUMN_FAVOURITE = "favourite";
        public static final String COLUMN_POSTERPATH = "posterPath";
    }

    public static final int MOVIE = 1;
    public static final int MOVIE_WITH_ID = 2;

    //Declare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, PATH_MOVIE, MOVIE);
        uriMatcher.addURI(AUTHORITY,PATH_MOVIE + "/#", MOVIE_WITH_ID);
        return uriMatcher;
    }

    private MoviesDbHelper mMoviesDbHelper;


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMoviesDbHelper = new MoviesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mMoviesDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int movie = sUriMatcher.match(uri);
        Cursor retCursor;

        // Query for the tasks directory and write a default case
        switch (movie) {
            // Query for the tasks directory
            case MOVIE:
                retCursor =  db.query(TABLE_DETAILS,
                        projection,
                        selection,
                        null,
                        null,
                        null,
                        null);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //Get access to database to write data into it
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        int movie = sUriMatcher.match(uri);
        Uri returnUri;


        switch(movie){
            case MOVIE:
                // Insert new values into the database
                // Inserting values into tasks table
                long id = db.insert(TABLE_DETAILS, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
