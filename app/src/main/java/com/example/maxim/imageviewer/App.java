package com.example.maxim.imageviewer;

import android.app.Application;

import com.example.maxim.imageviewer.di.AppComponent;
import com.example.maxim.imageviewer.di.DaggerAppComponent;
import com.example.maxim.imageviewer.di.modules.AppModule;
import com.example.maxim.imageviewer.mvp.model.entity.room.db.UserDatabase;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

public class App extends Application {
    static private App instance;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Timber.plant(new Timber.DebugTree());
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        UserDatabase.create(this);
        if(LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(this);
    }

    public static App getInstance(){
        return instance;
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}
