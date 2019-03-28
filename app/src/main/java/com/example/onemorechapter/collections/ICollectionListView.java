package com.example.onemorechapter.collections;

import com.example.onemorechapter.database.entities.Collection;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.ArrayList;

interface ICollectionListView extends MvpView {

    void showCollectionList(ArrayList<Collection> collections);

}
