package com.example.maxim.imageviewer.mvp.model.entity;

public class Photo {
    private String name;
    private boolean isFavourite;
    private String photoUrl;

    public Photo(){
    }

    public Photo(String photoUrl, boolean favourite, String name) {
        this.photoUrl = photoUrl;
        this.isFavourite = favourite;
        this.name = name;
    }

    public Photo(String photoUrl, String thumbnailUrl, boolean favourite, String name) {
        this.photoUrl = photoUrl;
        this.isFavourite = favourite;
        this.name = name;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
