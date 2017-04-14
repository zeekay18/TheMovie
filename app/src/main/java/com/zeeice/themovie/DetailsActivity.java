package com.zeeice.themovie;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zeeice.themovie.Model.Movie;

import org.w3c.dom.Text;

public class DetailsActivity extends AppCompatActivity {

    Movie movie;

    ImageView imageView;
    TextView synopsisView;
    TextView userRatingView;
    TextView releaseDateView;
    TextView titleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imageView = (ImageView)findViewById(R.id.movieImage);
        synopsisView = (TextView)findViewById(R.id.synopsis);
        userRatingView = (TextView)findViewById(R.id.userRating);
        releaseDateView = (TextView)findViewById(R.id.releaseDate);
        titleView = (TextView)findViewById(R.id.movieTitle);

        Intent intent = getIntent();
        movie = intent.getParcelableExtra("movies");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Movie Details");

        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w185//"+movie.getImageUrl())
                .placeholder(R.color.colorPrimary)
                .into(imageView);

        titleView.setText(movie.getTitle());
        synopsisView.setText(movie.getSynopsis());
        userRatingView.setText(movie.getRating());
        releaseDateView.setText(movie.getReleaseDate());
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
