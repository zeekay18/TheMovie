package com.zeeice.themovie.ui.helper;

import android.app.Activity;
import android.content.ContentValues;

import com.zeeice.themovie.Data.Model.Movie;
import com.zeeice.themovie.Data.Provider.FavoriteColumns;
import com.zeeice.themovie.Data.Provider.FavoriteProvider;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Oriaje on 13/05/2017.
 */

public class DatabaseHelper {


    private static PublishSubject<FavoredEvent> publishSubject = PublishSubject.create();

    private Activity mActivity;

    public DatabaseHelper(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public Observable<FavoredEvent> getFavoredObservable() {
        return publishSubject.subscribeOn(Schedulers.io());
    }

    public void setMovieFavored(Movie movie, boolean favored) {
        if (favored) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FavoriteColumns._ID, movie.getId());
            contentValues.put(FavoriteColumns.TITLE, movie.getTitle());
            contentValues.put(FavoriteColumns.OVERVIEW, movie.getSynopsis());
            contentValues.put(FavoriteColumns.BACK_DROP_PATH, movie.getBackdropPath());
            contentValues.put(FavoriteColumns.POSTER_PATH, movie.getImageUrl());
            contentValues.put(FavoriteColumns.RELEASE_DATE, movie.getReleaseDate());
            contentValues.put(FavoriteColumns.VOTE_AVERAGE, movie.getRating());
            contentValues.put(FavoriteColumns.IS_FAVORED, movie.isFavored() ? 1 : 0);

            mActivity.getContentResolver().insert(FavoriteProvider.Favorites.CONTENT_URI, contentValues);
        } else {
            mActivity.getContentResolver().delete(FavoriteProvider.Favorites.withId(movie.getId()), null, null);
        }

        publishSubject.onNext(new FavoredEvent(movie.getId(), favored));
    }

    public static class FavoredEvent {
        public long id;
        public boolean favored;

        private FavoredEvent(long movieId, boolean favored) {
            this.id = movieId;
            this.favored = favored;
        }
    }
}
