package com.example.onemorechapter.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.onemorechapter.reading.ReadingPresenter;

public class TextDataSource extends PageKeyedDataSource<Long, String> {
    ReadingPresenter presenter;

    public TextDataSource(){
        presenter = new ReadingPresenter();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params,
                            @NonNull LoadInitialCallback<Long, String> callback) {

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, String> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, String> callback) {

    }

}
