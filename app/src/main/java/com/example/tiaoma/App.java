package com.example.tiaoma;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.example.tiaoma.db.AppDatabase;
import com.example.tiaoma.utils.AppExecutors;

public class App extends Application {

    private static App app;

    private AppDatabase db;

    private AppExecutors executors;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        db = Room.databaseBuilder(this,AppDatabase.class,"edit").build();
        executors = new AppExecutors();
    }

    public static App getApp() {
        return app;
    }

    public AppDatabase getDb() {
        return db;
    }

    public AppExecutors getExecutors() {
        return executors;
    }
}
