package com.example.android.popular_movies_1.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.popular_movies_1.Model.Movie;

@Dao
public interface PopularDAO {

    @Query("SELECT * FROM favorite ORDER BY id")
    LiveData<Movie[]> loadAllMovies();

    @Query("SELECT * FROM favorite WHERE id = :id")
    LiveData<Movie> loadFavorite(int id);

    @Insert
    void insertFavorite(Movie movie);

}
