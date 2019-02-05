package com.example.maxim.imageviewer.mvp.model.image;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.example.maxim.imageviewer.mvp.model.entity.Photo;
import com.example.maxim.imageviewer.mvp.model.entity.instagram.instamedia.Data;
import com.example.maxim.imageviewer.mvp.model.repository.Repository;

public interface ImageLoader
{
    void loadInto(@Nullable String url, ImageView container);

    void download(Repository repository, Data data);
}
