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
    private static final String DATABASE_NAME_POPULAR = "moviepopular";
    private static final String DATABASE_NAME_TOPRATED = "movietoprated";
    private static AppDatabase sInstance;
    private static AppDatabase sInstancePopular;
    private static AppDatabase sInstanceToprated;

    public static AppDatabase getsInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public static AppDatabase getsInstancePopular(Context context){
        if (sInstancePopular == null){
            synchronized (LOCK){
                sInstancePopular = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME_POPULAR)
                        .build();
            }
        }
        return sInstancePopular;
    }

    public static AppDatabase getsInstanceToprated(Context context){
        if (sInstanceToprated == null){
            synchronized (LOCK){
                sInstanceToprated = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME_TOPRATED)
                        .build();
            }
        }
        return sInstanceToprated;
    }

    public abstract FavoriteDAO favoriteDAO();

//    public abstract PopularDAO popularDAO();

}
