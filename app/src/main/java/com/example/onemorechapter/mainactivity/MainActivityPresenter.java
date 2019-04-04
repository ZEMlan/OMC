package com.example.onemorechapter.mainactivity;


import com.example.onemorechapter.R;
import com.example.onemorechapter.database.entities.Collection;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import androidx.annotation.NonNull;

import static com.example.onemorechapter.model.Constants.FAVOURITE;
import static com.example.onemorechapter.model.Constants.HAVE_READ;
import static com.example.onemorechapter.model.Constants.LIBRARY;
import static com.example.onemorechapter.model.Constants.OTHERS;
import static com.example.onemorechapter.model.Constants.READING;
import static com.example.onemorechapter.model.Constants.START;

class MainActivityPresenter extends MvpBasePresenter<IMainActivityView>
        implements MvpPresenter<IMainActivityView> {

    MainActivityPresenter(){

    }

     void onMenuItemSelected(int id){
        boolean isViewAttached = isViewAttached();
        if (id == R.id.nav_start){
            if(isViewAttached) {
                getView().showStartFragment();
                getView().setTitle("Home");
            }
        }
        if (id == R.id.nav_lib){
            if(isViewAttached) {
                getView().showLibraryFragment();
                getView().setTitle("Library");
            }
        }
        if (id == R.id.nav_reading){
            if(isViewAttached) {
                getView().showReadingFragment();
                getView().setTitle("Reading");
            }
        }
        if (id == R.id.nav_fav){
            if(isViewAttached) {
                getView().showBooksFragment(new Collection(FAVOURITE));
                getView().setTitle("Collections");
            }
        }
        if (id == R.id.nav_read){
            if(isViewAttached) {
                getView().showBooksFragment(new Collection(HAVE_READ));
                getView().setTitle("Collections");
            }
        }
        if(id == R.id.nav_other){
            if(isViewAttached) {
                getView().showCollectionListFragment();
                getView().setTitle("Collections");
            }
        }
    }

    void onBackPressed(String fragmentType){
        boolean isViewAttached = isViewAttached();
        switch (fragmentType) {
            default:
                if (isViewAttached)
                    getView().onBackPressedOthers();
                break;
        }
    }

    void loadLastFragment(@NonNull String fragment){
        boolean isViewAttached = isViewAttached();
        switch (fragment){
            case START:
                if(isViewAttached) {
                    getView().showStartFragment();
                    getView().setTitle("Home");
                }
                break;
            case LIBRARY:
                if(isViewAttached) {
                    getView().showLibraryFragment();
                    getView().setTitle("Library");
                }
                break;
            case READING:
                if(isViewAttached) {
                    getView().showReadingFragment();
                    getView().setTitle("Reading");
                }
                break;
            case OTHERS:
                if(isViewAttached) {
                    getView().showCollectionListFragment();
                    getView().setTitle("Collections");
                }
                break;
            default:
                if(isViewAttached){
                    getView().showBooksFragment(new Collection(fragment));
                    getView().setTitle(fragment);
                }
        }
    }
}
