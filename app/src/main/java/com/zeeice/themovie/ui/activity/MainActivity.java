package com.zeeice.themovie.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zeeice.themovie.Data.Model.Movie;
import com.zeeice.themovie.R;
import com.zeeice.themovie.Utilities.PrefUtil;
import com.zeeice.themovie.ui.fragment.MovieDetailsFragment;
import com.zeeice.themovie.ui.fragment.MoviesFragment;

public class MainActivity extends AppCompatActivity implements
        MoviesFragment.Listener {

    private static final String SORT_MODE = "sort_mode";

    public static final String ARG_MOVIE = "arg_movie";

    private boolean mTwoPane;
    private String mMode;
    private final String MOVIES_FRAGMENT_TAG = "fragment_movies";
    private final String MOVIE_DETAILS_FRAGMENT_TAG = "fragment_movie_details";
    private MoviesFragment mMoviesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTwoPane = findViewById(R.id.movie_details_container) != null;

        mMode = (savedInstanceState != null) ?
                savedInstanceState.getString(SORT_MODE, "popular")
                : PrefUtil.getMoviesMode(this);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(mMode);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mMoviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentByTag(MOVIES_FRAGMENT_TAG);

        if (mMoviesFragment == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movies_container, new MoviesFragment(), MOVIES_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onMovieSelected(View view, Movie movie) {
        if (mTwoPane) {
            MovieDetailsFragment fragment = new MovieDetailsFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable(ARG_MOVIE, movie);

            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_container, fragment, MOVIE_DETAILS_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(ARG_MOVIE, movie);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }

    }
}
