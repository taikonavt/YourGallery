package com.example.maxim.imageviewer.di;

import com.example.maxim.imageviewer.di.modules.TestRepositoryModule;
import com.example.maxim.imageviewer.mvp.presenter.DbPhotoFragmentPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        TestRepositoryModule.class
})
public interface TestComponent {
    void inject(DbPhotoFragmentPresenter presenter);
}
