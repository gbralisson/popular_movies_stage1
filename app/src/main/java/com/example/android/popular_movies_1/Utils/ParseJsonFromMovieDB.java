package com.example.android.popular_movies_1.Utils;

import com.example.android.popular_movies_1.Model.Movie;
import com.example.android.popular_movies_1.Model.Review;
import com.example.android.popular_movies_1.Model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseJsonFromMovieDB {

    public static String RESULTS = "results";

    public static Movie[] getJson(String json) throws JSONException {

        String PAGE = "page";
        String TOTAL_RESULTS = "total_results";
        String TOTAL_PAGES = "total_pages";

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
            double popularity = resultMovie.getDouble(Attributes.POPULARITY);
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
            movies[i].setPopularity(popularity);
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

    public static Video[] getJsonVideos(String json) throws JSONException {

        JSONObject root = new JSONObject(json);
        JSONArray results = root.getJSONArray(RESULTS);

        Video[] videos = new Video[results.length()];

        for (int i=0; i<results.length(); i++){
            JSONObject resultVideo = results.getJSONObject(i);

            videos[i] = new Video();

            String id = resultVideo.getString(Attributes.ID);
            String key = resultVideo.getString(Attributes.KEY);
            String name = resultVideo.getString(Attributes.NAME);
            String site = resultVideo.getString(Attributes.SITE);
            int size = resultVideo.getInt(Attributes.SIZE);
            String type = resultVideo.getString(Attributes.TYPE);

            videos[i].setId(id);
            videos[i].setKey(key);
            videos[i].setName(name);
            videos[i].setSite(site);
            videos[i].setSize(size);
            videos[i].setType(type);
        }

        return videos;
    }

    public static Review[] getJsonReviews(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONArray results = root.getJSONArray(RESULTS);

        Review[] reviews = new Review[results.length()];

        for (int i=0; i<results.length(); i++){
            JSONObject resultReview = results.getJSONObject(i);

            reviews[i] = new Review();

            String id = resultReview.getString(Attributes.ID);
            String author = resultReview.getString(Attributes.AUTHOR);
            String content = resultReview.getString(Attributes.CONTENT);
            String url = resultReview.getString(Attributes.URL);

            reviews[i].setId(id);
            reviews[i].setAuthor(author);
            reviews[i].setContent(content);
            reviews[i].setUrl(url);
        }

        return reviews;
    }

}
