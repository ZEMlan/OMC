package com.example.onemorechapter.model;

import android.util.Log;

import com.example.onemorechapter.database.AppDatabase;
import com.example.onemorechapter.database.dao.IBookCollectionJoinDao;
import com.example.onemorechapter.database.dao.IBookDao;
import com.example.onemorechapter.database.dao.ICollectionDao;
import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.database.entities.BookCollectionJoin;
import com.example.onemorechapter.database.entities.Collection;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DataRepository {
    private static DataRepository sInstance;
    private AppDatabase db;

    private ICollectionDao collectionDao;
    private IBookDao bookDao;
    private IBookCollectionJoinDao bookCollectionJoinDao;


    private DataRepository(final AppDatabase database){
        db = database;
        collectionDao = db.collectionDao();
        bookCollectionJoinDao = db.bookCollectionJoinDao();
        bookDao = db.bookDao();
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }


    public void insertCollections(ArrayList<Collection> collections){
        Single.create((SingleEmitter<String> emitter) ->  {
            collectionDao.insert(collections);
            emitter.onSuccess("Ok");
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        Log.d("Single", "Success on thread " + Thread.currentThread().getName());
                    }


                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public void insertBooks(final List<Book> books){
        Single.create((SingleEmitter<String> emitter) ->  {
            bookDao.insert(books);
            emitter.onSuccess("Ok");
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        Log.d("Single", "Success on thread " + Thread.currentThread().getName());
                    }


                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public void insertBookCollectionJoin(List<BookCollectionJoin> joinList){
        Single.create((SingleEmitter<String> emitter) ->  {
            bookCollectionJoinDao.insert(joinList);
            emitter.onSuccess("Ok");
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        Log.d("Single", "Success on thread " + Thread.currentThread().getName());
                    }


                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * Get the list of books and collections from the database
     * and get notified when the data changes.
     */

    public Flowable<List<Collection>> getAllCollections() {
        return db.collectionDao().getAll();
    }

    public Flowable<List<Book>> loadBooksInCollection(final int collectionID) {
        return db.bookCollectionJoinDao().getBooksForCollection(collectionID);
    }

    public Flowable<List<Collection>> loadCollectionsForBook(final int bookId) {
        return db.bookCollectionJoinDao().getCollectionsForBook(bookId) ;
    }

    public boolean isExsist(){
        return db.getDatabaseCreated();
    }

}
