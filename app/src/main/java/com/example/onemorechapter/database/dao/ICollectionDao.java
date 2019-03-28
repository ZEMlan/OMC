package com.example.onemorechapter.database.dao;

import com.example.onemorechapter.database.entities.Collection;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Flowable;

@Dao
public interface ICollectionDao {
    @Query("SELECT * FROM collections")
    Flowable<List<Collection>> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<Collection> collections);

    @Delete
    void delete(List<Collection> collections);

    @Update
    void update(List<Collection> collections);
}
