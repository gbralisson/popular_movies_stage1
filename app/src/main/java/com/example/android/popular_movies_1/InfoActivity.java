package com.example.android.popular_movies_1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popular_movies_1.Adapter.MovieAdapter;
import com.example.android.popular_movies_1.Adapter.ReviewAdapter;
import com.example.android.popular_movies_1.Adapter.VideoAdapter;
import com.example.android.popular_movies_1.Database.AppDatabase;
import com.example.android.popular_movies_1.Loaders.LoaderReviews;
import com.example.android.popular_movies_1.Loaders.LoaderVideos;
import com.example.android.popular_movies_1.Model.Movie;
import com.example.android.popular_movies_1.Model.Video;
import com.example.android.popular_movies_1.Network.NetworkUtils;
import com.example.android.popular_movies_1.ViewModel.AddFavoriteFactoryViewModel;
import com.example.android.popular_movies_1.ViewModel.AddFavoriteViewModel;

import java.net.URL;

public class InfoActivity extends AppCompatActivity implements VideoAdapter.VideoAdapterOnClickHandler{

    private Movie movie;

    private RecyclerView recyclerViewTrailer;
    private RecyclerView recyclerViewReview;

    private VideoAdapter videoAdapter;
    private ReviewAdapter reviewAdapter;

    private static final String SEARCH_VIDEO = "queryVideo";
    private static final String SEARCH_REVIEW = "queryReview";

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView txtInfoTitle = findViewById(R.id.txt_info_title);
        TextView txtInfoOverview = findViewById(R.id.txt_info_overview);
        ImageView imgInfoPoster = findViewById(R.id.img_info_poster);
        TextView txtInfoRate = findViewById(R.id.txt_info_rate);
        TextView txtInfoRelease = findViewById(R.id.txt_info_release);
        recyclerViewTrailer = findViewById(R.id.rv_videos);
        recyclerViewReview = findViewById(R.id.rv_review);

        database = AppDatabase.getsInstance(getApplicationContext());

        // Verify if there is any data from another activity
        if (getIntent() != null){
            if(getIntent().hasExtra(getString(R.string.key_intent))){
                movie = (Movie) getIntent().getSerializableExtra(getString(R.string.key_intent));

                txtInfoTitle.setText(movie.getTitle());
                txtInfoOverview.setText(movie.getOverview());
                MovieAdapter.loadImageMovie(this, movie.getPoster_path(), imgInfoPoster);
                txtInfoRate.setText(String.valueOf(movie.getVote_average()));
                txtInfoRelease.setText(movie.getRelease_data());

                AddFavoriteFactoryViewModel favoriteFactory = new AddFavoriteFactoryViewModel(database, movie.getId());
                AddFavoriteViewModel favoriteViewModel = ViewModelProviders.of(this, favoriteFactory).get(AddFavoriteViewModel.class);

                favoriteViewModel.getFavorite().observe(this, new Observer<Movie>() {
                    @Override
                    public void onChanged(@Nullable Movie movie) {
                        if(movie != null) {
                            setMovieStatus(true);
                        }

                    }
                });

            }
        }

        createRecyclerTrailers();
        createRecyclerReviews();

        createLoaderTrailers();
        createLoaderReviews();

    }

    private URL makeUrlQuery(String sort){
        int id = movie.getId();

        return NetworkUtils.buildUrlMovieReviews(String.valueOf(id), sort);
    }

    private void createLoaderTrailers(){
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_VIDEO, makeUrlQuery(getString(R.string.videos)).toString());

        new LoaderVideos(getApplicationContext(), getSupportLoaderManager(), bundle, videoAdapter);
    }

    private void createLoaderReviews(){
        Bundle bundle = new Bundle();
        bundle.putString(SEARCH_REVIEW, makeUrlQuery(getString(R.string.reviews)).toString());

        new LoaderReviews(getApplicationContext(), getSupportLoaderManager(), bundle, reviewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu_activity, menu);

        if(movie.isFavorite()) {
            MenuItem menuItem = menu.findItem(R.id.menu_info);
            menuItem.setIcon(R.drawable.full_star);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_info){

            if (movie.isFavorite()) {
                item.setIcon(R.drawable.blank_star);
                deleteDatabase();
                movie.setFavorite(false);
            }
            else {
                item.setIcon(R.drawable.full_star);
                insertDatabase();
                movie.setFavorite(true);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Video video) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://"+video.getKey()));
        startActivity(intent);
    }

    public void createRecyclerTrailers(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewTrailer.setLayoutManager(linearLayoutManager);

        videoAdapter = new VideoAdapter(getApplicationContext(), this);
        recyclerViewTrailer.setAdapter(videoAdapter);
        recyclerViewTrailer.setHasFixedSize(true);
    }

    public void createRecyclerReviews(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewReview.setLayoutManager(linearLayoutManager);

        reviewAdapter = new ReviewAdapter(getApplicationContext(), null);
        recyclerViewReview.setAdapter(reviewAdapter);
        recyclerViewReview.setHasFixedSize(true);
    }

    public void insertDatabase(){
        new InsertDB().execute(movie);
    }

    public void deleteDatabase(){
        new DeleteDB().execute(movie);
    }

    public class InsertDB extends AsyncTask<Movie, Void, Void>{

        @Override
        protected Void doInBackground(Movie... movies) {
            database.favoriteDAO().insertFavorite(movie);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), "Movie Inserted in favorites", Toast.LENGTH_SHORT).show();
        }
    }

    public class DeleteDB extends AsyncTask<Movie, Void, Void>{

        @Override
        protected Void doInBackground(Movie... movies) {
            database.favoriteDAO().deleteFavorite(movie);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), "Movie removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    public void setMovieStatus(boolean status){
        this.movie.setFavorite(status);
    }
}
