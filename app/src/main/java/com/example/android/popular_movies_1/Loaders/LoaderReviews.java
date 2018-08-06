package com.example.android.popular_movies_1.Loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.example.android.popular_movies_1.Adapter.ReviewAdapter;
import com.example.android.popular_movies_1.Model.Review;
import com.example.android.popular_movies_1.Model.Video;
import com.example.android.popular_movies_1.Network.NetworkUtils;
import com.example.android.popular_movies_1.Utils.ParseJsonFromMovieDB;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class LoaderReviews implements LoaderManager.LoaderCallbacks<Review[]>, ReviewAdapter.ReviewAdapterOnClickHandler{

    private static final String SEARCH_REVIEW = "queryReview";
    private static final int LOADER_ID_REVIEW = 02;

    private Context context;
    private ReviewAdapter reviewAdapter;

    public LoaderReviews(Context context, LoaderManager loaderManager, Bundle bundle, ReviewAdapter reviewAdapter){
        this.context = context;
        this.reviewAdapter = reviewAdapter;
        verifyLoader(loaderManager, bundle);
    }

    @NonNull
    @Override
    public Loader<Review[]> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<Review[]>(context) {

            Review[] reviewJson;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (args == null)
                    return;

                if (reviewJson != null)
                    deliverResult(reviewJson);
                else
                    forceLoad();

            }

            @Override
            public Review[] loadInBackground() {

                String Url = args.getString(SEARCH_REVIEW);

                try {
                    URL reviewUrl = new URL(Url);
                    String response = NetworkUtils.getResponseFromHttpUrl(reviewUrl);
                    return ParseJsonFromMovieDB.getJsonReviews(response);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            public void deliverResult(Review[] reviews){
                reviewJson = reviews;
                super.deliverResult(reviewJson);
            }

        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Review[]> loader, Review[] data) {

        if(data.length == 0)
            return;

        reviewAdapter.setReviews(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Review[]> loader) {

    }

    @Override
    public void onClick(Review review) {

    }

    private void verifyLoader(LoaderManager loaderManager, Bundle bundle){
        Loader<Video[]> videoLoader = loaderManager.getLoader(LOADER_ID_REVIEW);

        if (videoLoader == null)
            loaderManager.initLoader(LOADER_ID_REVIEW, bundle, this);
        else
            loaderManager.restartLoader(LOADER_ID_REVIEW, bundle, this);
    }
}
