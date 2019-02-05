package com.example.maxim.imageviewer.di;

import com.example.maxim.imageviewer.di.modules.AppModule;
import com.example.maxim.imageviewer.di.modules.PreferencesModule;
import com.example.maxim.imageviewer.di.modules.RepositoryModule;
import com.example.maxim.imageviewer.mvp.presenter.DbPhotoFragmentPresenter;
import com.example.maxim.imageviewer.mvp.presenter.MainPresenter;
import com.example.maxim.imageviewer.mvp.presenter.NetPhotoFragmentPresenter;
import com.example.maxim.imageviewer.mvp.presenter.SettingsPresenter;
import com.example.maxim.imageviewer.ui.activity.AuthActivity;
import com.example.maxim.imageviewer.ui.activity.MainActivity;
import com.example.maxim.imageviewer.ui.activity.SettingsActivity;
import com.example.maxim.imageviewer.ui.adapter.NetPhotoAdapter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        RepositoryModule.class,
        PreferencesModule.class
})

public interface AppComponent{
    void inject(MainActivity mainActivity);
    void inject(SettingsActivity settingsActivity);
    void inject(AuthActivity authActivity);
    void inject(MainPresenter mainPresenter);
    void inject(SettingsPresenter settingsPresenter);
    void inject(DbPhotoFragmentPresenter fragmentPresenter);
    void inject(NetPhotoFragmentPresenter fragmentPresenter);
    void inject(NetPhotoAdapter photoAdapter);
}
