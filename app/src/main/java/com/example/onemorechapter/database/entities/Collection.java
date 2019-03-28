package com.example.onemorechapter.database.entities;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "collections")
public class Collection implements Serializable {
    @PrimaryKey
    public int collectionKey;
    @ColumnInfo (name = "name")
    public String name;

    public Collection(String name){
        this.name = name;
        collectionKey = name.hashCode();
    }

}
