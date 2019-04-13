package com.example.onemorechapter.collections.collectionlist;

import com.example.onemorechapter.database.entities.Collection;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.ArrayList;

interface ICollectionListView extends MvpView {

    void showCollectionList(ArrayList<Collection> collections);

    void onCollectionsUpdate();

}
