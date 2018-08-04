package com.example.android.popular_movies_1.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popular_movies_1.Database.AppDatabase;
import com.example.android.popular_movies_1.Model.Movie;

public class AddFavoriteViewModel extends ViewModel {

    private LiveData<Movie> favorite;

    public AddFavoriteViewModel(AppDatabase database, int id){
        favorite = database.favoriteDAO().loadFavorite(id);
    }

    public LiveData<Movie> getFavorite(){
        return favorite;
    }

}
