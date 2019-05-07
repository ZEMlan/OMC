package com.example.onemorechapter.database.entities;

import android.net.Uri;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class Book implements Serializable {
    @PrimaryKey
    @NonNull
    private Integer bookKey;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String uri;


    public Book(@NonNull Integer bookKey, String uri, String name) {
        this.bookKey = bookKey;
        this.uri = uri;
        this.name = name;
    }

    @Ignore
    public Book(DocumentFile file) {
        name = file.getName();
        uri = file.getUri().toString();
        bookKey = uri.hashCode();
    }

    @NonNull
    public Integer getBookKey() {
        return bookKey;
    }

    public void setBookKey(@NonNull Integer bookKey) {
        this.bookKey = bookKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri(){
        return uri;
    }

    public String getType() {
        return name.substring(name.lastIndexOf('.'));
    }

    public Uri getUriAsUri() {
        return Uri.parse(uri);
    }


}
