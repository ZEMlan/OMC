package com.example.onemorechapter.mainactivity;
import com.example.onemorechapter.database.entities.Collection;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface IMainActivityView extends MvpView{

    void loadLastFragment(String fragment);

    void showStartFragment();

    void showReadingFragment();

    void showLibraryFragment();

    void showCollectionListFragment();

    void showBooksFragment(Collection collection);

    void setTitle(String title);

    void onBackPressedLibrary();

    void onBackPressedOthers();
}
