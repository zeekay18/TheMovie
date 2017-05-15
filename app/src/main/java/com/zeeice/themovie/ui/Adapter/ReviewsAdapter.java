package com.zeeice.themovie.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeeice.themovie.Data.Model.Review;
import com.zeeice.themovie.R;

import java.util.List;

/**
 * Created by Oriaje on 12/05/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {


    private Context mContext;
    private List<Review> mREviews;

    public ReviewsAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.review_item_layout, parent, false);

        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        Review review = mREviews.get(position);

        holder.authorName.setText(review.getAuthor());
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (mREviews == null)
            return 0;

        return mREviews.size();
    }

    public void setData(List<Review> reviews) {
        if (reviews == null)
            return;

        mREviews = reviews;

        notifyDataSetChanged();
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder {

        public TextView authorName;
        public TextView content;

        public ReviewsViewHolder(View itemView) {
            super(itemView);

            authorName = (TextView) itemView.findViewById(R.id.review_author);
            content = (TextView) itemView.findViewById(R.id.review_content);
        }
    }
}
