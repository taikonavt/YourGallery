package com.example.maxim.imageviewer.di.modules;

import com.example.maxim.imageviewer.mvp.model.api.IDataSource;
import com.example.maxim.imageviewer.mvp.model.image.ImageLoader;
import com.example.maxim.imageviewer.mvp.model.repository.ICache;
import com.example.maxim.imageviewer.mvp.model.repository.Repository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module (includes = {CacheModule.class, ApiModule.class, ImageLoaderModule.class})
public class RepositoryModule {

    @Singleton
    @Provides
    public Repository provideRepository(ICache cache, IDataSource dataSource, ImageLoader imageLoader){
        return new Repository(cache, dataSource, imageLoader);
    }
}
