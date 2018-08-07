package com.example.android.popular_movies_1.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.popular_movies_1.Database.AppDatabase;
import com.example.android.popular_movies_1.Model.Movie;

public class TopRatedViewModel extends AndroidViewModel {

    private LiveData<Movie[]> movies;

    public TopRatedViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getsInstanceToprated(this.getApplication());
        movies = database.favoriteDAO().loadAllMovies();
    }

    public LiveData<Movie[]> getMovieToprated(){
        return movies;
    }
}
