package com.example.android.popular_movies_1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popular_movies_1.Adapter.MovieAdapter;
import com.example.android.popular_movies_1.Model.Movie;
import com.example.android.popular_movies_1.Network.NetworkUtils;
import com.example.android.popular_movies_1.Utils.ParseJsonFromMovieDB;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{

    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    private String sort_state;
    private Parcelable recycler_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.pb_loading);
        recyclerView = findViewById(R.id.rv_movies);

        if (savedInstanceState != null) {
            sort_state = savedInstanceState.getString("instance_sort");
//            recycler_state = savedInstanceState.getParcelable("instance_list");

        }else {
            sort_state = "popular";
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
//
        movieAdapter = new MovieAdapter(getApplicationContext(), this);
//
        recyclerView.setAdapter(movieAdapter);
        recyclerView.getLayoutManager().onRestoreInstanceState(recycler_state);

        loadDataMovie(sort_state);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("instance_sort", sort_state);
//        outState.putParcelable("instance_list", recyclerView.getLayoutManager().onSaveInstanceState());
    }

    public void loadDataMovie(String sort){
        new FetchMovieDB().execute(sort);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("movie_data", movie);
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
            sort_state = "popular";
            loadDataMovie(sort_state);
            return true;
        }

        if(id == R.id.menu_order_rate){
            movieAdapter.setMovieData(null);
            sort_state = "top_rated";
            loadDataMovie(sort_state);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchMovieDB extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {

            if (params.length == 0)
                return null;

            String sort = params[0];
            Movie[] movies = null;

            URL movie_url = NetworkUtils.buildUrl(sort);
            Log.d("project", movie_url.toString());

            try {

                String responseURL = NetworkUtils.getResponseFromHttpUrl(movie_url);
                movies = ParseJsonFromMovieDB.getJson(responseURL);

                return movies;

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
                movieAdapter.setMovieData(movieData);
                progressBar.setVisibility(View.INVISIBLE);
            }

        }

    }
}
