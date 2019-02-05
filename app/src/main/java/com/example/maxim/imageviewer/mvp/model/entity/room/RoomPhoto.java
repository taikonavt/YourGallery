package com.example.maxim.imageviewer.mvp.model.entity.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class RoomPhoto {

    @NonNull
    @PrimaryKey
    private String url;
    private boolean isFavourite;
    private String name;

    public RoomPhoto(@NonNull String url, boolean isFavourite, String name){
        this.url = url;
        this.isFavourite = isFavourite;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
