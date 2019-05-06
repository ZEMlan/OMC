package com.example.onemorechapter.database.dao;

import com.example.onemorechapter.database.entities.Collection;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Flowable;

@Dao
public interface ICollectionDao {
    @Query("SELECT * FROM collections")
    Flowable<List<Collection>> getAll();

    @Query("UPDATE collections SET name=:newName, collectionKey=:newKey WHERE collectionKey=:oldKey")
    void updateName(int oldKey, String newName, int newKey);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Collection collection);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMany(List<Collection> collections);

    @Delete
    void deleteMany(List<Collection> collections);

    @Delete
    void delete(Collection collection);
}
