package com.example.maxim.imageviewer.mvp.model.repository;

import com.example.maxim.imageviewer.mvp.model.entity.Photo;
import com.example.maxim.imageviewer.mvp.model.entity.User;
import com.example.maxim.imageviewer.mvp.model.entity.room.RoomPhoto;
import com.example.maxim.imageviewer.mvp.model.entity.room.RoomUser;
import com.example.maxim.imageviewer.mvp.model.entity.room.db.UserDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RoomCache implements ICache {
    @Override
    public Flowable<List<Photo>> getPhotoList(boolean onlyFavourites) {
        return UserDatabase.getInstance().getPhotoDao()
                    .getAll()
                    .map(roomPhotoList ->{
                        List<Photo> photoList = new ArrayList<>();
                        for (int i = 0; i < roomPhotoList.size(); i++) {
                            RoomPhoto roomPhoto = roomPhotoList.get(i);
                            if (onlyFavourites){
                                if (roomPhoto.isFavourite()) {
                                    photoList.add(RoomCache.this.castPhoto(roomPhoto));
                                }
                            } else {
                                photoList.add(RoomCache.this.castPhoto(roomPhoto));
                            }
                        }
                        return photoList;
                    })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cast((Class<List<Photo>>)(Class)List.class);
    }

    private Photo castPhoto(RoomPhoto roomPhoto){
        return new Photo(roomPhoto.getUrl(),
                roomPhoto.isFavourite(),
                roomPhoto.getName());
    }

    private RoomPhoto castPhoto(Photo photo){
        String photoName = photo.getName();
        return new RoomPhoto(photo.getPhotoUrl(),
                photo.isFavourite(),
                photoName);
    }

    @Override
    public void savePhotoToDb(Photo photo) {
        String photoName = photo.getName();
        RoomPhoto roomPhoto = new RoomPhoto(
                photo.getPhotoUrl(), photo.isFavourite(), photoName);
        Completable.fromAction(() -> UserDatabase.getInstance().getPhotoDao().insert(roomPhoto))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void deletePhoto(Photo photo) {
        Completable.fromAction(() -> {
            String photoName = photo.getName();
            RoomPhoto roomPhoto = new RoomPhoto(
                    photo.getPhotoUrl(), photo.isFavourite(), photoName);
            UserDatabase.getInstance().getPhotoDao().delete(roomPhoto);
        })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public Single<Photo> getPhoto(String url) {
        return Single.create(emitter -> {
            RoomPhoto roomPhoto = UserDatabase.getInstance().getPhotoDao()
                    .findByUrl(url);
            if (roomPhoto == null){
                emitter.onError(new RuntimeException("Photo doesn't exist"));
            } else {
                Photo photo = castPhoto(roomPhoto);
                emitter.onSuccess(photo);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cast(Photo.class);
    }

    @Override
    public Single<Boolean> containPhoto(String url) {
        return Single.create(emitter -> {
            RoomPhoto roomPhoto = UserDatabase.getInstance().getPhotoDao()
                    .findByUrl(url);
            if (roomPhoto == null){
                emitter.onSuccess(false);
            } else {
                emitter.onSuccess(true);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cast(Boolean.class);
    }

    @Override
    public void updatePhoto(Photo photo) {
        Completable.fromAction(() -> {
            RoomPhoto roomPhoto = castPhoto(photo);
            UserDatabase.getInstance().getPhotoDao()
                    .update(roomPhoto);
        })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public Single<User> getUser() {
        return Single.create(emitter -> {
            RoomUser roomUser = UserDatabase.getInstance().getUserDao().getUser();
            User user;
            if (roomUser != null) {
                user = new User(roomUser.getLogin(), roomUser.getToken());
            } else {
                user = new User();
            }
            emitter.onSuccess(user);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cast(User.class);
    }

    @Override
    public void saveUser(User user) {
        RoomUser roomUser = new RoomUser(user.getLogin(), user.getAccessToken());
        Completable.fromAction(() -> UserDatabase.getInstance().getUserDao().insert(roomUser))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void resetUser() {
        Completable.fromAction(() -> UserDatabase.getInstance().getUserDao().resetUser())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}
