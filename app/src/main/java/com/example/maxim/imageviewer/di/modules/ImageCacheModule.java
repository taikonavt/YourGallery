package com.example.maxim.imageviewer.di.modules;

import com.example.maxim.imageviewer.mvp.model.repository.IImageCache;
import com.example.maxim.imageviewer.mvp.model.repository.ImageCacheImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class ImageCacheModule {

    @Provides
    public IImageCache providImageCache(){
        return new ImageCacheImpl();
    }
}
