package com.example.maxim.imageviewer.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.maxim.imageviewer.common.Var;
import com.example.maxim.imageviewer.mvp.model.entity.Photo;
import com.example.maxim.imageviewer.mvp.model.preferences.IPrefs;
import com.example.maxim.imageviewer.mvp.model.repository.Repository;
import com.example.maxim.imageviewer.mvp.view.MainView;

import javax.inject.Inject;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    @Inject
    Repository repository;
    @Inject
    IPrefs prefs;

    public MainPresenter(){
    }

    public void onStartCreating() {
        Var.theme = prefs.getTheme();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        repository.getUser()
                .subscribe(user -> {
                        getViewState().showPhotoFromPhone();
                });
    }

    @Override
    public void attachView(MainView view) {
        super.attachView(view);
        repository.getUser()
                .subscribe(user -> {
                    if (user == null || user.getAccessToken() == null){
                        getViewState().startSettingsActivity();
                    }
                });
    }

    public void onMakePhotoClicked() {
        getViewState().makePhoto();
    }

    public void onSettingsClicked() {
        getViewState().startSettingsActivity();
    }

    public void photoGot(Photo photo) {
        repository.savePhotoToRepo(photo);
    }

    public void onNavFromPhoneClicked() {
        getViewState().showPhotoFromPhone();
    }

    public void onNavFromNetClicked() {
        getViewState().showPhotoFromNet();
    }

    public void onNavFavouriteClicked() {
        getViewState().showFavPhoto();
    }

    public void onResumed() {
        if (Var.themeIsChanged){
            Var.themeIsChanged = false;
            getViewState().recreateActivity();
        }
    }

    public void photoCanceled(Photo photo) {
        getViewState().deletePhoto(photo);
    }
}
