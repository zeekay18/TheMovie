package com.zeeice.themovie.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.squareup.picasso.Picasso;
import com.zeeice.themovie.Api.MovieApi;
import com.zeeice.themovie.Data.Model.Movie;
import com.zeeice.themovie.Data.Model.Review;
import com.zeeice.themovie.Data.Model.Video;
import com.zeeice.themovie.Data.repository.MoviesRepository;
import com.zeeice.themovie.R;
import com.zeeice.themovie.Utilities.ServiceGenerator;
import com.zeeice.themovie.ui.Adapter.ReviewsAdapter;
import com.zeeice.themovie.ui.Adapter.VideosAdapter;
import com.zeeice.themovie.ui.Listner.MyRecyclerItemClickListener;
import com.zeeice.themovie.ui.activity.DetailsActivity;
import com.zeeice.themovie.ui.activity.MainActivity;
import com.zeeice.themovie.ui.helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Oriaje on 06/05/2017.
 */

public class MovieDetailsFragment extends Fragment
        implements ObservableScrollViewCallbacks, MyRecyclerItemClickListener.OnItemClickListener,
        View.OnClickListener {

    Movie mMovie;

    List<Review> mReviews;
    List<Video> mVideos;

    ImageView coverImage;
    ImageView posterImage;
    TextView synopsisView;
    TextView userRatingView;
    TextView releaseDateView;
    TextView titleView;

    ImageView favoriteButton;

    RecyclerView reviewsRecyclerView;
    RecyclerView videosRecyclerView;

    ObservableScrollView mScrollView;
    FrameLayout mMovieCoverContainer;

    private Toolbar mToolbar;

    private static final String STATE_VIDEOS = "state_videos";
    private static final String STATE_REVIEWS = "state_reviews";

    private CompositeDisposable mSubscriptions;
    private Unbinder unbinder;

    MoviesRepository mMovieRepository;

    ReviewsAdapter reviewsAdapter;
    VideosAdapter videosAdapter;

    DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        coverImage = (ImageView) view.findViewById(R.id.coverImage);
        posterImage = (ImageView) view.findViewById(R.id.posterImage);
        synopsisView = (TextView) view.findViewById(R.id.synopsis);
        userRatingView = (TextView) view.findViewById(R.id.userRating);
        releaseDateView = (TextView) view.findViewById(R.id.releaseDate);
        titleView = (TextView) view.findViewById(R.id.movieTitle);

        favoriteButton = (ImageView) view.findViewById(R.id.movie_favorite_button);
        favoriteButton.setOnClickListener(this);

        reviewsAdapter = new ReviewsAdapter(getActivity());
        videosAdapter = new VideosAdapter(getActivity());

        reviewsRecyclerView = (RecyclerView) view.findViewById(R.id.review_recycleView);
        videosRecyclerView = (RecyclerView) view.findViewById(R.id.movies_recycleView);

        reviewsRecyclerView.setHasFixedSize(true);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        reviewsRecyclerView.setAdapter(reviewsAdapter);

        videosRecyclerView.setHasFixedSize(true);
        videosRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        videosRecyclerView.setAdapter(videosAdapter);
        videosRecyclerView.addOnItemTouchListener(new MyRecyclerItemClickListener(getActivity(), this));

        mScrollView = (ObservableScrollView) view.findViewById(R.id.movie_details_scrollview);
        mMovieCoverContainer = (FrameLayout) view.findViewById(R.id.coverMovieContainer);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trySetUpToolbar();
        mScrollView.setScrollViewCallbacks(this);

        if (savedInstanceState != null) {
            mVideos = savedInstanceState.getParcelableArrayList(STATE_VIDEOS);
            mReviews = savedInstanceState.getParcelableArrayList(STATE_REVIEWS);
            mScrollView.onRestoreInstanceState(mScrollView.onSaveInstanceState());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mReviews != null)
            outState.putParcelableArrayList(STATE_REVIEWS, new ArrayList<>(mReviews));
        if (mVideos != null)
            outState.putParcelableArrayList(STATE_VIDEOS, new ArrayList<>(mVideos));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        databaseHelper = new DatabaseHelper(getActivity());

        mSubscriptions = new CompositeDisposable();
        mMovieRepository = new MoviesRepository(getActivity(), ServiceGenerator.createService(MovieApi.class));

        onScrollChanged(mScrollView.getScrollY(), false, false);
        onMovieLoaded(getArguments().getParcelable(MainActivity.ARG_MOVIE));

        if (mReviews != null) onReviewsLoaded(mReviews);
        else loadReviews();

        if (mVideos != null) onVideosLoaded(mVideos);
        else loadVideos();

//        mSubscriptions.add(databaseHelper.getFavoredObservable()
//        .filter( favoredEvent -> (mMovie != null) && mMovie.getId() == favoredEvent.id)
//        .subscribe(movie -> {
//            mMovie.setFavored(movie.favored);
//            favoriteButton.setSelected(movie.favored);
//        }));
    }

    private void trySetUpToolbar() {

        if (getActivity() instanceof DetailsActivity) {
            DetailsActivity activity = (DetailsActivity) getActivity();
            mToolbar = activity.getToolbar();
        }
    }

    private void loadReviews() {
        mSubscriptions.add(mMovieRepository.getReviews(mMovie.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reviews -> {
                    onReviewsLoaded(reviews.getItems());
                }, throwable -> {

                    onReviewsLoaded(null);
                }));
    }

    private void onReviewsLoaded(List<Review> reviews) {
        this.mReviews = reviews;

        if (reviews != null) {
            if (!reviews.isEmpty())
                reviewsAdapter.setData(mReviews);
        }

    }

    private void loadVideos() {
        mSubscriptions.add(mMovieRepository.getVideos(mMovie.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(vidoes -> {
                    onVideosLoaded(vidoes.getItems());
                }, throwable -> {
                    onVideosLoaded(null);
                }));
    }

    private void onVideosLoaded(List<Video> videos) {
        this.mVideos = videos;
        if (videos != null) {

            if (!videos.isEmpty()) {
                Video video = videos.get(0);
                mMovieCoverContainer.setTag(video);
                mMovieCoverContainer.setOnClickListener(this);
                videosAdapter.setData(mVideos);
            }
        }
    }

    private void onMovieLoaded(Movie movie) {
        this.mMovie = movie;

        if (mToolbar != null) {
            mToolbar.setTitle(mMovie.getTitle());
        }
        Picasso.with(getActivity())
                .load("http://image.tmdb.org/t/p/w185//" + movie.getImageUrl())
                .placeholder(R.color.colorPrimary)
                .into(coverImage);

        Picasso.with(getActivity())
                .load("http://image.tmdb.org/t/p/w185//" + movie.getBackdropPath())
                .placeholder(R.color.colorPrimary)
                .into(posterImage);

        titleView.setText(movie.getTitle());
        synopsisView.setText(movie.getSynopsis());
        userRatingView.setText(getString(R.string.movie_details_rating, movie.getRating()));
        releaseDateView.setText(getString(R.string.movie_details_release_date, movie.getReleaseDate()));
        favoriteButton.setSelected(movie.isFavored());

    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

        ViewCompat.setTranslationY(mMovieCoverContainer, scrollY / 2);

        if (mToolbar != null) {
            int toolbarColor = getResources().getColor(R.color.colorPrimary);
            int textColor = getResources().getColor(R.color.textPrimary);

            int parallaxImageHeight = mMovieCoverContainer.getMeasuredHeight();
            float alpha = Math.min(1, (float) scrollY / parallaxImageHeight);

            mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, toolbarColor));
            mToolbar.setTitleTextColor(ScrollUtils.getColorWithAlpha(alpha, textColor));
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSubscriptions.clear();
    }

    @Override
    public void onItemClick(View view, int position) {

        if (view.getId() == R.id.movie_item_view) {
            Video video = (Video) view.getTag();

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));

            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == favoriteButton.getId()) {
            if (mMovie == null)
                return;

            boolean favored = !mMovie.isFavored();
            favoriteButton.setSelected(favored);
            mMovie.setFavored(favored);

            databaseHelper.setMovieFavored(mMovie, favored);

            Toast.makeText(getActivity(), favored ? "added to favorite" : "removed from favorite", Toast.LENGTH_SHORT).show();
        }
        if (v.getId() == R.id.coverMovieContainer) {
            Video video = (Video) v.getTag();

            if (video == null)
                return;

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));

            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
}

