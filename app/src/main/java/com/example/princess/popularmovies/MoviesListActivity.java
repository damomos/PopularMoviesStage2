package com.example.princess.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
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


public class MoviesListActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MoviesListActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private LinearLayout noInternet;
    private LinearLayout noRecordFound;
    private MoviesAdapter mAdapter;
    private List<Movies> moviesList = new ArrayList<>();
    private static SharedPreferences sharedPreferences;
    private int mPosition = RecyclerView.NO_POSITION;
    boolean isPopularMovie;
    boolean isTopratedMovie;
    boolean isFavoriteMovie;
    Loader loader;


    private static final String API_KEY = BuildConfig.MOVIE_API_KEY;

    public static final int MOVIE_LOADER_ID = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
    }

    private void initViews() {
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.api_key_error_message, Toast.LENGTH_LONG).show();
            return;
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        noInternet = (LinearLayout) findViewById(R.id.empty_state_container);
        noRecordFound = (LinearLayout) findViewById(R.id.empty_state_favorites_container);

        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
    }


    private void checkSortOrder() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isPopularMovie = sharedPreferences.getBoolean("popular_key", false);
        isTopratedMovie = sharedPreferences.getBoolean("toprated_key", false);
        isFavoriteMovie = sharedPreferences.getBoolean("favorite_key", false);

        if (isPopularMovie) {
            moviesList.clear();
            popular();
        } else if (isTopratedMovie) {
            moviesList.clear();
            toprated();
        } else if (isFavoriteMovie){
            moviesList.clear();
            loader = getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        }
    }

    private boolean checkConnection(){
        return ConnectionTest.isNetworkAvailable(this);
    }

    private void popular() {

        try {
            if (checkConnection()) {

                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                Call<MoviesResponse> call = apiService.getPopularMovies(API_KEY);
                call.enqueue(new Callback<MoviesResponse>() {

                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                        if (response.isSuccessful()) {

                            moviesList = response.body().getResults();
                            mRecyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), moviesList));
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        //Toast.makeText(getApplicationContext(), R.string.failure_message, Toast.LENGTH_LONG).show();
                        ourView("NoInternet");
                    }
                });
            }
            else {
                ourView("NoInternet");
            }
        }catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void toprated() {

        try {
            if(checkConnection()){

                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
                call.enqueue(new Callback<MoviesResponse>() {

                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        if (response.isSuccessful()) {
                            moviesList = response.body().getResults();
                            mRecyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), moviesList));
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        //Toast.makeText(getApplicationContext(), R.string.failure_message, Toast.LENGTH_LONG).show();
                        ourView("NoInternet");
                    }
                });
            }
            else {
                ourView("NoInternet");
            }
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void fillView(List<Movies> moviesList){
        ourView("Recycler");
        mAdapter = new MoviesAdapter(getApplicationContext(), moviesList);
        mAdapter.notifyDataSetChanged();

        mRecyclerView.removeAllViews();
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
            case "Recycler":
                noInternet.setVisibility(View.GONE);
                noRecordFound.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(moviesList.isEmpty()){
            checkSortOrder();
        }
        else {
            checkSortOrder();
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
        if(isFavoriteMovie){
            return new CursorLoader(this,
                    MoviesContract.FavoriteEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        }
        else{
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){

        if(cursor.getCount()== 0){
            ourView("NoRecordFound");
        }else{

            for(cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                Movies databaseMovies = new Movies(
                        cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteEntry.COLUMN_POSTER_PATH)),
                        cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteEntry.COLUMN_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteEntry.COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteEntry.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteEntry.COLUMN_RATING)),
                        cursor.getString(cursor.getColumnIndex(MoviesContract.FavoriteEntry.COLUMN_BACKDROP_PATH))
                );

                moviesList.add(databaseMovies);
            }
            fillView(moviesList);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0){

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        checkSortOrder();
    }

}
