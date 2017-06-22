package com.example.princess.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.princess.popularmovies.adapters.MoviesAdapter;
import com.example.princess.popularmovies.data.MoviesContract;
import com.example.princess.popularmovies.data.MoviesDbHelper;
import com.example.princess.popularmovies.models.Movies;
import com.example.princess.popularmovies.models.MoviesResponse;
import com.example.princess.popularmovies.rest.ApiClient;
import com.example.princess.popularmovies.rest.ApiService;
import com.example.princess.popularmovies.utils.ConnectionTest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoviesListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MoviesListActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private LinearLayout noInternet;
    private LinearLayout noRecordFound;
    private MoviesAdapter mAdapter;
    private List<Movies> moviesList = new ArrayList<>();
    private  List<Movies> moviesListCur = new ArrayList<>();
    private static SharedPreferences sharedPreferences;
    boolean isPopularMovie;
    boolean isTopratedMovie;
    boolean isFavoriteMovie;
    Loader loader;
    Uri uri;

    private static final String API_KEY = BuildConfig.MOVIE_API_KEY;

    public static final int MOVIE_LOADER_ID = 20;
    public static final int FAVORITE_MOVIE_LOADER_ID = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
    }

    private void initViews(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        noInternet = (LinearLayout) findViewById(R.id.empty_state_container);
        noRecordFound = (LinearLayout) findViewById(R.id.empty_state_favorites_container);

        if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }


    private void checkSortOrder() {
        isPopularMovie = sharedPreferences.getBoolean("popular_key", true);
        isTopratedMovie = sharedPreferences.getBoolean("toprated_key", false);
        isFavoriteMovie = sharedPreferences.getBoolean("favorite_key", false);
        boolean isFirstTimeCallPopular = sharedPreferences.getBoolean("firsttimepopular_key", true);
        boolean isFirstTimeCallTop = sharedPreferences.getBoolean("firsttimetop_key", true);

        if (isPopularMovie) {
            if(isFirstTimeCallPopular){
                popular();
            }
            else {
                //CheckDatabase
                loader = getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
            }
        } else if (isTopratedMovie) {
            if (isFirstTimeCallTop){
                toprated();
            }
            else {
                //CheckDatabase
                loader = getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
            }
        } else if (isFavoriteMovie) {
            loader = getSupportLoaderManager().initLoader(FAVORITE_MOVIE_LOADER_ID, null, this);
        }else{
            ourView("NoRecordFound");
        }
    }

    private void convertCursor2List(Cursor cursor){
        if(cursor.getCount()== 0){
            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            ourView("NoRecordFound");
            return;
        }

        for(cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            Movies databaseMovies = new Movies(
                    cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH)),
                    cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_DATE)),
                    cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID)),
                    cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RATING)),
                    cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH))
            );
            moviesListCur.add(databaseMovies);
        }
        fillView(moviesListCur);
    }

    private boolean checkConnection(){
        return ConnectionTest.isNetworkAvailable(this);
    }

    private void popular() {
        try {

            if (checkConnection()) {

                if (API_KEY.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.api_key_error_message, Toast.LENGTH_LONG).show();
                    ourView("NoRecordFound");
                    return;
                }
                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                Call<MoviesResponse> call = apiService.getPopularMovies(API_KEY);
                call.enqueue(new Callback<MoviesResponse>() {

                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                        if (response.isSuccessful()) {

                            moviesList = response.body().getResults();

                            ContentValues cv = new ContentValues();
                            for (int j = 0; j < moviesList.size(); j++) {
                                cv.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, moviesList.get(j).getId());
                                cv.put(MoviesContract.MovieEntry.COLUMN_TITLE, moviesList.get(j).getTitle());
                                cv.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, moviesList.get(j).getPosterPath());
                                cv.put(MoviesContract.MovieEntry.COLUMN_RATING, moviesList.get(j).getRating());
                                cv.put(MoviesContract.MovieEntry.COLUMN_DATE, moviesList.get(j).getDate(getApplicationContext()));
                                cv.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, moviesList.get(j).getOverview());
                                cv.put(MoviesContract.MovieEntry.COLUMN_MOVIE_CATEGORY, "popular");
                                cv.put(MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH, moviesList.get(j).getBackdropPath());
                                getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, cv);
                            }

                            sharedPreferences.edit().putBoolean("firsttimepopular_key", false).commit();
                            fillView(moviesList);
                        } else {
                            ourView("NoRecordFound");
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.failure_message, Toast.LENGTH_LONG).show();
                        ourView("NoInternet");
                    }
                });
            }
            else{
                ourView("NoInternet");
            }
        }catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void toprated() {
        try {

            if (checkConnection()) {

                if (API_KEY.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.api_key_error_message, Toast.LENGTH_LONG).show();
                    ourView("NoRecordFound");
                    return;
                }
                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
                call.enqueue(new Callback<MoviesResponse>() {

                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            moviesList = response.body().getResults();

                            ContentValues cv = new ContentValues();
                            for (int j = 0; j < moviesList.size(); j++) {
                                cv.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, moviesList.get(j).getId());
                                cv.put(MoviesContract.MovieEntry.COLUMN_TITLE, moviesList.get(j).getTitle());
                                cv.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, moviesList.get(j).getPosterPath());
                                cv.put(MoviesContract.MovieEntry.COLUMN_RATING, moviesList.get(j).getRating());
                                cv.put(MoviesContract.MovieEntry.COLUMN_DATE, moviesList.get(j).getDate(getApplicationContext()));
                                cv.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, moviesList.get(j).getOverview());
                                cv.put(MoviesContract.MovieEntry.COLUMN_MOVIE_CATEGORY, "toprated");
                                cv.put(MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH, moviesList.get(j).getBackdropPath());
                                getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, cv);
                            }

                            sharedPreferences.edit().putBoolean("firsttimetop_key", false).commit();
                            fillView(moviesList);

                        } else {
                            ourView("NoRecordFound");
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.failure_message, Toast.LENGTH_LONG).show();
                        ourView("NoInternet");
                    }
                });
            } else {
                ourView("NoInternet");
            }
        }catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void fillView(List<Movies> moviesList){
        mAdapter = new MoviesAdapter(getApplicationContext(), moviesList);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void ourView(String inView){
        switch (inView){
            case "NoInternet":
                noInternet.setVisibility(View.VISIBLE);
                noRecordFound.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                break;
            case "NoRecordFound":
                noInternet.setVisibility(View.GONE);
                noRecordFound.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(loader!=null){
            getContentResolver().registerContentObserver(uri,true,new DataObserver(new Handler()));
        }
        checkSortOrder();
    }

    class DataObserver extends ContentObserver{
        public DataObserver(Handler handler){
            super(handler);
        }
        @Override
        public void onChange(boolean selfChange, Uri uri){
            loader.forceLoad();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle arg){
        if(isPopularMovie){
            uri = MoviesContract.MovieEntry.CONTENT_URI;
            return new CursorLoader(this, uri,
                    null, MoviesContract.MovieEntry.COLUMN_MOVIE_CATEGORY + " = ?", new String[] {"popular"}, null);
        }
        else if(isTopratedMovie){
            uri = MoviesContract.MovieEntry.CONTENT_URI;
            return new CursorLoader(this, uri,
                    null, MoviesContract.MovieEntry.COLUMN_MOVIE_CATEGORY + " = ?", new String[] {"toprated"}, null);
        }
        else {
            uri = MoviesContract.FavoriteEntry.CONTENT_URI;
            return new CursorLoader(this, uri,
                    MoviesDbHelper.MAIN_MOVIE_ROJECTION,null,null, null);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){

        if(cursor.getCount()== 0){
            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            ourView("NoRecordFound");
            return;
        }

        for(cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            Movies databaseMovies = new Movies(
                    cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH)),
                    cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_DATE)),
                    cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID)),
                    cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RATING)),
                    cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH))
            );
            moviesListCur.add(databaseMovies);
        }
        fillView(moviesListCur);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0){
    }

}
