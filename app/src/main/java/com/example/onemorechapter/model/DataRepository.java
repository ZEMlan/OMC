package com.example.onemorechapter.model;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.onemorechapter.database.AppDatabase;
import com.example.onemorechapter.database.dao.IBookCollectionJoinDao;
import com.example.onemorechapter.database.dao.IBookDao;
import com.example.onemorechapter.database.dao.ICollectionDao;
import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.database.entities.BookCollectionJoin;
import com.example.onemorechapter.database.entities.Collection;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.onemorechapter.model.Constants.FAVOURITE;
import static com.example.onemorechapter.model.Constants.HAVE_READ;

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

        collectionDao.insert(new Collection(HAVE_READ));
        collectionDao.insert(new Collection(FAVOURITE));
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

    public void insertCollection(Collection collection) {
        Completable.create( emitter -> collectionDao.insert(collection))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void insertBooks(final List<Book> books){
        Completable.create( emitter -> bookDao.insertMany(books))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void insertBook(Book book) {
        Completable.create( emitter -> bookDao.insert(book))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void insertManyBookCollectionJoin(List<BookCollectionJoin> joinList){
        Completable.create( emitter -> bookCollectionJoinDao.insertMany(joinList))
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void insertBookCollectionJoin(BookCollectionJoin join) {
        Completable.create( emitter -> bookCollectionJoinDao.insert(join))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public Flowable<List<Collection>> getAllCollections() {
        return db.collectionDao().getAll();
    }

    public Flowable<List<Book>> loadBooksInCollection(final int collectionID) {
        return db.bookCollectionJoinDao().getBooksForCollection(collectionID);
    }

    public void deleteMany(List<BookCollectionJoin> joins){
        db.bookCollectionJoinDao().deleteMany(joins);
    }

    public void delete(BookCollectionJoin join) {
        db.bookCollectionJoinDao().delete(join);
    }

    public void delete(Collection collection) {
        db.collectionDao().delete(collection);
    }

    public void delete(Book book) {
        db.bookDao().delete(book);
    }

    public void renameCollection(Collection collection, String newName){
        collectionDao.updateName(collection.getName(), newName);
        bookCollectionJoinDao.updateName(collection.getCollectionKey(), newName.hashCode());
    }
}
