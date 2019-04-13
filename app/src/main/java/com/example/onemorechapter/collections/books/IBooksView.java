package com.example.onemorechapter.collections.books;

import com.example.onemorechapter.database.entities.Book;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.ArrayList;


interface IBooksView extends MvpView {

    void getBooks(ArrayList<Book> books);
}
