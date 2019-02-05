package com.example.maxim.imageviewer.di.modules;

import com.example.maxim.imageviewer.mvp.model.image.ImageLoader;
import com.example.maxim.imageviewer.mvp.model.image.android.ImageLoaderGlide;
import com.example.maxim.imageviewer.mvp.model.repository.IImageCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ImageCacheModule.class)
public class ImageLoaderModule {

    @Singleton
    @Provides
    public ImageLoader imageLoader(IImageCache imageCache){
        return new ImageLoaderGlide(imageCache);
    }
}
