package com.zeeice.themovie.Utilities;

import com.zeeice.themovie.Model.Movie;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Oriaje on 3/7/2017.
 */
public interface MyHttpClient {

    @GET("/3/movie/popular")
    Call<Movie.SearchResult> getPopularMovies(@Query(value = "api_key") String query);

    @GET("/3/movie/top_rated")
    Call<Movie.SearchResult> getTopRatedMovies(@Query(value = "api_key")String query);
}
