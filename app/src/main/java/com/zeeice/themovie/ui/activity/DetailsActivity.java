package com.zeeice.themovie.ui.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.zeeice.themovie.Data.Model.Movie;
import com.zeeice.themovie.R;
import com.zeeice.themovie.ui.fragment.MovieDetailsFragment;

public class DetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            if (getIntent().hasExtra(MainActivity.ARG_MOVIE)) {

                Movie movie = getIntent().getParcelableExtra(MainActivity.ARG_MOVIE);

                Bundle arguments = new Bundle();
                arguments.putParcelable(MainActivity.ARG_MOVIE, movie);

                MovieDetailsFragment fragment = new MovieDetailsFragment();
                fragment.setArguments(arguments);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_details_container, fragment).commit();
            }
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
    }
}
