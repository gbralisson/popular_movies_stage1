package com.example.android.popular_movies_1;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popular_movies_1.Adapter.MovieAdapter;
import com.example.android.popular_movies_1.Model.Movie;
import com.example.android.popular_movies_1.Model.Video;
import com.example.android.popular_movies_1.Network.NetworkUtils;
import com.example.android.popular_movies_1.Utils.ParseJsonFromMovieDB;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class InfoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Video[]>{

    private TextView txtInfoTitle;
    private TextView txtInfoOverview;
    private ImageView imgInfoPoster;
    private TextView txtInfoRate;
    private TextView txtInfoRelease;
    private Movie movie;

    private String KEY_INTENT = "movie_data";
    private static final String SEARCH_MOVIE_REVIEW = "query";
    private static final int LOADER_ID = 01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        txtInfoTitle = findViewById(R.id.txt_info_title);
        txtInfoOverview = findViewById(R.id.txt_info_overview);
        imgInfoPoster = findViewById(R.id.img_info_poster);
        txtInfoRate = findViewById(R.id.txt_info_rate);
        txtInfoRelease = findViewById(R.id.txt_info_release);

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

        makeUrlQuery(getString(R.string.videos));

    }

    private void makeUrlQuery(String sort){
        int id = movie.getId();

        URL videos_URL = NetworkUtils.buildUrlMovieReviews(String.valueOf(id), sort);

        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_MOVIE_REVIEW, videos_URL.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Video[]> videoLoader = loaderManager.getLoader(LOADER_ID);

        if (videoLoader == null)
            loaderManager.initLoader(LOADER_ID, bundle, this);
        else
            loaderManager.restartLoader(LOADER_ID, bundle, this);

    }


    @Override
    public Loader<Video[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Video[]>(this) {

            Video[] videoJson;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (args == null)
                    return;

                if (videoJson != null)
                    deliverResult(videoJson);
                else
                    forceLoad();
            }

            @Override
            public Video[] loadInBackground() {

                String Url = args.getString(SEARCH_MOVIE_REVIEW);

                try {
                    URL movieUrl = new URL(Url);
                    String response = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                    return ParseJsonFromMovieDB.getJsonVideos(response);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return null;
            }

            public void deliverResult(Video[] videos){
                videoJson = videos;
                super.deliverResult(videos);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Video[]> loader, Video[] data) {
        Log.d("teste", data[0].getId() + " " + data[0].getSite() + " " + data[0].getType());
    }

    @Override
    public void onLoaderReset(Loader<Video[]> loader) {

    }
}
