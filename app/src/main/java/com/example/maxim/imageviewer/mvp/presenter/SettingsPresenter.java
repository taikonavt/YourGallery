package com.example.maxim.imageviewer.mvp.presenter;

import android.os.Build;
import android.webkit.CookieManager;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.maxim.imageviewer.common.Theme;
import com.example.maxim.imageviewer.common.Var;
import com.example.maxim.imageviewer.mvp.model.entity.User;
import com.example.maxim.imageviewer.mvp.model.preferences.IPrefs;
import com.example.maxim.imageviewer.mvp.model.repository.Repository;
import com.example.maxim.imageviewer.mvp.view.SettingsView;

import javax.inject.Inject;

@InjectViewState
public class SettingsPresenter extends MvpPresenter<SettingsView> {

    @Inject
    Repository repository;
    @Inject
    IPrefs prefs;

    private boolean isAuth;

    @Override
    public void attachView(SettingsView view) {
        super.attachView(view);
        getUser();
    }

    private void getUser(){
        repository.getUser()
                .subscribe(user -> {
                    if (user == null){
                        setViewNotAuth();
                    } else if (user.getAccessToken() == null){
                        setViewNotAuth();
                    } else {
                        setViewIsAuth(user);
                    }
                });
    }

    private void setViewIsAuth(User user) {
        isAuth = true;
        getViewState().setLoginHintTvVisible(true);
        getViewState().setLoginTvVisible(true);
        getViewState().setLogin(user.getLogin());
        getViewState().setPassTvHintVisible(true);
        getViewState().setPassTvVisible(true);
        getViewState().setPass("*******");
        getViewState().setSignOutButton();
    }

    private void setViewNotAuth() {
        isAuth = false;
        getViewState().setLoginHintTvVisible(false);
        getViewState().setLoginTvVisible(false);
        getViewState().setLogin("");
        getViewState().setPassTvHintVisible(false);
        getViewState().setPassTvVisible(false);
        getViewState().setPass("");
        getViewState().setSignInButton();
    }

    public void switchChecked() {
        Var.themeIsChanged = true;
        prefs.setTheme(Theme.Black);
        getViewState().setThemeSwitchChecked(true);
    }

    public void switchUnchecked() {
        Var.themeIsChanged = true;
        prefs.setTheme(Theme.Green);
        getViewState().setThemeSwitchChecked(false);
    }

    public void onBackPressed() {
        if (isAuth) {
            getViewState().finishActivity();
        } else {
            getViewState().finishApp();
        }
    }

    public void resultOk() {
        getUser();
    }

    public void signInBtnClicked() {
        getViewState().startSignInActivity();
    }

    public void signOutBtnClicked() {
        repository.resetUser();
        setViewNotAuth();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
        } else {
            CookieManager.getInstance().removeAllCookie();
        }
    }
}
