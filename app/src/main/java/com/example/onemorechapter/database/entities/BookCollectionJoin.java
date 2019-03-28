package com.example.onemorechapter.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName="book_collection_join",
        primaryKeys={"collectionId", "bookId"},
        foreignKeys={
                @ForeignKey(
                        entity=Collection.class,
                        parentColumns="collectionKey",
                        childColumns="collectionId",
                        onDelete=CASCADE),
                @ForeignKey(
                        entity= Book.class,
                        parentColumns="bookKey",
                        childColumns="bookId",
                        onDelete=CASCADE)},
        indices={
                @Index(value="collectionId"),
                @Index(value="bookId")
        }
)
public class BookCollectionJoin {
    public int collectionId;
    public int bookId;

    public BookCollectionJoin(int collectionId, int bookId) {
        this.collectionId = collectionId;
        this.bookId = bookId;
    }

    @Ignore
    public BookCollectionJoin(Collection collection, Book book){
        this.collectionId = collection.collectionKey;
        this.bookId = book.bookKey;
    }
}
