package com.example.onemorechapter.collections.books;

import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.database.entities.Collection;
import com.example.onemorechapter.model.App;
import com.example.onemorechapter.model.DataRepository;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;

class BooksPresenter extends MvpBasePresenter<IBooksView> {

    private final DataRepository repository;

    BooksPresenter(){
        repository = App.getInstance().getDataRepository();
    }

    void loadCollection(Collection collection){
       repository.loadBooksInCollection(collection.getCollectionKey())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(books -> {
                   if(isViewAttached()){
                       getView().getBooks((ArrayList<Book>) books);
                   }
               });
    }


}
