package com.example.maxim.imageviewer;

import android.app.Application;

public class App extends Application {
    static private App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance(){
        return instance;
    }
}
