package com.zeeice.themovie;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zeeice.themovie.Adapter.MovieListAdapter;
import com.zeeice.themovie.Model.Movie;
import com.zeeice.themovie.Utilities.MyHttpClient;
import com.zeeice.themovie.Utilities.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
implements MyRecyclerItemClickListener.OnItemClickListener{

    RecyclerView mMoviesRecyclerView;
    MovieListAdapter movieListAdapter;
    ArrayList<Movie>movies;
    ProgressBar progressBar;

    private final String API_KEY = "api_key"; //pass in the api key here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        movies = null;

        movieListAdapter = new MovieListAdapter(this);

        mMoviesRecyclerView = (RecyclerView)findViewById(R.id.movie_list);
        mMoviesRecyclerView.setHasFixedSize(true);
        mMoviesRecyclerView.setLayoutManager(new GridLayoutManager(this,3));

        mMoviesRecyclerView.setAdapter(movieListAdapter);

        mMoviesRecyclerView.addOnItemTouchListener(new MyRecyclerItemClickListener(this,this));

        //get default popular movies
        populateData(1);
    }

    private  void setMovies(ArrayList<Movie>movies)
    {
        this.movies = movies;
        movieListAdapter.swapDataset(movies);
    }

    private void populateData(int option)
    {
        //show the progress bar
        progressBar.setVisibility(View.VISIBLE);

        MyHttpClient httpClient = ServiceGenerator.createService(MyHttpClient.class);

        Call<Movie.SearchResult> call = null;

        if( option == 1)
            call = httpClient.getPopularMovies(API_KEY);
        else if(option == 2)
            call = httpClient.getTopRatedMovies(API_KEY);
        else
            throw new IllegalArgumentException("wrong query type supplied");

        //runs on a background thread and gets the data using the API
        call.enqueue(new Callback<Movie.SearchResult>() {
            @Override
            public void onResponse(Call<Movie.SearchResult> call, Response<Movie.SearchResult> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if(response.isSuccessful()) {
                    setMovies(response.body().getItems());
                }else {
                    Toast.makeText(MainActivity.this, "failed to get movies now", Toast.LENGTH_LONG)
                            .show();
                }
            }
            @Override
            public void onFailure(Call<Movie.SearchResult> call, Throwable t) {
               progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this," failed to get movies now",Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_sort_popular:
                populateData(1);
                break;
            case R.id.action_sort_toprated:
                populateData(2);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this,DetailsActivity.class);
        intent.putExtra("movies",movies.get(position));
        startActivity(intent);
        overridePendingTransition(R.anim.slide__in_right, R.anim.slide_out_right);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

}
