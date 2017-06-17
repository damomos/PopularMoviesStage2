package com.example.princess.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.princess.popularmovies.R;
import com.example.princess.popularmovies.models.Reviews;

import java.util.List;

/**
 * Created by Princess on 6/15/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private Context mContext;
    private List<Reviews> reviewsList;

    public ReviewsAdapter(Context mContext, List<Reviews> reviewsList){
        this.mContext = mContext;
        this.reviewsList = reviewsList;
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {

        //public Reviews reviews;
        TextView mAuthor;
        TextView mContent;
        LinearLayout reviewsLayout;

        public ReviewsViewHolder(View itemView) {
            super(itemView);

            reviewsLayout = (LinearLayout) itemView.findViewById(R.id.reviews_layout);
            mAuthor = (TextView) itemView.findViewById(R.id.review_author);
            mContent = (TextView) itemView.findViewById(R.id.review_content);
        }
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.reviews_list, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        //final Reviews reviews = reviewsList.get(position);

        //holder.reviews = reviews;
        Reviews reviews = reviewsList.get(position);
        holder.mAuthor.setText(reviews.getAuthor());
        holder.mContent.setText(reviews.getContent());
    }

    @Override
    public int getItemCount() {
        if(reviewsList == null)
            return 0;
        return reviewsList.size();
    }


}
