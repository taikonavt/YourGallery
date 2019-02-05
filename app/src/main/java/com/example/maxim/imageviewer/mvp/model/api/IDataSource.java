package com.example.maxim.imageviewer.mvp.model.api;

import com.example.maxim.imageviewer.mvp.model.entity.instagram.InstaMedia;
import com.example.maxim.imageviewer.mvp.model.entity.instagram.InstaUser;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IDataSource{
    @GET("users/self/")
    Single<InstaUser> getUserInfo(@Query("access_token") String accessToken);

    @GET("users/self/media/recent/")
    Single<InstaMedia> getUserMedia(@Query("access_token") String accessToken);
}
