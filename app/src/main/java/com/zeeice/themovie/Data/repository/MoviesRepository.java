package com.zeeice.themovie.Data.repository;

import android.content.Context;
import android.database.Cursor;

import com.zeeice.themovie.Api.MovieApi;
import com.zeeice.themovie.Data.Model.Movie;
import com.zeeice.themovie.Data.Model.Review;
import com.zeeice.themovie.Data.Model.Video;
import com.zeeice.themovie.Data.Provider.FavoriteColumns;
import com.zeeice.themovie.Data.Provider.FavoriteProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Oriaje on 06/05/2017.
 */

public final class MoviesRepository {


    private final MovieApi mMovieApi;

    private Context mContext;

    public MoviesRepository(Context mContex,MovieApi mMovieApi)
    {
        this.mContext = mContex;
        this.mMovieApi = mMovieApi;
}

    public Observable<List<Movie>> getMovies(String type)
    {
        return mMovieApi.getMovies(type)
                .map(result ->result.getItems())
                .zipWith(getFavoriteIds(),(movies, favoriteIds) -> {

                    for(Movie movie : movies)
                    {
                        if(favoriteIds.contains(movie.getId()))
                            movie.setFavored(true);
                    }

                    return movies;
                })
                .timeout(5, TimeUnit.SECONDS)
                .retry(2)
                .subscribeOn(Schedulers.io());
    }

    public Observable<List<Long>> getFavoriteIds() {
        Cursor cursor = mContext.getContentResolver().query(FavoriteProvider.Favorites.CONTENT_URI,
                new String[]{FavoriteColumns._ID}, null, null, null);

        List<Long> ids = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {

                ids.add(cursor.getLong(cursor.getColumnIndex(FavoriteColumns._ID)));
            }
            cursor.close();
        }

        return Observable.fromArray(ids);
    }
    public Observable<List<Movie>> getFavorites()
    {
        Cursor cursor = mContext.getContentResolver().query(FavoriteProvider.Favorites.CONTENT_URI,
                null,null,null ,null);

        List<Movie> movies = new ArrayList<>();

        if(cursor != null)
        {
            while (cursor.moveToNext())
            {
                movies.add( new Movie(
                        cursor.getLong(cursor.getColumnIndex(FavoriteColumns._ID)),
                                cursor.getString(cursor.getColumnIndex(FavoriteColumns.POSTER_PATH)),
                                cursor.getString(cursor.getColumnIndex(FavoriteColumns.BACK_DROP_PATH)),
                                cursor.getString(cursor.getColumnIndex(FavoriteColumns.TITLE)),
                                cursor.getString(cursor.getColumnIndex(FavoriteColumns.OVERVIEW)),
                                cursor.getString(cursor.getColumnIndex(FavoriteColumns.VOTE_AVERAGE)),
                                cursor.getString(cursor.getColumnIndex(FavoriteColumns.RELEASE_DATE)),
                                cursor.getInt(cursor.getColumnIndex(FavoriteColumns.IS_FAVORED))));
            }
            cursor.close();
        }

        return Observable.fromArray(movies);
    }

    public Observable<Review.SearchResult> getReviews(long id)
    {
        return mMovieApi.getReviews(id)
                .timeout(5,TimeUnit.SECONDS)
                .retry(2)
                .subscribeOn(Schedulers.io());
    }

    public Observable<Video.SearchResult> getVideos (long id)
    {
        return mMovieApi.getVideos(id)
                .timeout(5,TimeUnit.SECONDS)
                .retry(2)
                .subscribeOn(Schedulers.io());
    }
}
