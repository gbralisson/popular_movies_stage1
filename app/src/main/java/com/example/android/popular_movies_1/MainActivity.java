package com.example.android.popular_movies_1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popular_movies_1.Adapter.MovieAdapter;
import com.example.android.popular_movies_1.Model.Movie;
import com.example.android.popular_movies_1.Network.NetworkUtils;
import com.example.android.popular_movies_1.Utils.ParseJsonFromMovieDB;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{

    private ProgressBar progressBar;
    private TextView txt_loading;

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    private String sort_state;
    private Parcelable recycler_state;

    private final String SAVE_SORT_STATE = "instance_sort";
    private final String SAVE_LIST_STATE = "instance_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.pb_loading);
        recyclerView = findViewById(R.id.rv_movies);
        txt_loading = findViewById(R.id.txt_loading);

        // Check if there is an instance saved
        if (savedInstanceState != null) {
            sort_state = savedInstanceState.getString(SAVE_SORT_STATE);
        }else {
            sort_state = getString(R.string.popular);
        }

        int NUM_COLUMNS = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUM_COLUMNS, GridLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(gridLayoutManager);

        movieAdapter = new MovieAdapter(getApplicationContext(), this);

        recyclerView.setAdapter(movieAdapter);
        recyclerView.setHasFixedSize(true);

        // Restore the instance saved
        recyclerView.getLayoutManager().onRestoreInstanceState(recycler_state);

        loadDataMovie(sort_state);

    }

    // Save the instances of sort type and position of recycler view
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SAVE_SORT_STATE, sort_state);
        outState.putParcelable(SAVE_LIST_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    // Restore position data from recycler view
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null)
            recycler_state = savedInstanceState.getParcelable(SAVE_LIST_STATE);
    }

    // Method to fetch data from movieDB
    private void loadDataMovie(String sort){
        new FetchMovieDB().execute(sort);
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

        if(id == R.id.menu_order_popularity){
            movieAdapter.setMovieData(null);
            sort_state = getString(R.string.popular);
            loadDataMovie(sort_state);
            return true;
        }

        if(id == R.id.menu_order_rate){
            movieAdapter.setMovieData(null);
            sort_state = getString(R.string.top_rated);
            loadDataMovie(sort_state);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class FetchMovieDB extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            txt_loading.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {

            if (params.length == 0)
                return null;

            String sort = params[0];
            URL movie_url = NetworkUtils.buildUrl(sort);

            try {

                String responseURL = NetworkUtils.getResponseFromHttpUrl(movie_url);

                if (responseURL != null){
                    return ParseJsonFromMovieDB.getJson(responseURL);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Movie[] movieData) {

            if (movieData != null) {

                progressBar.setVisibility(View.INVISIBLE);
                txt_loading.setVisibility(View.INVISIBLE);
                movieAdapter.setMovieData(movieData);

            } else {

                progressBar.setVisibility(View.INVISIBLE);
                txt_loading.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);

            }

        }

    }
}
