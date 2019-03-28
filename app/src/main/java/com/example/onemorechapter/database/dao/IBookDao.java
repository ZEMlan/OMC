package com.example.onemorechapter.database.dao;


import com.example.onemorechapter.database.entities.Book;

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

    //maybe not useful
    @Query("SELECT EXISTS (SELECT * FROM books WHERE bookKey = :bookId LIMIT 1)")
    boolean isExist(int bookId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<Book> books);

    @Delete
    void delete(List<Book> books);
}
