package com.schoolpathram.schoolpathramdotcom.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.schoolpathram.schoolpathramdotcom.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCategory(Category category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllCategories(List<Category> categoryList);

    @Query("SELECT * FROM categories")
    List<Category> getAll();

    @Query("SELECT * FROM categories WHERE id=:catId")
    Category getCategory(String catId);


}