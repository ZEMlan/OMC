package com.example.onemorechapter.collections;

import android.annotation.SuppressLint;

import com.example.onemorechapter.database.entities.Collection;
import com.example.onemorechapter.model.App;
import com.example.onemorechapter.model.DataRepository;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;

class CollectionListPresenter extends MvpBasePresenter<ICollectionListView> {

    private final DataRepository repository;

    CollectionListPresenter(){
        repository = App.getInstance().getDataRepository();
    }

    @SuppressLint("CheckResult")
    void loadCollectionList(){
        repository.getAllCollections()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(collections -> {
                    if(isViewAttached())
                        getView().showCollectionList((ArrayList<Collection>) collections);
                });
    }


}
