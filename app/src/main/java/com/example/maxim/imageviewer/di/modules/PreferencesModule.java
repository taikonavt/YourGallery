package com.example.maxim.imageviewer.di.modules;

import com.example.maxim.imageviewer.mvp.model.preferences.IPrefs;
import com.example.maxim.imageviewer.mvp.model.preferences.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferencesModule {

    @Singleton
    @Provides
    public IPrefs providePrefs(){
        return new SharedPreferences();
    }
}
