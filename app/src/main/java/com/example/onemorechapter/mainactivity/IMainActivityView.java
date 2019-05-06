package com.example.onemorechapter.mainactivity;
import com.example.onemorechapter.database.entities.Collection;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface IMainActivityView extends MvpView{

    void showStartFragment();

    void showReadingFragment();

    void showCollectionListFragment();

    void showBooksFragment(Collection collection);

    void showSettingFragment();

    void showInfoFragment();

    void setTitle(String title);

}
