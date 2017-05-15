package com.zeeice.themovie.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zeeice.themovie.Api.MovieApi;
import com.zeeice.themovie.Data.Model.Movie;
import com.zeeice.themovie.Data.repository.MoviesRepository;
import com.zeeice.themovie.R;
import com.zeeice.themovie.Utilities.PrefUtil;
import com.zeeice.themovie.Utilities.ServiceGenerator;
import com.zeeice.themovie.Utilities.Sort;
import com.zeeice.themovie.ui.Adapter.MovieListAdapter;
import com.zeeice.themovie.ui.Listner.MyRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Oriaje on 05/05/2017.
 */

public class MoviesFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, MyRecyclerItemClickListener.OnItemClickListener {

    //@BindView(R.id.movie_list)
    RecyclerView mMoviesRecyclerView;

    SwipeRefreshLayout swipeRefreshLayout;

    private CompositeDisposable mSubscriptions;

    MoviesRepository mMovieRepository;

    MovieListAdapter movieListAdapter;
    List<Movie> movies;

    private static final String STATE_MOVIES = "state_movies";

    private Unbinder unbinder;

    private String mMode;

    public interface Listener {
        void onMovieSelected(View view, Movie movie);
    }

    private Listener listener;

    @Override
    public void onItemClick(View view, int position) {

        listener.onMovieSelected(view, movies.get(position));
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        setHasOptionsMenu(true);

        if (!(getActivity() instanceof Listener)) {
            throw new IllegalStateException("Activity must implement listener.");
        }
        listener = (Listener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        //    unbinder = ButterKnife.bind(this,view);

        mMoviesRecyclerView = (RecyclerView) view.findViewById(R.id.movie_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSubscriptions = new CompositeDisposable();
        mMovieRepository = new MoviesRepository(getActivity(), ServiceGenerator.createService(MovieApi.class));

        movies = (savedInstanceState != null)
                ? savedInstanceState.getParcelableArrayList(STATE_MOVIES)
                : new ArrayList<>();

        movieListAdapter = new MovieListAdapter(getActivity());

        initRecyclerView();
        initSwipeRefreshLayout();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMode = PrefUtil.getMoviesMode(getActivity());

        //If activity was not previously created
        if (savedInstanceState == null) {
            loadContent();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(STATE_MOVIES, new ArrayList<>(movies));
    }

    @Override
    public void onRefresh() {
        loadContent();
    }


    private void loadContent() {
        switch (mMode) {
            case Sort.MOVIES_POPULAR:
                subscribeToMovies(mMode);
                break;
            case Sort.MOVIES_TOP_RATED:
                subscribeToMovies(mMode);
                break;
            case Sort.MOVIES_FAVORITE:
                subscribeToFavorites();
                break;
            default:
                throw new IllegalArgumentException("Unknown sort type given");
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_sort_popular:
                setSortMode(Sort.MOVIES_POPULAR);
                break;
            case R.id.action_sort_toprated:
                setSortMode(Sort.MOVIES_TOP_RATED);
                break;
            case R.id.action_sort_favorite:
                setSortMode(Sort.MOVIES_FAVORITE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        onRefresh();
        return true;
    }

    private void subscribeToFavorites() {
        swipeRefreshLayout.setRefreshing(true);
        mSubscriptions.add(mMovieRepository.getFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    this.movies = result;
                    swipeRefreshLayout.setRefreshing(false);

                    movieListAdapter.swapDataset(movies);

                }, throwable -> {

                    swipeRefreshLayout.setRefreshing(false);

                    Toast.makeText(getActivity(), R.string.error_message, Toast.LENGTH_SHORT).show();

                }));
    }

    private void subscribeToMovies(String type) {
        swipeRefreshLayout.setRefreshing(true);
        mSubscriptions.add(mMovieRepository.getMovies(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {

                    this.movies = result;
                    swipeRefreshLayout.setRefreshing(false);
                    movieListAdapter.swapDataset(movies);
                }, throwable -> {

                    swipeRefreshLayout.setRefreshing(false);

                    Toast.makeText(getActivity(), R.string.error_message, Toast.LENGTH_SHORT).show();

                }));

    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.movies_column));

        mMoviesRecyclerView.setHasFixedSize(true);
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);

        mMoviesRecyclerView.setAdapter(movieListAdapter);

        mMoviesRecyclerView.addOnItemTouchListener(new MyRecyclerItemClickListener(getActivity(), this));

        movieListAdapter.swapDataset(movies);

    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_progress_colors));
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //  unbinder.unbind();
        mSubscriptions.clear();
        PrefUtil.setMoviesMode(getActivity(), mMode);
    }

    public void setSortMode(String mMode) {
        this.mMode = mMode;

        if (getActivity().getActionBar() != null)
            getActivity().getActionBar().setTitle(this.mMode);
    }

}
