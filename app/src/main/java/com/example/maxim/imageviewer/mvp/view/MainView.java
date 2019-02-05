package com.example.maxim.imageviewer.mvp.view;


import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.maxim.imageviewer.mvp.model.entity.Photo;

@StateStrategyType(SingleStateStrategy.class)
public interface MainView extends MvpView {

    @StateStrategyType(SkipStrategy.class)
    void startSettingsActivity();

    @StateStrategyType(SkipStrategy.class)
    void makePhoto();

    void showPhotoFromPhone();

    void showPhotoFromNet();

    void showFavPhoto();

    @StateStrategyType(SkipStrategy.class)
    void recreateActivity();

    @StateStrategyType(SkipStrategy.class)
    void deletePhoto(Photo photo);
}
