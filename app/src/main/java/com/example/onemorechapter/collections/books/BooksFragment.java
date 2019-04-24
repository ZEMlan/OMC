package com.example.onemorechapter.collections.books;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.onemorechapter.R;
import com.example.onemorechapter.collections.collectionlist.SwipeToDeleteAndEditCallback;
import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.database.entities.Collection;
import com.example.onemorechapter.model.App;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.onemorechapter.model.Constants.BOOKS;
import static com.example.onemorechapter.model.Constants.REQUEST_CODE_PICK_FILE;

public class BooksFragment extends MvpFragment<IBooksView, BooksPresenter>
                            implements IBooksView{
    private Collection collection;

    private BooksRecyclerAdapter adapter;
    private ArrayList<Book> books;

    private ImageView blank;
    private RecyclerView recyclerView;

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
            //means that we open this fragment at first
            collection = (Collection) getArguments().getSerializable(BOOKS);
        }

        if(savedInstanceState != null)
            //means that we've already fetch books in collection lately
            books = (ArrayList<Book>) savedInstanceState.getSerializable(BOOKS);

        books = new ArrayList<>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BOOKS, books);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.booksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new BooksRecyclerAdapter(collection, getPresenter());
        recyclerView.setAdapter(adapter);
        adapter.setBooks(books);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(400);
        itemAnimator.setRemoveDuration(400);
        itemAnimator.setMoveDuration(400);

        recyclerView.setItemAnimator(itemAnimator);

        enableSwipeToDelete();

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
        });

        if(books == null || books.isEmpty()){
            blank = view.findViewById(R.id.blank);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_PICK_FILE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri bookUri = data.getData();
                    getPresenter().addNewSelectedBook(bookUri, collection);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getBooks(ArrayList<Book> books){
        if(books != null){
            if(books.size() > 1) {
                this.books = books;
                blank.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }else if(books.isEmpty()){
                this.books = books;
                blank.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }else{
                this.books.add(books.get(0));
                blank.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            adapter.setBooks(books);
            adapter.notifyDataSetChanged();
        }
    }

    private void enableSwipeToDelete() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(App.getInstance()) {

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    final Book item = adapter.getBooks().get(position);

                    getPresenter().deleteBookFromCollection(item, collection);

                    Snackbar snackbar = Snackbar
                            .make(getView(), "Книга успешно удалена из коллекции.", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("ОТМЕНИТЬ", view -> getPresenter().addBookToCollection(item, collection));
                    snackbar.setActionTextColor(getResources().getColor(R.color.secondaryLightColor));
                    snackbar.show();
                }
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

}
