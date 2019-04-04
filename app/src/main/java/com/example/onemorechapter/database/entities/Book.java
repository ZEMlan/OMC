package com.example.onemorechapter.database.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    @Ignore
    private String name;
    @ColumnInfo(name = "path")
    private String path;
    @Ignore
    private long size;
    @Ignore
    private boolean isFavourite, isRead;
    @Ignore
    private DocumentFile documentFile;
    @Ignore
    private String type;

    public Book(@NonNull Integer bookKey, String path) {
        this.bookKey = bookKey;
        this.path = path;
    }

    @Ignore
    public Book(DocumentFile file) {
        name = file.getName();
        size = file.length()/8192; //file size in MB (1024*8)
        path = String.valueOf(file.getUri());
        bookKey = path.hashCode();
        documentFile = file;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
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


    static public ArrayList<Book> getBookArray(DocumentFile[] files){
        if(files == null){
            return new ArrayList<>();
        }
        ArrayList<Book> books = new ArrayList<>();
        for(DocumentFile i: files){
            books.add(new Book(i));
        }
        return books;
    }

    static public List<Integer> getKeyArray(List<Book> books){
        List<Integer> res = new ArrayList<>();
        if (books == null || books.isEmpty())
            return res;
        for(Book book: books){
            res.add(book.bookKey);
        }
        return res;
    }

    public DocumentFile getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(DocumentFile documentFile) {
        this.documentFile = documentFile;
    }

    public String getType() {
        return name.substring(name.indexOf("."));
    }
}
