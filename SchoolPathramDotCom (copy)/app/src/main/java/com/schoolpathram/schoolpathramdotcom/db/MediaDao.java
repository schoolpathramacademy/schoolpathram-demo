package com.schoolpathram.schoolpathramdotcom.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.schoolpathram.schoolpathramdotcom.model.Media;

import java.util.List;

@Dao
public interface MediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMedia(Media media);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllMedia(List<Media> mediaList);

    @Query("SELECT * FROM media")
    List<Media> getAll();

    @Query("SELECT * FROM media WHERE id=:mediaId")
    Media getMedia(int mediaId);
}