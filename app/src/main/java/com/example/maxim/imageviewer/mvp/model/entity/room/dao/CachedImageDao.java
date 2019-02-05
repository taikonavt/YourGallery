package com.example.maxim.imageviewer.mvp.model.entity.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.maxim.imageviewer.mvp.model.entity.room.RoomCachedImage;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CachedImageDao {

    @Insert(onConflict = REPLACE)
    void insert(RoomCachedImage image);

    @Update
    void update(RoomCachedImage image);

    @Delete
    void delete(RoomCachedImage image);

    @Query("SELECT * FROM roomcachedimage WHERE url = :url LIMIT 1")
    RoomCachedImage getCachedImageByUrl(String url);

}
