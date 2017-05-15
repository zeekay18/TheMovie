package com.zeeice.themovie.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zeeice.themovie.Data.Model.Movie;
import com.zeeice.themovie.R;

import java.util.List;

/**
 * Created by Oriaje on 12/04/2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>{


    private Context mContext;
    private List<Movie> mMovies;

    public MovieListAdapter(Context mContext)
    {
        this.mContext = mContext;

    }

    @Override
    public MovieListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_list_item,parent,false);

        return new MovieListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieListViewHolder holder, int position) {
        Movie movie = mMovies.get(position);

        Picasso.with(mContext)
                .load("http://image.tmdb.org/t/p/w185//"+movie.getImageUrl())
                .placeholder(R.color.colorPrimary)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (this.mMovies == null)
            return 0;
        return this.mMovies.size();
    }

    public void swapDataset(List<Movie> movies)
    {
        if(movies == null)
            return;
        this.mMovies = movies;

        this.notifyDataSetChanged();
    }
    class MovieListViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MovieListViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.movieImage);
        }
    }
}
