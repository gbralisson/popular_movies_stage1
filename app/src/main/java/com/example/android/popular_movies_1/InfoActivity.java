package com.example.android.popular_movies_1;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popular_movies_1.Adapter.MovieAdapter;
import com.example.android.popular_movies_1.Adapter.VideoAdapter;
import com.example.android.popular_movies_1.Loaders.LoaderReviews;
import com.example.android.popular_movies_1.Loaders.LoaderVideos;
import com.example.android.popular_movies_1.Model.Movie;
import com.example.android.popular_movies_1.Model.Review;
import com.example.android.popular_movies_1.Model.Video;
import com.example.android.popular_movies_1.Network.NetworkUtils;
import com.example.android.popular_movies_1.Utils.ParseJsonFromMovieDB;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class InfoActivity extends AppCompatActivity implements VideoAdapter.VideoAdapterOnClickHandler{

    private TextView txtInfoTitle;
    private TextView txtInfoOverview;
    private ImageView imgInfoPoster;
    private TextView txtInfoRate;
    private TextView txtInfoRelease;
    private Movie movie;

    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;

    private String KEY_INTENT = "movie_data";
    private static final String SEARCH_VIDEO = "queryVideo";
    private static final String SEARCH_REVIEW = "queryReview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        txtInfoTitle = findViewById(R.id.txt_info_title);
        txtInfoOverview = findViewById(R.id.txt_info_overview);
        imgInfoPoster = findViewById(R.id.img_info_poster);
        txtInfoRate = findViewById(R.id.txt_info_rate);
        txtInfoRelease = findViewById(R.id.txt_info_release);
        recyclerView = findViewById(R.id.rv_videos);

        // Verify if there is any data from another activity
        if (getIntent() != null){
            if(getIntent().hasExtra(KEY_INTENT)){
                movie = (Movie) getIntent().getSerializableExtra(KEY_INTENT);

                txtInfoTitle.setText(movie.getTitle());
                txtInfoOverview.setText(movie.getOverview());
                MovieAdapter.loadImageMovie(this, movie.getPoster_path(), imgInfoPoster);
                txtInfoRate.setText(String.valueOf(movie.getVote_average()));
                txtInfoRelease.setText(movie.getRelease_data());

            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        videoAdapter = new VideoAdapter(getApplicationContext(), this);
        recyclerView.setAdapter(videoAdapter);
        recyclerView.setHasFixedSize(true);

        createLoaderVideos();
        createLoaderReviews();

    }

    private URL makeUrlQuery(String sort){
        int id = movie.getId();

        return NetworkUtils.buildUrlMovieReviews(String.valueOf(id), sort);
    }

    private void createLoaderVideos(){
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_VIDEO, makeUrlQuery(getString(R.string.videos)).toString());

        new LoaderVideos(getApplicationContext(), getSupportLoaderManager(), bundle, videoAdapter);
    }

    private void createLoaderReviews(){
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_REVIEW, makeUrlQuery(getString(R.string.reviews)).toString());

        new LoaderReviews(getApplicationContext(), getSupportLoaderManager(), bundle, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_info){
            Log.d("teste", "favoritou");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Video video) {
        Log.d("teste", "clicou");
    }
}
