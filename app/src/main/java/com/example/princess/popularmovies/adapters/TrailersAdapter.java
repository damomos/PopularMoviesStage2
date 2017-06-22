package com.example.princess.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.princess.popularmovies.R;
import com.example.princess.popularmovies.models.Trailers;
import com.squareup.picasso.Picasso;

import java.util.List;

import static java.lang.System.load;

/**
 * Created by Princess on 6/15/2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder>{

    private Context context;
    private List<Trailers> trailersList;
    String thumbnailUrl;

    public TrailersAdapter(Context context, List<Trailers> trailersList){
        this.context = context;
        this.trailersList = trailersList;
    }

    public class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mTrailer;
        ImageView mIcon;
        FrameLayout trailersLayout;

        public TrailersViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            trailersLayout = (FrameLayout) itemView.findViewById(R.id.trailers_layout);
            mTrailer = (ImageView) itemView.findViewById(R.id.trailer_thumbnail);
            mIcon = (ImageView) itemView.findViewById(R.id.play_icon);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION){
                Trailers clickedDataItem = trailersList.get(position);
                String videoId = trailersList.get(position).getKey();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+videoId));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("VIDEO_ID", videoId);
                context.startActivity(intent);
            }
        }
    }

    @Override
    public TrailersAdapter.TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailers_list, parent, false);
        return new TrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersAdapter.TrailersViewHolder holder, int position) {

        Trailers video = trailersList.get(position);
        holder.mIcon.setImageResource(R.drawable.ic_play_circle_outline_black_32dp);
        thumbnailUrl = "http://img.youtube.com/vi/" + video.getKey() + "/0.jpg";
        Picasso.with(context)
                .load(thumbnailUrl)
                .into(holder.mTrailer);
    }

    @Override
    public int getItemCount() {
        if(trailersList == null)
            return 0;
        return trailersList.size();
    }


}
