package com.example.onemorechapter.database.dao;


import com.example.onemorechapter.database.entities.Book;
import com.google.android.material.snackbar.BaseTransientBottomBar;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Flowable;

@Dao
public interface IBookDao {
    @Query("SELECT * FROM books")
    Flowable<List<Book>> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMany(List<Book> books);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Book book);

    @Delete
    void delete(Book book);
}
