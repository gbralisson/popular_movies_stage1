package com.example.android.popular_movies_1.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String KEY_MOVIEDB = "";

    // URL that get data from movieDB
    private static final String MOVIEDB_URL = "http://api.themoviedb.org/3/movie";


    // The format we want our API to return
    private static final String format = "json";
    // The units we want our API to return
    private static final String units = "metric";

    private final static String QUERY_PARAM = "api_key";
    private final static String FORMAT_PARAM = "mode";
    private final static String UNITS_PARAM = "units";

    // Build the URL
    public static URL buildUrl(String sort) {

        Uri builtUri = Uri.parse(MOVIEDB_URL).buildUpon()
                .appendPath(sort)
                .appendQueryParameter(QUERY_PARAM, KEY_MOVIEDB)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlMovieReviews(String id, String type){
        Uri builtUri = Uri.parse(MOVIEDB_URL).buildUpon()
                .appendPath(id)
                .appendPath(type)
                .appendQueryParameter(QUERY_PARAM, KEY_MOVIEDB)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    // Get response from URL built
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean verifyConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null){
                return networkInfo.isConnected();
            }
        }

        return false;
    }

}
