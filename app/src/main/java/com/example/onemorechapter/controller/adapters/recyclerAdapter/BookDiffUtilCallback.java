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
