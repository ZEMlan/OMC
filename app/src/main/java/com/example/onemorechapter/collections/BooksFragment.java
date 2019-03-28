package com.example.onemorechapter.collections;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.onemorechapter.R;
import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.database.entities.Collection;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.onemorechapter.model.Constants.BOOKS;
import static com.example.onemorechapter.model.Constants.COLLECTIONS;


public class BooksFragment extends MvpFragment<IBooksView, BooksPresenter>
                            implements IBooksView{
    private Collection collection;

    private BooksRecyclerAdapter adapter;
    private ArrayList<Book> books;


    public BooksFragment() {
        // Required empty public constructor
    }

    @NotNull
    @Override
    public BooksPresenter createPresenter() {
        return new BooksPresenter();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            collection = (Collection) getArguments().getSerializable(COLLECTIONS);
        }
        if(savedInstanceState != null)
            books = (ArrayList<Book>) savedInstanceState.getSerializable(BOOKS);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BOOKS, books);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.booksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new BooksRecyclerAdapter(collection, getPresenter());
        recyclerView.setAdapter(adapter);
        adapter.setBooks(books);


        if(books == null || books.isEmpty()){
            ImageView blank = view.findViewById(R.id.blank);
            blank.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_books, container, false);
    }


    @Override
    public void getBooks(ArrayList<Book> books) {
        this.books = books;
        adapter.setBooks(books);
        adapter.notifyDataSetChanged();
    }

}
