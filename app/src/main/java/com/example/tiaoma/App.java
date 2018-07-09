package com.example.tiaoma;

import android.app.Application;

public class App extends Application {

    private App app;

    @Override
    public void onCreate() {
        super.onCreate();
        this.app = this;
    }

    public App getApp() {
        return app;
    }

}
