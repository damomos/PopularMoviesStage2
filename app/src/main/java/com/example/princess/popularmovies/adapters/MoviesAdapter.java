package com.example.princess.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.princess.popularmovies.MovieDetailsActivity;
import com.example.princess.popularmovies.MoviesListActivity;
import com.example.princess.popularmovies.R;
import com.example.princess.popularmovies.models.Movies;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Princess on 6/15/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>{

    private Context mContext;
    private List<Movies> moviesList;

    public MoviesAdapter(Context mContext, List<Movies> moviesList){
        this.mContext = mContext;
        this.moviesList = moviesList;
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView posterImage;
        LinearLayout moviesLayout;

        //Constructor to get widget reference
        public MoviesViewHolder(View itemView) {
            super(itemView);

            moviesLayout = (LinearLayout) itemView.findViewById(R.id.movies_layout);
            posterImage = (ImageView) itemView.findViewById(R.id.poster_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            Movies data = moviesList.get(getLayoutPosition());
            intent.putExtra("data", data);
            context.startActivity(intent);
        }
    }

    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_list, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MoviesViewHolder holder, int position) {

        Movies image = moviesList.get(position);
        String posterPath_url = "http://image.tmdb.org/t/p/w500" + image.getPosterPath();
        // load image into image view using picasso
        Picasso.with(mContext).load(posterPath_url).placeholder(R.mipmap.placeholder).into(holder.posterImage);

    }

    @Override
    public int getItemCount() {
        if(moviesList == null)
            return 0;
        return moviesList.size();
    }

}
