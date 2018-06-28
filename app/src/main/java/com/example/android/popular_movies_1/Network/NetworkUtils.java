package com.example.android.popular_movies_1.Network;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String KEY_MOVIEDB = "47cd3048b1243fdf03812df8a6e2fc4f";

    private static final String MOVIEDB_URL = "http://api.themoviedb.org/3/movie";


    /* The format we want our API to return */
    private static final String format = "json";
    /* The units we want our API to return */
    private static final String units = "metric";

    final static String QUERY_PARAM = "api_key";
    final static String SORT_BY_PARAM = "sort_by";
    final static String FORMAT_PARAM = "mode";
    final static String UNITS_PARAM = "units";


    public static URL buildUrl(String sort) {

        Uri builtUri = Uri.parse(MOVIEDB_URL + "/" + sort).buildUpon()
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

}
