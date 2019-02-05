package com.example.maxim.imageviewer.mvp.model.entity.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class RoomCachedImage {

    @NonNull
    @PrimaryKey
    private String url;
    private String path;

    public RoomCachedImage(String url, String path){
        this.url = url;
        this.path = path;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
