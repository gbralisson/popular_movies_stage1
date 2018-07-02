package com.example.android.popular_movies_1.Utils;

import com.example.android.popular_movies_1.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseJsonFromMovieDB {

    public static Movie[] getJson(String json) throws JSONException {

        String PAGE = "page";
        String TOTAL_RESULTS = "total_results";
        String TOTAL_PAGES = "total_pages";
        String RESULTS = "results";

        // Creating a JSON object according json
        JSONObject root = new JSONObject(json);

        Movie[] movies;

        int page = root.getInt(PAGE);
        int total_results = root.getInt(TOTAL_RESULTS);
        int total_pages = root.getInt(TOTAL_PAGES);

        JSONArray results = root.getJSONArray(RESULTS);

        movies = new Movie[results.length()];

        for (int i=0; i<movies.length; i++){

            JSONObject resultMovie = results.getJSONObject(i);

            movies[i] = new Movie();

            // Get data of each attribute
            String overview = resultMovie.getString(Attributes.OVERVIEW);
            int id = resultMovie.getInt(Attributes.ID);
            String title = resultMovie.getString(Attributes.TITLE);
            int vote_count = resultMovie.getInt(Attributes.VOTE_COUNT);
            boolean video = resultMovie.getBoolean(Attributes.VIDEO);
            double vote_average = resultMovie.getDouble(Attributes.VOTE_AVERAGE);
            double populatiry = resultMovie.getDouble(Attributes.POPULARITY);
            String poster_path = resultMovie.getString(Attributes.POSTER_PATH);
            String original_language = resultMovie.getString(Attributes.ORIGINAL_LANGUAGE);
            String original_title = resultMovie.getString(Attributes.ORIGINAL_TITLE);
            String backdrop_path = resultMovie.getString(Attributes.BACKDROP_PATH);
            boolean adult = resultMovie.getBoolean(Attributes.ADULT);
            String release_data = resultMovie.getString(Attributes.RELEASE_DATE);

            // Set the retreived data into each movie instance
            movies[i].setVote_count(vote_count);
            movies[i].setId(id);
            movies[i].setVideo(video);
            movies[i].setVote_average(vote_average);
            movies[i].setTitle(title);
            movies[i].setPopulatrity(populatiry);
            movies[i].setPoster_path(poster_path);
            movies[i].setOriginal_language(original_language);
            movies[i].setOriginal_title(original_title);
            movies[i].setBackdrop_path(backdrop_path);
            movies[i].setAdult(adult);
            movies[i].setOverview(overview);
            movies[i].setRelease_data(release_data);

        }

        return movies;

    }

}
