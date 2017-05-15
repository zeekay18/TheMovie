package com.zeeice.themovie.Utilities;

import com.zeeice.themovie.Data.Model.Movie;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Oriaje on 3/7/2017.
 */
public interface MyHttpClient {

    @GET("/3/movie/popular")
    Observable<Movie.SearchResult> getPopularMovies(@Query(value = "api_key") String query);

    @GET("/3/movie/top_rated")
    Observable<Movie.SearchResult> getTopRatedMovies(@Query(value = "api_key") String query);
}
