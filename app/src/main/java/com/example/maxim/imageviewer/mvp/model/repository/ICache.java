package com.example.maxim.imageviewer.mvp.model.repository;

import com.example.maxim.imageviewer.mvp.model.entity.Photo;
import com.example.maxim.imageviewer.mvp.model.entity.User;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface ICache {
    Flowable<List<Photo>> getPhotoList(boolean onlyFavourites);

    void savePhotoToDb(Photo photo);

    void deletePhoto(Photo photo);

    Single<Photo> getPhoto(String url);

    Single<Boolean> containPhoto(String url);

    void updatePhoto(Photo photo);

    Single<User> getUser();

    void saveUser(User user);

    void resetUser();
}
