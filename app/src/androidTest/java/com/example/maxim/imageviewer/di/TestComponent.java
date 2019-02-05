package com.example.maxim.imageviewer.di;

import com.example.maxim.imageviewer.RepositoryInstrumentedTest;
import com.example.maxim.imageviewer.di.modules.RepositoryModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RepositoryModule.class})
public interface TestComponent {
    void inject(RepositoryInstrumentedTest test);
}
