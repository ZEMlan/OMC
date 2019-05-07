package com.example.onemorechapter.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName="book_collection_join",
        primaryKeys={"collectionId", "bookId"},
        foreignKeys={
                @ForeignKey (
                        entity=Collection.class,
                        parentColumns="collectionKey",
                        childColumns="collectionId" ,
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
    @ColumnInfo
    @NonNull
    private Integer collectionId;
    @ColumnInfo
    @NonNull
    private Integer bookId;

    public BookCollectionJoin(int collectionId, int bookId) {
        this.collectionId = collectionId;
        this.bookId = bookId;
    }

    @NonNull
    public Integer getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(@NonNull Integer collectionId) {
        this.collectionId = collectionId;
    }

    @NonNull
    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(@NonNull Integer bookId) {
        this.bookId = bookId;
    }

}
