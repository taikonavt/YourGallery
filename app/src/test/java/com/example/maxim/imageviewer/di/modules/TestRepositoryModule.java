package com.example.maxim.imageviewer.di.modules;

import com.example.maxim.imageviewer.mvp.model.repository.Repository;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TestRepositoryModule {

    @Singleton
    @Provides
    public Repository provideRepository(){
        return Mockito.mock(Repository.class);
    }
}
