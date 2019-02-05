package com.example.maxim.imageviewer.mvp.view.item;

import android.graphics.Bitmap;

public interface DbPhotoItemView {
    int getPos();
    void setImage(String path);
    void setIsFavourite(boolean b);
    boolean getIsFavourite();
}
