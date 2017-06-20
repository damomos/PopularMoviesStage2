package com.example.princess.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
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


public class MoviesListActivity extends AppCompatActivity
        //implements LoaderManager.LoaderCallbacks<Cursor>
                                                            {

    private static final String TAG = MoviesListActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;
    private List<Movies> moviesList;
    boolean isConnected;
    private static SharedPreferences sharedPreferences;
    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar mProgressBar;

    private static final String API_KEY = BuildConfig.MOVIE_API_KEY;

    public static final int INDEX_MOVIE_POSTERPATH = 1;
    public static final int ID_MOVIE_LOADER = 20;


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

        moviesList = new ArrayList<>();
        mAdapter = new MoviesAdapter(this, moviesList);

        if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        mRecyclerView.setAdapter(mAdapter);
            checkSortOrder();
    }

    private void initViews2(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        moviesList = new ArrayList<>();
        mAdapter = new MoviesAdapter(this, moviesList);

        if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        mRecyclerView.setAdapter(mAdapter);

        //getAllFavorite();
    }

    public void popular(){

        try{
        isConnected = ConnectionTest.isNetworkAvailable(this);
        if(isConnected) {
            if (API_KEY.isEmpty()) {
                Toast.makeText(getApplicationContext(),R.string.api_key_error_message, Toast.LENGTH_LONG);

            }
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<MoviesResponse> call = apiService.getPopularMovies(API_KEY);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List<Movies> mMovies = response.body().getResults();
                    mRecyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), mMovies));
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), R.string.failure_message, Toast.LENGTH_LONG).show();
                }
            });
        }
        } catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void toprated(){

        try{
            isConnected = ConnectionTest.isNetworkAvailable(this);
            if(isConnected) {
                if (API_KEY.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.api_key_error_message, Toast.LENGTH_LONG).show();
                }
                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
                call.enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        List<Movies> mMovies = response.body().getResults();
                        mRecyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), mMovies));
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.failure_message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    public void checkSortOrder(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isPopularMovie = sharedPreferences.getBoolean("popular_key", false);
        boolean isTopratedMovie = sharedPreferences.getBoolean("toprated_key", false);
        boolean isFavorite = sharedPreferences.getBoolean("favorite_key", false);

        if(isPopularMovie){
            popular();
        } else if(isTopratedMovie){
            toprated();
        }else if(isFavorite){
           initViews2();
        }else
            popular();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(moviesList.isEmpty()){
            checkSortOrder();
        }else{
            checkSortOrder();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

//

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

//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        switch (id) {
//
//            case ID_MOVIE_LOADER:
//                /* URI for all rows of movie data in our movie table */
//                Uri movieQueryUri = MoviesContract.MovieEntry.CONTENT_URI;
//
//                return new CursorLoader(this,
//                        movieQueryUri,
//                        MoviesContract.MAIN_MOVIE_ROJECTION,
//                        null,
//                        null,
//                        null);
//
//            default:
//                throw new RuntimeException("Loader Not Implemented: " + id);
//        }
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        return;
//        //mAdapter.swapCursor(data);
////        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
////        mRecyclerView.smoothScrollToPosition(mPosition);
////        if (data.getCount() != 0) showMovieDataView();
//    }
//
//    private void showMovieDataView() {
////        /* First, hide the loading indicator */
////        mProgressBar.setVisibility(View.INVISIBLE);
////        /* Finally, make sure the movie data is visible */
////        mRecyclerView.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        //mAdapter.swapCursor(null);
//
//    }
}
