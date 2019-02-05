package com.example.maxim.imageviewer.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.maxim.imageviewer.common.Theme;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SettingsView extends MvpView {

    void setLogin(String s);

    void setPass(String s);

    void setSignInButton();

    void setLoginTvVisible(boolean b);

    void setPassTvVisible(boolean b);

    void setSignOutButton();

    void setThemeSwitchChecked(boolean b);

    void setLoginHintTvVisible(boolean b);

    void setPassTvHintVisible(boolean b);

    void finishActivity();

    void startSignInActivity();

    void finishApp();
}
