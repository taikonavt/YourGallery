package com.example.maxim.imageviewer.di.modules;

import com.example.maxim.imageviewer.mvp.model.repository.ICache;
import com.example.maxim.imageviewer.mvp.model.repository.RoomCache;

import dagger.Module;
import dagger.Provides;

@Module
public class CacheModule {

    @Provides
    public ICache provideCache(){
        return new RoomCache();
    }
}
