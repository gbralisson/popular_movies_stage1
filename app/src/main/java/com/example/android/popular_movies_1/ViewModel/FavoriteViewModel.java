package com.example.android.popular_movies_1.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.popular_movies_1.Database.AppDatabase;
import com.example.android.popular_movies_1.Model.Movie;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel{

    private LiveData<Movie[]> movies;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        movies = database.favoriteDAO().loadAllMovies();
    }

    public LiveData<Movie[]> getFavorites(){
        return movies;
    }

}
