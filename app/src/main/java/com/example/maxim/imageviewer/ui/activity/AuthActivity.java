package com.example.maxim.imageviewer.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.maxim.imageviewer.App;
import com.example.maxim.imageviewer.R;
import com.example.maxim.imageviewer.common.Theme;
import com.example.maxim.imageviewer.common.Var;
import com.example.maxim.imageviewer.mvp.model.entity.User;
import com.example.maxim.imageviewer.mvp.model.repository.Repository;

import javax.inject.Inject;

import timber.log.Timber;

public class AuthActivity extends AppCompatActivity {

    public static final String AUTH_URI = "https://api.instagram.com/oauth/authorize/";
    private static String CLIENT_ID;
    private static String REDIRECT_URI;

    public static final String LOGIN_KEY = "login_key";
    private String login = "login";

    @Inject
    Repository repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme();

        WebView webView = new WebView(this);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new MyWebClient());
        webView.getSettings().setJavaScriptEnabled(true);

        setContentView(webView);

        App.getInstance().getAppComponent().inject(this);

        CLIENT_ID = App.getInstance().getString(R.string.client_id);
        REDIRECT_URI = App.getInstance().getString(R.string.redirect_uri);

        Uri uri = Uri.parse(AUTH_URI).buildUpon()
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("response_type", "token")
                .build();
        String url = uri.toString() + "&redirect_uri=" + REDIRECT_URI;
        Timber.d(url);
        webView.loadUrl(url);
    }

    private void sendResultOk(){
        Intent intent = new Intent();
        intent.putExtra(LOGIN_KEY, login);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setTheme() {
        if (Var.theme == Theme.Black) {
            setTheme(R.style.MyAppThemeBlack);
        }
    }


    class MyWebClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(REDIRECT_URI)){
                saveToken(url);
                sendResultOk();
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                url = request.getUrl().toString();
                if (url.startsWith(REDIRECT_URI)){
                    saveToken(url);
                    sendResultOk();
                }
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        private void saveToken(String url){
            String parts[] = url.split("=");
            String requestToken = parts[1];
            User user = new User(login, requestToken);
            repository.saveUser(user);
        }
    }
}
