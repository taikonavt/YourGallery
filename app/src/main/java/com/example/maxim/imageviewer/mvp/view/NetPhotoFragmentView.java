package com.example.maxim.imageviewer.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface NetPhotoFragmentView extends MvpView {
    void showLoading();
    void hideLoading();
    void updateList();
    void showError(String message);
}
