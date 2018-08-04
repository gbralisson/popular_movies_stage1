package com.example.android.popular_movies_1.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Database;
import android.support.annotation.NonNull;

import com.example.android.popular_movies_1.Database.AppDatabase;

public class AddFavoriteFactoryViewModel extends ViewModelProvider.NewInstanceFactory {

    private AppDatabase database;
    private int id;

    public AddFavoriteFactoryViewModel(AppDatabase database, int id){
        this.database = database;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddFavoriteViewModel(database, id);
    }
}
