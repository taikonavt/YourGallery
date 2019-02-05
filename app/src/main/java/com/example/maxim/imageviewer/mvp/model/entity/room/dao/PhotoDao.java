package com.example.maxim.imageviewer.mvp.model.entity.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.maxim.imageviewer.mvp.model.entity.room.RoomPhoto;

import java.util.List;

import io.reactivex.Flowable;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface PhotoDao {

    @Insert(onConflict = REPLACE)
    void insert(RoomPhoto photo);

    @Insert(onConflict = REPLACE)
    void insert(RoomPhoto... photos);

    @Insert(onConflict = REPLACE)
    void insert(List<RoomPhoto> photos);


    @Update
    void update(RoomPhoto photo);

    @Update
    void update(RoomPhoto... photos);

    @Update
    void update(List<RoomPhoto> photos);


    @Delete
    void delete(RoomPhoto photo);

    @Delete
    void delete(RoomPhoto... photos);

    @Delete
    void delete(List<RoomPhoto> photos);


    @Query("SELECT * FROM roomphoto")
    Flowable<List<RoomPhoto>> getAll();

    @Query("SELECT * FROM roomphoto WHERE url = :url LIMIT 1")
    RoomPhoto findByUrl(String url);
}
