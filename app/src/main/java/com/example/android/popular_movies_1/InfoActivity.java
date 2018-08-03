package com.example.android.popular_movies_1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popular_movies_1.Adapter.MovieAdapter;
import com.example.android.popular_movies_1.Adapter.ReviewAdapter;
import com.example.android.popular_movies_1.Adapter.VideoAdapter;
import com.example.android.popular_movies_1.Database.AppDatabase;
import com.example.android.popular_movies_1.Loaders.LoaderReviews;
import com.example.android.popular_movies_1.Loaders.LoaderVideos;
import com.example.android.popular_movies_1.Model.Movie;
import com.example.android.popular_movies_1.Model.Video;
import com.example.android.popular_movies_1.Network.NetworkUtils;
import java.net.URL;

public class InfoActivity extends AppCompatActivity implements VideoAdapter.VideoAdapterOnClickHandler{

    private Movie movie;

    private RecyclerView recyclerViewTrailer;
    private RecyclerView recyclerViewReview;

    private VideoAdapter videoAdapter;
    private ReviewAdapter reviewAdapter;

    private String KEY_INTENT = "movie_data";
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
            if(getIntent().hasExtra(KEY_INTENT)){
                movie = (Movie) getIntent().getSerializableExtra(KEY_INTENT);

                txtInfoTitle.setText(movie.getTitle());
                txtInfoOverview.setText(movie.getOverview());
                MovieAdapter.loadImageMovie(this, movie.getPoster_path(), imgInfoPoster);
                txtInfoRate.setText(String.valueOf(movie.getVote_average()));
                txtInfoRelease.setText(movie.getRelease_data());

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_info){
            insertDatabase();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Video video) {
        Log.d("teste", "clicou");
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

    public class InsertDB extends AsyncTask<Movie, Void, Void>{

        @Override
        protected Void doInBackground(Movie... movies) {
            database.favoriteDAO().insertFavorite(movie);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("teste", "inseriu");
        }
    }
}
