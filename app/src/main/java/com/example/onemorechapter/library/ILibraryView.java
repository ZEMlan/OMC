package com.example.onemorechapter.library;

import com.example.onemorechapter.database.entities.Book;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

interface ILibraryView extends MvpView {

    void showData(List<Book> books);

    String getCurrentDir();

    void setCurrentDir(String curDir);

    void setFavList(int[] books);

    void setReadList(int[] books);
}
