package com.example.android.popular_movies_1;

import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popular_movies_1.Adapter.MovieAdapter;
import com.example.android.popular_movies_1.Database.AppDatabase;
import com.example.android.popular_movies_1.Model.Movie;
import com.example.android.popular_movies_1.Network.NetworkUtils;
import com.example.android.popular_movies_1.Utils.ParseJsonFromMovieDB;
import com.example.android.popular_movies_1.ViewModel.FavoriteViewModel;
import com.example.android.popular_movies_1.ViewModel.PopularViewModel;
import com.example.android.popular_movies_1.ViewModel.TopRatedViewModel;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<Movie[]>{

    private ProgressBar progressBar;
    private TextView txt_loading;
    private TextView txt_favorite_null;

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    private String sort_state;
    private Parcelable recycler_state;

    private final String SAVE_LIST_STATE = "instance_list";
    private static final String SEARCH_MOVIE = "queryMovie";
    private static final int LOADER_ID_MOVIE = 01;

    private AppDatabase databasePopular;
    private AppDatabase databaseTopRated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.pb_loading);
        recyclerView = findViewById(R.id.rv_movies);
        txt_loading = findViewById(R.id.txt_loading);
        txt_favorite_null = findViewById(R.id.txt_favorites_null);

        databasePopular = AppDatabase.getsInstancePopular(getApplicationContext());
        databaseTopRated = AppDatabase.getsInstanceToprated(getApplicationContext());

        setupPreferences();

        int NUM_COLUMNS = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUM_COLUMNS, GridLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(gridLayoutManager);

        movieAdapter = new MovieAdapter(getApplicationContext(), this);

        recyclerView.setAdapter(movieAdapter);
        recyclerView.setHasFixedSize(true);

        // Restore the instance saved
        recyclerView.getLayoutManager().onRestoreInstanceState(recycler_state);

        if (sort_state.equals(getString(R.string.pref_options_popular_value)))
            setupViewModelPopular();

        else if (sort_state.equals(getString(R.string.pref_options_top_rated_value)))
            setupViewModelTopRated();

        else
            setupViewModelFavorites();

    }

    // Save the instances of sort type and position of recycler view
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVE_LIST_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    // Restore position data from recycler view
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null)
            recycler_state = savedInstanceState.getParcelable(SAVE_LIST_STATE);
    }

    private void setupPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadSettingOptions(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void loadSettingOptions(SharedPreferences sharedPreferences){
        sort_state = sharedPreferences.getString(getString(R.string.pref_options_key), getString(R.string.pref_options_popular_value));
    }

    private void setupViewModelPopular(){
        PopularViewModel popularViewModel = ViewModelProviders.of(this).get(PopularViewModel.class);
        popularViewModel.getMoviePopular().observe(this, new Observer<Movie[]>() {
            @Override
            public void onChanged(@Nullable Movie[] movies) {
                if (movies.length == 0){
                    loadDataMovie(sort_state);
                }else{
                    movieAdapter.setMovieData(movies);
                }
            }
        });
    }

    private void setupViewModelTopRated(){
        TopRatedViewModel topRatedViewModel = ViewModelProviders.of(this).get(TopRatedViewModel.class);
        topRatedViewModel.getMovieToprated().observe(this, new Observer<Movie[]>() {
            @Override
            public void onChanged(@Nullable Movie[] movies) {
                if (movies.length == 0){
                    loadDataMovie(sort_state);
                }else{
                    movieAdapter.setMovieData(movies);
                }
            }
        });
    }

    private void setupViewModelFavorites(){
        FavoriteViewModel viewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        viewModel.getFavorites().observe(this, new Observer<Movie[]>() {
            @Override
            public void onChanged(@Nullable Movie[] movies) {
                if (movies.length == 0) {
                    txt_favorite_null.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    txt_loading.setVisibility(View.INVISIBLE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    txt_loading.setVisibility(View.INVISIBLE);
                    txt_favorite_null.setVisibility(View.INVISIBLE);
                    movieAdapter.setMovieData(movies);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_options_key)))
            loadSettingOptions(sharedPreferences);
    }

    // Method to fetch data from movieDB
    private void loadDataMovie(String sort){

        if (NetworkUtils.verifyConnection(this)) {
            Bundle bundle = new Bundle();
            bundle.putString(SEARCH_MOVIE, sort);

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<Movie[]> movieLoader = loaderManager.getLoader(LOADER_ID_MOVIE);
            if (movieLoader == null)
                loaderManager.initLoader(LOADER_ID_MOVIE, bundle, this);
            else
                loaderManager.restartLoader(LOADER_ID_MOVIE, bundle, this);

        } else {
            progressBar.setVisibility(View.INVISIBLE);
            txt_loading.setVisibility(View.VISIBLE);
            txt_favorite_null.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }

    }

    // Send the data to Info activity
    @Override
    public void onClick(Movie movie) {
        String KEY_INTENT = "movie_data";
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra(KEY_INTENT, movie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Movie[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Movie[]>(this) {

            Movie[] moviesJson;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (args == null)
                    return;

                recyclerView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                txt_loading.setVisibility(View.INVISIBLE);
                txt_favorite_null.setVisibility(View.INVISIBLE);

                if (moviesJson != null)
                    deliverResult(moviesJson);
                else
                    forceLoad();

            }

            @Nullable
            @Override
            public Movie[] loadInBackground() {

                String sort = args.getString(SEARCH_MOVIE);
                URL movie_url = NetworkUtils.buildUrl(sort);

                try {

                    String responseURL = NetworkUtils.getResponseFromHttpUrl(movie_url);

                    if (responseURL != null){
                        Movie[] movies = ParseJsonFromMovieDB.getJson(responseURL);

                        if (sort.equals(getString(R.string.pref_options_popular_value)))
                            insertDatabase(databasePopular, movies);
                        else
                            insertDatabase(databaseTopRated, movies);

                        return movies;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Movie[]> loader, Movie[] data) {

        if (data != null) {

            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            txt_loading.setVisibility(View.INVISIBLE);
            txt_favorite_null.setVisibility(View.INVISIBLE);
            movieAdapter.setMovieData(data);

        } else {

            progressBar.setVisibility(View.VISIBLE);
            txt_loading.setVisibility(View.INVISIBLE);
            txt_favorite_null.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Movie[]> loader) {

    }

    public void insertDatabase(AppDatabase database, Movie[] movies){
        for (int i=0;i<movies.length;i++){
            database.favoriteDAO().insertFavorite(movies[i]);
        }
    }
}
