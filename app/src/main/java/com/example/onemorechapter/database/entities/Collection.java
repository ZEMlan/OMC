package com.example.onemorechapter.database.entities;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "collections")
public class Collection implements Serializable {
    @PrimaryKey
    @NonNull
    private Integer collectionKey;
    @ColumnInfo (name = "name")
    @NonNull
    private String name;

    public Collection(String name){
        this.name = name;
        collectionKey = name.hashCode();
    }

    @NonNull
    public Integer getCollectionKey() {
        return collectionKey;
    }

    public void setCollectionKey(@NonNull Integer collectionKey) {
        this.collectionKey = collectionKey;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
