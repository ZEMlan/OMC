package com.example.onemorechapter.database.dao;

import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.database.entities.BookCollectionJoin;
import com.example.onemorechapter.database.entities.Collection;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Flowable;

@Dao
public interface IBookCollectionJoinDao {
    @Insert
    void insert(List<BookCollectionJoin> collectionJoin);

    @Query("SELECT * FROM books INNER JOIN book_collection_join ON " +
            "books.bookKey=book_collection_join.bookId WHERE " +
            "book_collection_join.collectionId=:targetCollectionId")
    Flowable<List<Book>> getBooksForCollection(int targetCollectionId);

    @Query("SELECT * FROM collections INNER JOIN book_collection_join ON "+
    "collections.collectionKey=book_collection_join.collectionId WHERE "+
    "book_collection_join.bookId=:targetBookId")
    Flowable<List<Collection>> getCollectionsForBook(int targetBookId);

    @Delete
    void delet(List<BookCollectionJoin> bookCollectionJoins);
}