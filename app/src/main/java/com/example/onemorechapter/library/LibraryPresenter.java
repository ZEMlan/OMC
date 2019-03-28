package com.example.onemorechapter.library;

import android.util.Log;

import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.model.App;
import com.example.onemorechapter.model.DataRepository;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.onemorechapter.model.Constants.FAVOURITE;
import static com.example.onemorechapter.model.Constants.HAVE_READ;

class LibraryPresenter extends MvpBasePresenter<ILibraryView> {

    private final DataRepository repository;

    LibraryPresenter(){
        repository = App.getInstance().getDataRepository();
    }

     void loadBooks(final boolean pullToRefresh, final String curDir){
        Single.create((SingleEmitter<ArrayList<Book>> emitter) ->  {
            File mFile = new File(curDir);
            Log.d("Storage", "Can read: " +  Boolean.toString(mFile.canRead()));
            emitter.onSuccess(Book.getBookArray(mFile.listFiles()));
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<Book>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("Single", "Subscribe on thread " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onSuccess(ArrayList<Book> bookArrayList) {
                        if(isViewAttached()) {
                            getView().showData(bookArrayList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    void loadFavList(){
        repository.loadBooksInCollection(FAVOURITE.hashCode())
                .observeOn(AndroidSchedulers.mainThread())
                .map(list -> Book.getKeyArray(list))
                .subscribe(books -> {
                    if(isViewAttached()){
                        getView().setFavList(books);
                    }
                });
    }

    void loadReadList(){
        repository.loadBooksInCollection(HAVE_READ.hashCode())
                .observeOn(AndroidSchedulers.mainThread())
                .map(list -> Book.getKeyArray(list))
                .subscribe(books -> {
                    if(isViewAttached()){
                        getView().setFavList(books);
                    }
                });
    }
}
