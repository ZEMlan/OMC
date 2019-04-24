package com.example.onemorechapter.collections.books;

import android.annotation.SuppressLint;
import android.net.Uri;

import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.database.entities.BookCollectionJoin;
import com.example.onemorechapter.database.entities.Collection;
import com.example.onemorechapter.model.App;
import com.example.onemorechapter.model.DataRepository;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.documentfile.provider.DocumentFile;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.observers.ConsumerSingleObserver;

class BooksPresenter extends MvpBasePresenter<IBooksView> {

    private final DataRepository repository;

    BooksPresenter(){
        repository = App.getInstance().getDataRepository();
    }


    @SuppressLint("CheckResult")
    void loadCollection(Collection collection){
       repository.loadBooksInCollection(collection.getCollectionKey())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(books -> {
                   if(isViewAttached()){
                       getView().getBooks((ArrayList<Book>) books);
                   }
               });
    }

    void addNewSelectedBook(Uri uri, Collection collection){
        Book book = new Book(Objects.requireNonNull(
                DocumentFile.fromSingleUri(App.getInstance(), uri)));
        repository.insertBook(book);
        repository.insertCollection(collection);
        repository.insertBookCollectionJoin(
                new BookCollectionJoin(collection.getCollectionKey(), book.getBookKey()));
    }

    void deleteBookFromCollection(Book book, Collection collection){
        repository.delete(new BookCollectionJoin(collection.getCollectionKey(), book.getBookKey()));
    }

    void addBookToCollection(Book book, Collection collection){
        repository.insertBookCollectionJoin(new BookCollectionJoin(collection.getCollectionKey(), book.getBookKey()));
    }
}
