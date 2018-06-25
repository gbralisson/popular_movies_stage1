package com.example.android.popular_movies_1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popular_movies_1.Model.Movie;
import com.example.android.popular_movies_1.Network.NetworkUtils;
import com.example.android.popular_movies_1.Utils.ParseJsonFromMovieDB;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;

    Movie[] movies;

    private final String KEY = "47cd3048b1243fdf03812df8a6e2fc4f";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //imageView = (ImageView) findViewById(R.id.img_test);
        textView = (TextView) findViewById(R.id.txt_Json);

        //Picasso.with(this).load("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg").into(imageView);

        loadDataMovie();

    }

    public void loadDataMovie(){
        new FetchMovieDB().execute("");
    }

    public class FetchMovieDB extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected Movie[] doInBackground(String... params) {

            if (params.length == 0)
                return null;

            String id = params[0];
            Movie[] movies = null;

            URL movie_url = NetworkUtils.buildUrl("popularity.asc");
            Log.d("project", movie_url.toString());

            try {

                String responseURL = NetworkUtils.getResponseFromHttpUrl(movie_url);
                movies = ParseJsonFromMovieDB.getJson(responseURL);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return movies;

        }

        @Override
        protected void onPostExecute(Movie[] movieData) {
            Log.d("project", "Entrou");

            if (movieData != null) {

                Log.d("project", "Entrou1");
                for (int i=0; i<movieData.length; i++) {
                    movies[i] = movieData[i];
                    Log.d("project", movies[i].getOriginal_title());
                }
            }

            Log.d("project", "saiu");
        }

    }
}
