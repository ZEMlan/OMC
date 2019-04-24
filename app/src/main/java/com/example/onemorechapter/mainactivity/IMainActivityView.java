package com.example.onemorechapter.mainactivity;
import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.database.entities.Collection;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.ArrayList;

public interface IMainActivityView extends MvpView{

    void loadLastFragment(String fragment);

    void showStartFragment();

    void showReadingFragment();

    void showCollectionListFragment();

    void showBooksFragment(Collection collection);

    void setTitle(String title);

}
