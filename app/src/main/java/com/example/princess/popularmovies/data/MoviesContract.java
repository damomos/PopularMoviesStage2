package com.example.princess.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Princess on 6/15/2017.
 */

public class MoviesContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.princess.popularmovies";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    public static final String PATH_FAVORITE = "favorite";

    public static final class FavoriteEntry implements BaseColumns {
        //Favorite table name
        public static final String TABLE_FAVOURITE = "favourite";

        //Movies Columns names
        public static final String _ID = "id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";


        // MovieEntry content URI = base content URI + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_FAVORITE;

        public static Uri buildFavoriteUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
