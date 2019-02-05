package com.example.maxim.imageviewer.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.maxim.imageviewer.App;
import com.example.maxim.imageviewer.R;
import com.example.maxim.imageviewer.common.Directories;
import com.example.maxim.imageviewer.common.Theme;
import com.example.maxim.imageviewer.common.Var;
import com.example.maxim.imageviewer.mvp.model.entity.Photo;
import com.example.maxim.imageviewer.mvp.presenter.MainPresenter;
import com.example.maxim.imageviewer.mvp.view.MainView;
import com.example.maxim.imageviewer.ui.fragment.DbPhotoFragment;
import com.example.maxim.imageviewer.ui.fragment.NetPhotoFragment;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends MvpAppCompatActivity
            implements MainView {

    private static final int PHOTO_REQUEST_CODE = 1232;
    private static final int SETTINGS_CODE = 1233;
    private static final int PERMISSION_REQUEST_CODE = 452;
    private static final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.toolbar_app_bar_main)
    Toolbar toolbar;
    @BindView(R.id.activity_main_base_view)
    View baseView;
    @InjectPresenter
    MainPresenter mainPresenter;
    private Photo photo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainPresenter.onStartCreating();
        setTheme();

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        App.getInstance().getAppComponent().inject(this);

        setToolbar();

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (!checkPermission()) {
            askPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainPresenter.onResumed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mainPresenter.onMakePhotoClicked();
                return true;
            }
            case R.id.settings: {
                mainPresenter.onSettingsClicked();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @ProvidePresenter
    public MainPresenter provideMainPresenter() {
        MainPresenter presenter = new MainPresenter();
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_from_phone:
                mainPresenter.onNavFromPhoneClicked();
                return true;
            case R.id.navigation_from_network:
                mainPresenter.onNavFromNetClicked();
                return true;
            case R.id.navigation_favourite:
                mainPresenter.onNavFavouriteClicked();
                return true;
        }
        return false;
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    mainPresenter.photoGot(photo);
                    galleryAddPic();
                }
                if (resultCode == RESULT_CANCELED){
                    mainPresenter.photoCanceled(photo);
                }
                photo = null;
                break;
            }
            case SETTINGS_CODE: {
                if (resultCode == RESULT_CANCELED){
                    finish();
                }
                break;
            }
        }
    }

    @Override
    public void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, SETTINGS_CODE);
    }

    @Override
    public void makePhoto() {
        if (!checkPermission()) {
            askPermission();
        } else {
            startImageCaptureIntent();
        }
    }

    @Override
    public void showPhotoFromPhone() {
        showFragment(DbPhotoFragment.getInstance(DbPhotoFragment.ALL));
    }

    @Override
    public void showPhotoFromNet() {
        showFragment(NetPhotoFragment.getInstance(null));
    }

    @Override
    public void showFavPhoto() {
        showFragment(DbPhotoFragment.getInstance(DbPhotoFragment.FAV));
    }

    @Override
    public void recreateActivity() {
        recreate();
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private void startImageCaptureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            try {
                photo = createPhoto();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photo != null) {
                Uri uri = Uri.parse(photo.getPhotoUrl());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, PHOTO_REQUEST_CODE);
            }
        }
    }

    private Photo createPhoto() throws IOException {
        String directory = Directories.getDirPath();
        String name = "photo_" + System.currentTimeMillis() + ".jpg";
        String photoPath = directory + name;

        File photoFile = new File(photoPath);
        if (photoFile.createNewFile()) {
            Uri photoUri = Uri.fromFile(photoFile);
            return new Photo(photoUri.toString(), false, name);
        } else
            return null;
    }

    @Override
    public void deletePhoto(Photo photo) {
        String directory = Directories.getDirPath();
        String photoPath = directory + photo.getName();
        File photoFile = new File(photoPath);
        photoFile.delete();
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.parse(photo.getPhotoUrl());
        mediaScanIntent.setData(uri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setToolbar() {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_photo_camera_24dp);
        int color = fetchColor(getBaseContext(), R.attr.onColorPrimary);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(drawable);
        }
    }

    private int fetchColor(Context context, int id) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(id, typedValue, true);
        int colorRes = typedValue.resourceId;
        int color = -1;
        try {
            color = context.getResources().getColor(colorRes);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return color;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Snackbar.make(baseView, R.string.permissions_denied, Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean checkPermission() {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    private void setTheme() {
        if (Var.theme == Theme.Black) {
            setTheme(R.style.MyAppThemeBlack);
        }
    }
}