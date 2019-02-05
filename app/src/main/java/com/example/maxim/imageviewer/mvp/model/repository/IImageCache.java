package com.example.maxim.imageviewer.mvp.model.repository;

import android.graphics.Bitmap;

import java.io.File;

import io.reactivex.Single;

public interface IImageCache {

    File saveImage(String url, Bitmap resource);

    Single<File> getFile(String url);
}
