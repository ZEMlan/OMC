package com.example.onemorechapter.model.entities;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {
    private String name,path;
    private long size;
    private boolean isFavourite, isRead;

    public String getName() {
        return name;
    }

    public String getType(){
        return name.substring(name.indexOf("."));
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Book(File file) {
        name = file.getName();
        size = file.lastModified();
        path = file.getPath();
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
}
