package com.example.onemorechapter.controller.adapters.recyclerAdapter;

import android.os.Bundle;

import com.example.onemorechapter.model.entities.Book;

import java.util.ArrayList;

import androidx.recyclerview.widget.DiffUtil;
import io.reactivex.annotations.Nullable;

public class BookDiffUtilCallback extends DiffUtil.Callback {
    ArrayList<Book> newList;
    ArrayList<Book> oldList;

    public BookDiffUtilCallback(ArrayList<Book> newList, ArrayList<Book> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition).getPath()
                .equals(oldList.get(oldItemPosition).getPath());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }


    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        Book newBook = newList.get(newItemPosition);
        Book oldBook = oldList.get(oldItemPosition);

        Bundle diff = new Bundle();

        if (newBook.isRead() != oldBook.isRead()) {
            diff.putBoolean("isRead", newBook.isRead());
        }
        if (newBook.isFavourite() != oldBook.isFavourite()){
            diff.putBoolean("isFavourite", newBook.isFavourite());
        }
        if (diff.size() == 0) {
            return null;
        }
        return diff;
    }
}

//стаф для переделки после

/**    private void fetchCurrentStorageDirectory(final String curDir) {
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
                        books = bookArrayList;
                        Log.d("Single", "Success on thread " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    private void getList(String curDir){
        Single.just(Book.getBookArray(new File(curDir).listFiles()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(bookArrayList -> books = bookArrayList)
                .doOnError(Throwable::printStackTrace)
                .subscribe();
    }


 }
 **/
