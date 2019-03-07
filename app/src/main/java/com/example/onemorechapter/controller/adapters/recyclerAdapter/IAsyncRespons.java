package com.example.onemorechapter.controller.adapters.recyclerAdapter;

import com.example.onemorechapter.model.entities.Book;

import java.util.ArrayList;

public interface IAsyncRespons {

    void processFinish(ArrayList<Book> output);
}
