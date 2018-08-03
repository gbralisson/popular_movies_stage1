package com.example.android.popular_movies_1.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.popular_movies_1.Model.Movie;

@Database(entities = {Movie.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "moviefavorite";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract FavoriteDAO favoriteDAO();

}
