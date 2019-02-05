package com.example.maxim.imageviewer.mvp.model.repository;

import android.graphics.Bitmap;
import android.os.Environment;

import com.example.maxim.imageviewer.App;
import com.example.maxim.imageviewer.mvp.model.entity.room.RoomCachedImage;
import com.example.maxim.imageviewer.mvp.model.entity.room.db.UserDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ImageCacheImpl implements IImageCache {

    private static final String IMAGE_FOLDER_NAME = "image";

    @Override
    public File saveImage(String url, Bitmap bitmap) {
        if (!getImageDir().exists() && !getImageDir().mkdirs()) {
            throw new RuntimeException("Failed to create directory: " + getImageDir().toString());
        }

        final String fileFormat = url.contains(".jpg") ? ".jpg" : ".png";
        final File imageFile = new File(getImageDir(), SHA1(url) + fileFormat);
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(imageFile);
            bitmap.compress(fileFormat.equals("jpg") ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            Timber.d("Failed to save image");
            return null;
        }

        Completable.fromAction(() -> {
            RoomCachedImage image = new RoomCachedImage(url, imageFile.toString());
            UserDatabase.getInstance().getCachedImageDao().insert(image);
        })
                .subscribeOn(Schedulers.io())
                .subscribe();

        return imageFile;
    }

    @Override
    public Single<File> getFile(String url) {
        return Single.create(emitter -> {
            RoomCachedImage cachedImage =
                    UserDatabase.getInstance().getCachedImageDao().getCachedImageByUrl(url);
            if (cachedImage != null){
                emitter.onSuccess(new File(cachedImage.getPath()));
            } else {
                emitter.onSuccess(null);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cast(File.class);
    }

    private File getImageDir() {
        return new File(App.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + IMAGE_FOLDER_NAME);
    }

    private String SHA1(String s) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(s.getBytes(), 0, s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }
}
