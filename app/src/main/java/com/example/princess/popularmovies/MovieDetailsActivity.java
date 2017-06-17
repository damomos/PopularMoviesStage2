package com.example.princess.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.princess.popularmovies.adapters.MoviesAdapter;
import com.example.princess.popularmovies.adapters.ReviewsAdapter;
import com.example.princess.popularmovies.adapters.TrailersAdapter;
import com.example.princess.popularmovies.models.Movies;
import com.example.princess.popularmovies.models.MoviesResponse;
import com.example.princess.popularmovies.models.Reviews;
import com.example.princess.popularmovies.models.ReviewsResponse;
import com.example.princess.popularmovies.models.Trailers;
import com.example.princess.popularmovies.models.TrailersResponse;
import com.example.princess.popularmovies.rest.ApiClient;
import com.example.princess.popularmovies.rest.ApiService;
import com.example.princess.popularmovies.utils.ConnectionTest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.data;
import static android.os.Build.VERSION_CODES.M;
import static com.example.princess.popularmovies.R.id.coordinatorLayout;
import static com.example.princess.popularmovies.R.id.fab;
import static com.example.princess.popularmovies.R.id.trailer_list;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String API_KEY = BuildConfig.MOVIE_API_KEY;

    private List<Movies> movies;
    private boolean isConnected;
    private ImageView imageView;
    private CoordinatorLayout coordinatorLayout;
    private TrailersAdapter mTrailersAdapter;
    private List<Trailers> trailersList;
    private ReviewsAdapter mReviewsAdapter;
    private List<Reviews> reviewsList;
    private ShareActionProvider mShareActionProvider;

    private RecyclerView mRecyclerViewForTrailers;
    private RecyclerView mRecyclerViewForReviews;

    @BindView(R.id.tv_movie_title) TextView title;
    @BindView(R.id.tv_release_date) TextView releaseDate;
    @BindView(R.id.tv_rating) TextView ratings;
    @BindView(R.id.tv_overview) TextView overView;
    //@BindViews({R.id.rating_first_star, R.id.rating_second_star, R.id.rating_third_star,
                //R.id.rating_fourth_star, R.id.rating_fifth_star}) List<ImageView> ratingStarViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        Movies data = getIntent().getParcelableExtra("data");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        title.setText(data.getTitle());
        releaseDate.setText(data.getDate(getApplicationContext()));
        ratings.setText(data.getRating().toString());
        overView.setText(data.getOverview());

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(data.getTitle());

        imageView = (ImageView) findViewById(R.id.movie_backdrop);
        if(imageView != null){
            String backdrop_url = "http://image.tmdb.org/t/p/w185" + data.getBackdropPath();
            Picasso.with(getApplicationContext())
                    .load(backdrop_url)
                    .into(imageView);
        }

        initViews();
        initViews2();

    }

    private void initViews(){
        trailersList = new ArrayList<>();
        mTrailersAdapter = new TrailersAdapter(this, trailersList);
        // For horizontal list of trailers
        mRecyclerViewForTrailers = (RecyclerView) findViewById(R.id.trailer_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewForTrailers.setLayoutManager(layoutManager);
        mRecyclerViewForTrailers.setAdapter(mTrailersAdapter);
        mTrailersAdapter.notifyDataSetChanged();
        mRecyclerViewForTrailers.setNestedScrollingEnabled(false);

        loadTrailers();
    }

    private void loadTrailers(){
        int movie_id = getIntent().getExtras().getInt("id");
        try{
            isConnected = ConnectionTest.isNetworkAvailable(this);
            if(isConnected) {
                if (API_KEY.isEmpty()) {
                    Snackbar bar = Snackbar.make(coordinatorLayout, R.string.api_key_error_message, Snackbar.LENGTH_LONG);
                    bar.show();
                }
                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                Call<TrailersResponse> call = apiService.getMovieTrailers(movie_id, API_KEY);
                call.enqueue(new Callback<TrailersResponse>() {
                    @Override
                    public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                       List<Trailers> trailers = response.body().getTrailers();
                       mRecyclerViewForTrailers.setAdapter(new TrailersAdapter(getApplicationContext(), trailers));
                    }

                    @Override
                    public void onFailure(Call<TrailersResponse> call, Throwable t) {
                        //Snackbar bar = Snackbar.make(coordinatorLayout, R.string.failure_message, Snackbar.LENGTH_LONG);
                        //bar.show();
                    }
                });
            }
        } catch (Exception e){
            Log.d("Error", e.getMessage());
            //Snackbar bar = Snackbar.make(coordinatorLayout, e.toString(), Snackbar.LENGTH_LONG);
            //bar.show();

        }
    }

    private void initViews2(){
        reviewsList = new ArrayList<>();
        mReviewsAdapter = new ReviewsAdapter(this, reviewsList);

        mRecyclerViewForReviews = (RecyclerView) findViewById(R.id.review_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewForReviews.setLayoutManager(layoutManager);
        mRecyclerViewForReviews.setAdapter(mReviewsAdapter);
        mReviewsAdapter.notifyDataSetChanged();

        loadReviews();
    }

    public void loadReviews(){
        int movie_id = getIntent().getExtras().getInt("id");
        try{
            isConnected = ConnectionTest.isNetworkAvailable(this);
            if(isConnected) {
                if (API_KEY.isEmpty()) {
                    Snackbar bar = Snackbar.make(coordinatorLayout, R.string.api_key_error_message, Snackbar.LENGTH_LONG);
                    bar.show();
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
                        //Snackbar bar = Snackbar.make(coordinatorLayout, R.string.failure_message, Snackbar.LENGTH_LONG);
                        //bar.show();
                    }
                });
            }
        } catch (Exception e){
            Log.d("Error", e.getMessage());
            //Snackbar bar = Snackbar.make(coordinatorLayout, e.toString(), Snackbar.LENGTH_LONG);
            //bar.show();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
