package com.example.android.popular_movies_1.Utils;

import android.util.Log;

import com.example.android.popular_movies_1.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicReference;

public class ParseJsonFromMovieDB {

    public static Movie[] getJson(String json) throws JSONException {

        JSONObject root = new JSONObject(json);

        Movie[] movie = null;

        if (root != null) {
            int page = root.getInt("page");
            int total_results = root.getInt("total_results");
            int total_pages = root.getInt("total_pages");

            JSONArray results = root.getJSONArray("results");
            
            movie = new Movie[results.length()];

            int count=0;
            //Log.d("project", String.valueOf(movie.length));
            
            for (int i=0; i<movie.length; i++){

                JSONObject resultMovie = results.getJSONObject(i);

                if(resultMovie != null) {

                    movie[i] = new Movie();

                    movie[i].setVote_count(resultMovie.getInt(Attributes.VOTE_COUNT));
                    movie[i].setId(resultMovie.getInt(Attributes.ID));
                    movie[i].setVideo(resultMovie.getBoolean(Attributes.VIDEO));
                    movie[i].setVote_average(resultMovie.getDouble(Attributes.VOTE_AVERAGE));
                    movie[i].setTitle(resultMovie.getString(Attributes.TITLE));
                    movie[i].setPopulatrity(resultMovie.getDouble(Attributes.POPULARITY));
                    movie[i].setPoster_path(resultMovie.getString(Attributes.POSTER_PATH));
                    movie[i].setOriginal_language(resultMovie.getString(Attributes.ORIGINAL_LANGUAGE));
                    movie[i].setOriginal_title(resultMovie.getString(Attributes.ORIGINAL_TITLE));
                    movie[i].setBackdrop_path(resultMovie.getString(Attributes.BACKDROP_PATH));
                    movie[i].setAdult(resultMovie.getBoolean(Attributes.ADULT));
                    movie[i].setOverview(resultMovie.getString(Attributes.OVERVIEW));
                    movie[i].setRelease_data(resultMovie.getString(Attributes.RELEASE_DATE));

                    Log.d("project", String.valueOf(movie[i].getVote_count()));
                    Log.d("project", String.valueOf(movie[i].getId()));
                }
                
            }
            
        }

        return movie;
    }

}
