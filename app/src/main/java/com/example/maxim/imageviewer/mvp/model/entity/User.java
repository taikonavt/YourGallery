package com.example.maxim.imageviewer.mvp.model.entity;

import com.example.maxim.imageviewer.mvp.model.entity.instagram.instamedia.Data;

import java.util.List;

public class User {
    private String login;
    private String accessToken;
    private List<Photo> photoListFromDb;
    private List<Data> dataListFromNet;

    public User(String login, String accessToken){
        this.accessToken = accessToken;
        this.login = login;
    }

    public User(){
    }

    public List<Photo> getPhotoListFromDb() {
        return photoListFromDb;
    }

    public void setPhotoListFromDb(List<Photo> photoListFromDb) {
        this.photoListFromDb = photoListFromDb;
    }

    public List<Data> getDataListFromNet() {
        return dataListFromNet;
    }

    public void setDataListFromNet(List<Data> dataListFromNet) {
        this.dataListFromNet = dataListFromNet;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getLogin() {
        return login;
    }
}
