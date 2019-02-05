package com.example.maxim.imageviewer.mvp.model.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.maxim.imageviewer.App;
import com.example.maxim.imageviewer.NetworkStatus;
import com.example.maxim.imageviewer.R;
import com.example.maxim.imageviewer.common.Const;
import com.example.maxim.imageviewer.common.Directories;
import com.example.maxim.imageviewer.mvp.model.api.IDataSource;
import com.example.maxim.imageviewer.mvp.model.entity.Envelop;
import com.example.maxim.imageviewer.mvp.model.entity.Photo;
import com.example.maxim.imageviewer.mvp.model.entity.User;
import com.example.maxim.imageviewer.mvp.model.entity.instagram.instamedia.Data;
import com.example.maxim.imageviewer.mvp.model.image.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class Repository {

    private ICache cache;
    private IDataSource dataSource;
    private ImageLoader imageLoader;


    public Repository(ICache cache, IDataSource dataSource, ImageLoader imageLoader){
        this.dataSource = dataSource;
        this.cache = cache;
        this.imageLoader = imageLoader;
    }

    public Flowable<List<Photo>> getPhotoListFromDb(boolean onlyFavourites){
        return cache.getPhotoList(onlyFavourites);
    }

    public void savePhotoToRepo(Photo photo){
        createThumbnail(photo);
        cache.savePhotoToDb(photo);
    }

    private void deletePhotoFromRepo(Photo photo){
        deleteThumbnail(photo);
        cache.deletePhoto(photo);
    }

    public void updatePhoto(Photo photo){
        cache.updatePhoto(photo);
    }

    public Single<Envelop> getDataListFromNet(User user){
        Timber.d("token %s", user.getAccessToken());
        if (NetworkStatus.isOnline()){
            return dataSource.getUserMedia(user.getAccessToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(instaMedia -> {
                        // TODO: 24.05.2019 check token validation
                        Data[] dataArray = instaMedia.getData();
                        List list = Arrays.asList(dataArray);
                        return new Envelop(Envelop.Type.LIST, list);
                    });
        } else {
            String message = App.getInstance().getString(R.string.error_net_unavailable);
            return Single.just(new Envelop(Envelop.Type.MESSAGE, message));
        }
    }

    public Single<User> getUser() {
        return cache.getUser();
    }

    public void saveUser(User user){
        cache.saveUser(user);
    }

    public void resetUser() {
        cache.resetUser();
    }

    public void synchronizePhotos() {
        addNewPhotoToDb();
        checkPhotosFromDbStillExist();
    }

    private void addNewPhotoToDb() {
        String[] photoList = getPhotosList(Directories.getDirPath());
        if (photoList != null) {
            for (int i = 0; i < photoList.length; i++) {
                String photoName = photoList[i];
                String photoPath = Directories.getDirPath() + photoName;
                Uri uri = Uri.fromFile(new File(photoPath));
                cache.containPhoto(uri.toString())
                        .subscribe(isContain -> {
                            if (!isContain) {
                                Photo photo = new Photo(uri.toString(), false, photoName);
                                savePhotoToRepo(photo);
                            }
                        });
            }
        }
    }

    private void checkPhotosFromDbStillExist() {
        boolean onlyFavourites = false;
        getPhotoListFromDb(onlyFavourites)
                .subscribe(photos -> {
                    for (int i = 0; i < photos.size(); i++) {
                        String path = Directories.getDirPath() + photos.get(i).getName();
                        File file = new File(path);
                        if (!file.exists()){
                            deletePhotoFromRepo(photos.get(i));
                        }
                    }
                });
    }

    private String[] getPhotosList(String path){
        File file = new File(path);
        String[] fileList = file.list(
                (dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));
        return fileList;
    }

    private void createThumbnail(Photo photo){
        String photoPath = Directories.getDirPath() + photo.getName();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, options);

        options.inSampleSize = calculateThumbnailSize(options);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
        try {
            String thumbnailPath = Directories.getThumbnailDir() + photo.getName();
            OutputStream outputStream = new FileOutputStream(thumbnailPath);
            if (bitmap != null){
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            } else {
                File file = new File(photoPath);
                file.delete();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void deleteThumbnail(Photo photo) {
        String thumbnailPath = Directories.getThumbnailDir() + photo.getName();
        File file = new File(thumbnailPath);
        file.delete();
    }

    private int calculateThumbnailSize(BitmapFactory.Options options) {
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (width > Const.THUMBNAIL_WIDTH){
            int halfWidth = width / 2;
            while ((halfWidth / inSampleSize) > Const.THUMBNAIL_WIDTH){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public void savePhotoFromNet(Data data) {
        imageLoader.download(this, data);
    }
}
