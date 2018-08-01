package com.example.android.popular_movies_1.Loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.android.popular_movies_1.Adapter.VideoAdapter;
import com.example.android.popular_movies_1.Model.Video;
import com.example.android.popular_movies_1.Network.NetworkUtils;
import com.example.android.popular_movies_1.R;
import com.example.android.popular_movies_1.Utils.ParseJsonFromMovieDB;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class LoaderVideos implements LoaderManager.LoaderCallbacks<Video[]>,
        VideoAdapter.VideoAdapterOnClickHandler {

    private static final String SEARCH_VIDEO = "queryVideo";
    private static final int LOADER_ID_VIDEO = 01;

    private Context context;
    private VideoAdapter videoAdapter;


    public LoaderVideos(Context context, LoaderManager loaderManager, Bundle bundle, VideoAdapter videoAdapter) {
        this.context = context;
        this.videoAdapter = videoAdapter;
        verifyLoader(loaderManager, bundle);
    }

    @NonNull
    @Override
    public Loader<Video[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Video[]>(context) {

            Video[] videoJson;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (args == null) {
                    return;
                }

                if (videoJson != null) {
                    deliverResult(videoJson);
                }else
                    forceLoad();
            }

            @Override
            public Video[] loadInBackground() {

                String Url = args.getString(SEARCH_VIDEO);

                try {
                    URL videoUrl = new URL(Url);
                    String response = NetworkUtils.getResponseFromHttpUrl(videoUrl);
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
                super.deliverResult(videoJson);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Video[]> loader, Video[] data) {
        Log.d("teste", data[0].getName());
        videoAdapter.setVideos(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Video[]> loader) {

    }

    @Override
    public void onClick(Video video) {

    }

    private void verifyLoader(LoaderManager loaderManager, Bundle bundle){
        Loader<Video[]> videoLoader = loaderManager.getLoader(LOADER_ID_VIDEO);

        if (videoLoader == null)
            loaderManager.initLoader(LOADER_ID_VIDEO, bundle, this);
        else
            loaderManager.restartLoader(LOADER_ID_VIDEO, bundle, this);
    }

}
