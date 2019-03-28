package com.example.onemorechapter.database.entities;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class Book implements Serializable {
    @PrimaryKey
    public int bookKey;
    @Ignore
    public String name;
    @ColumnInfo(name = "path")
    public String path;
    @Ignore
    public long size;
    @Ignore
    public boolean isFavourite, isRead;

    @Ignore
    private Book(File file) {
        name = file.getName();
        size = file.length()/8192; //file size in MB (1024*8)
        path = file.getPath();
        bookKey = path.hashCode();
    }

    public Book(){

    }

    static public ArrayList<Book> getBookArray(File[] files){
        if(files == null){
            return new ArrayList<>();
        }
        ArrayList<Book> books = new ArrayList<>();
        for(File i: files){
            books.add(new Book(i));
        }
        return books;
    }

    static public int[] getKeyArray(List<Book> books){
        if (books == null || books.isEmpty())
            return new int[0];
        else{
            int[] res = new int[books.size()];
            int i = 0;
            for(Book book: books){
                res[i] = book.bookKey;
                i ++;
            }
            return res;
        }
    }

}
