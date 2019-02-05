package com.example.maxim.imageviewer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.maxim.imageviewer.App;
import com.example.maxim.imageviewer.R;
import com.example.maxim.imageviewer.common.Theme;
import com.example.maxim.imageviewer.common.Var;
import com.example.maxim.imageviewer.mvp.presenter.SettingsPresenter;
import com.example.maxim.imageviewer.mvp.view.SettingsView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends MvpAppCompatActivity
                implements SettingsView{

    private static final int REQUEST_CODE = 3;

    @BindView(R.id.toolbar_app_bar_settings)
    Toolbar toolbar;
    @BindView(R.id.settings_login_hint_tv)
    TextView loginHintTv;
    @BindView(R.id.settings_login_tv)
    TextView loginTv;
    @BindView(R.id.settings_password_hint_tv)
    TextView passwordHintTv;
    @BindView(R.id.settings_password_tv)
    TextView passwordTv;
    @BindView(R.id.settings_sign_out_btn)
    Button signOutBtn;
    @BindView(R.id.settings_sign_in_btn)
    Button signInBtn;
    @BindView(R.id.settings_theme_switch)
    Switch themeSwitch;
    @BindView(R.id.settings_switch_text_tv)
    TextView themeTv;
    @InjectPresenter
    SettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme();

        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        App.getInstance().getAppComponent().inject(this);

        setToolbar();
        setThemeSwitchChecked(Var.theme == Theme.Black);

        setListeners();
    }

    private void setListeners() {
        themeSwitch.setOnClickListener(view -> {
            if (themeSwitch.isChecked()){
                presenter.switchChecked();
            } else {
                presenter.switchUnchecked();
            }
        });

        signInBtn.setOnClickListener(view -> {
            presenter.signInBtnClicked();
        });

        signOutBtn.setOnClickListener(view -> {
            presenter.signOutBtnClicked();
        });
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }

    @ProvidePresenter
    public SettingsPresenter provideMainPresenter() {
        SettingsPresenter presenter = new SettingsPresenter();
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }

    @Override
    public void setLogin(String s) {
        loginTv.setText(s);
    }

    @Override
    public void setPass(String s) {
        passwordTv.setText(s);
    }

    @Override
    public void setSignInButton() {
        signInBtn.setVisibility(View.VISIBLE);
        signOutBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setSignOutButton() {
        signInBtn.setVisibility(View.INVISIBLE);
        signOutBtn.setVisibility(View.VISIBLE);
    }

    private void setTheme() {
        if (Var.theme == Theme.Black){
            setTheme(R.style.MyAppThemeBlack);
        }
    }

    @Override
    public void setThemeSwitchChecked(boolean b) {
        themeSwitch.setChecked(b);
        if (b) {
            themeTv.setText(R.string.settings_theme_black);
        } else {
            themeTv.setText(R.string.settings_theme_green);
        }
    }

    @Override
    public void setLoginHintTvVisible(boolean b) {
        if (b) {
            loginHintTv.setVisibility(View.VISIBLE);
        } else {
            loginHintTv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setPassTvHintVisible(boolean b) {
        if (b) {
            passwordHintTv.setVisibility(View.VISIBLE);
        } else {
            passwordHintTv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void finishActivity() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void startSignInActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void finishApp() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data == null){
            return;
        }
        if (resultCode == RESULT_OK){
            presenter.resultOk();
        }
    }

    @Override
    public void setLoginTvVisible(boolean b) {
        if (b) {
            loginTv.setVisibility(View.VISIBLE);
        } else {
            loginTv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setPassTvVisible(boolean b) {
        if (b) {
            passwordTv.setVisibility(View.VISIBLE);
        } else {
            passwordTv.setVisibility(View.INVISIBLE);
        }
    }
}
