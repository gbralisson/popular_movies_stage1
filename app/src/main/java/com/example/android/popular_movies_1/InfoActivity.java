package com.example.android.popular_movies_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popular_movies_1.Adapter.MovieAdapter;
import com.example.android.popular_movies_1.Model.Movie;

public class InfoActivity extends AppCompatActivity {

    private TextView txtInfoTitle;
    private TextView txtInfoOverview;
    private ImageView imgInfoPoster;
    private TextView txtInfoRate;
    private TextView txtInfoRelease;
    private Movie movie;

    private String KEY_INTENT = "movie_data";

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

    }
}
