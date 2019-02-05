package com.example.maxim.imageviewer.mvp.model.entity.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.maxim.imageviewer.mvp.model.entity.room.RoomUser;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Insert(onConflict = REPLACE)
    void insert(RoomUser user);

    @Update
    void update(RoomUser user);

    @Query("DELETE FROM roomuser")
    void resetUser();

    @Query("SELECT * FROM roomuser LIMIT 1")
    RoomUser getUser();
}
