package com.example.android.popular_movies_1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popular_movies_1.Model.Movie;
import com.squareup.picasso.Picasso;

public class InfoActivity extends AppCompatActivity {

    private TextView txtInfoTitle;
    private TextView txtInfoOverview;
    private ImageView imgInfoPoster;
    private TextView txtInfoRate;
    private TextView txtInfoRelease;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        txtInfoTitle = (TextView) findViewById(R.id.txt_info_title);
        txtInfoOverview = (TextView) findViewById(R.id.txt_info_overview);
        imgInfoPoster = (ImageView) findViewById(R.id.img_info_poster);
        txtInfoRate = (TextView) findViewById(R.id.txt_info_rate);
        txtInfoRelease = (TextView) findViewById(R.id.txt_info_release);

        if (getIntent() != null){
            if(getIntent().hasExtra("movie_data")){
                movie = (Movie) getIntent().getSerializableExtra("movie_data");

                txtInfoTitle.setText(movie.getTitle());
                txtInfoOverview.setText(movie.getOverview());
                loadImageMovie(this, movie.getPoster_path(), imgInfoPoster);
                txtInfoRate.setText("" + movie.getVote_average());
                txtInfoRelease.setText(movie.getRelease_data());

            }
        }

    }

    public void loadImageMovie(Context context, String pathImage, ImageView imageView){
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + pathImage).into(imageView);
    }
}