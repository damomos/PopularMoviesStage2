package com.example.princess.popularmovies;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.princess.popularmovies.adapters.ReviewsAdapter;
import com.example.princess.popularmovies.adapters.TrailersAdapter;
import com.example.princess.popularmovies.data.FavoriteService;
import com.example.princess.popularmovies.models.Movies;
import com.example.princess.popularmovies.models.Reviews;
import com.example.princess.popularmovies.models.ReviewsResponse;
import com.example.princess.popularmovies.models.Trailers;
import com.example.princess.popularmovies.models.TrailersResponse;
import com.example.princess.popularmovies.rest.ApiClient;
import com.example.princess.popularmovies.rest.ApiService;
import com.example.princess.popularmovies.utils.ConnectionTest;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieDetailsActivity extends AppCompatActivity {

    private static final String API_KEY = BuildConfig.MOVIE_API_KEY;

    public Movies movies;
    private boolean isConnected;
    private ImageView imageView;
    private FloatingActionButton fab;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FavoriteService favoriteService;

    private RecyclerView mRecyclerViewForTrailers;
    private RecyclerView mRecyclerViewForReviews;

    @BindView(R.id.tv_movie_title) TextView title;
    @BindView(R.id.tv_release_date) TextView releaseDate;
    @BindView(R.id.tv_rating) TextView ratings;
    @BindView(R.id.tv_overview) TextView overView;
    @BindViews({R.id.rating_first_star, R.id.rating_second_star, R.id.rating_third_star,
            R.id.rating_fourth_star, R.id.rating_fifth_star}) List<ImageView> ratingStarViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        movies = getIntent().getParcelableExtra("data");

        initCollapsingToolbar();
        updateRatingStar();

        favoriteService = new FavoriteService(getApplicationContext());
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if(favoriteService.isFavorite(movies))
            fab.setImageResource(R.drawable.ic_favorite_black_24dp);
        else
            fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoriteService.isFavorite(movies)){
                    favoriteService.removeFromFavorites(movies);
                    fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }
                else{
                    favoriteService.addToFavorites(movies);
                    fab.setImageResource(R.drawable.ic_favorite_black_24dp);
                }
            }
        });

        imageView = (ImageView) findViewById(R.id.movie_backdrop);
        if(imageView != null){
            String backdrop_url = "http://image.tmdb.org/t/p/w500" + movies.getBackdropPath();
            Picasso.with(getApplicationContext())
                    .load(backdrop_url)
                    .into(imageView);
        }

        title.setText(movies.getTitle());
        releaseDate.setText(movies.getDate(getApplicationContext()));
        ratings.setText(movies.getRating());
        overView.setText(movies.getOverview());

        initViews();
        initViews2();
    }

    private void showSnackbar(String message) {
        Snackbar.make(collapsingToolbarLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void initCollapsingToolbar(){
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(movies.getTitle());
        imageView = (ImageView) collapsingToolbarLayout.findViewById(R.id.movie_backdrop);
    }

    private void initViews(){

        // For horizontal list of trailers
        mRecyclerViewForTrailers = (RecyclerView) findViewById(R.id.trailer_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewForTrailers.setLayoutManager(layoutManager);
        //Trailers divider
        ItemOffsetDecoration itemOffsetDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.item_offset);
        mRecyclerViewForTrailers.addItemDecoration(itemOffsetDecoration);
        mRecyclerViewForTrailers.setNestedScrollingEnabled(false);

        loadTrailers();
    }

    private void loadTrailers(){
        String movie_id = movies.getId();
        try{
            isConnected = ConnectionTest.isNetworkAvailable(this);
            if(isConnected) {
                if (API_KEY.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.api_key_error_message, Toast.LENGTH_LONG).show();
                    return;
                }

                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                Call<TrailersResponse> call = apiService.getMovieTrailers(movie_id, API_KEY);
                call.enqueue(new Callback<TrailersResponse>() {
                    @Override
                    public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                        if (response.isSuccessful()) {
                            List<Trailers> trailers = response.body().getTrailers();
                            mRecyclerViewForTrailers.setAdapter(new TrailersAdapter(getApplicationContext(), trailers));
                        }
                    }

                    @Override
                    public void onFailure(Call<TrailersResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.failure_message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    private void initViews2(){

        mRecyclerViewForReviews = (RecyclerView) findViewById(R.id.review_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewForReviews.setLayoutManager(layoutManager);

        loadReviews();
    }

    public void loadReviews(){
        String movie_id = movies.getId();
        try{
            isConnected = ConnectionTest.isNetworkAvailable(this);
            if(isConnected) {
                if (API_KEY.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.api_key_error_message, Toast.LENGTH_LONG).show();
                    return;
                }

                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                Call<ReviewsResponse> call = apiService.getMovieReviews(movie_id, API_KEY);
                call.enqueue(new Callback<ReviewsResponse>() {
                    @Override
                    public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                        List<Reviews> reviews = response.body().getReviews();
                        mRecyclerViewForReviews.setAdapter(new ReviewsAdapter(getApplicationContext(), reviews));
                    }

                    @Override
                    public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.failure_message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void updateRatingStar(){
        if(movies.getRating() != null && !movies.getRating().isEmpty()){
            String userRatingStar = getResources().getString(R.string.user_rating_movie,
                    movies.getRating());
            ratings.setText(userRatingStar);

            float userRating = Float.valueOf(movies.getRating()) / 2;
            int integerPart = (int) userRating;

            //Fill stars
            for(int i = 0; i < integerPart; i++){
                ratingStarViews.get(i).setImageResource(R.drawable.ic_star_black_24dp);
            }
            //Fill half stars
            if(Math.round(userRating) > integerPart) {
                ratingStarViews.get(integerPart).setImageResource(R.drawable.ic_star_half_black_24dp);
            }
        }else {
            ratings.setVisibility(View.GONE);
        }
    }


    //class for dividing trailers in the recyclerview
    private class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
//            Intent shareIntent = new Intent(Intent.ACTION_SEND);
//            shareIntent.setType("text/plain");
//            shareIntent.putExtra(Intent.EXTRA_SUBJECT, data.getTitle());
            //shareIntent.putExtra(Intent.EXTRA_TEXT);
            // mShareActionProvider.setShareIntent(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}