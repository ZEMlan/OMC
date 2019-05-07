package com.example.onemorechapter.mainactivity;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.onemorechapter.R;
import com.example.onemorechapter.database.entities.Collection;
import com.example.onemorechapter.model.App;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import androidx.annotation.Nullable;

import static com.example.onemorechapter.model.Constants.COLLECTIONS;
import static com.example.onemorechapter.model.Constants.FAVOURITE;
import static com.example.onemorechapter.model.Constants.HAVE_READ;
import static com.example.onemorechapter.model.Constants.READING;
import static com.example.onemorechapter.model.Constants.START;

class MainActivityPresenter extends MvpBasePresenter<IMainActivityView>
        implements MvpPresenter<IMainActivityView> {

    MainActivityPresenter(){
    }

     void onMenuItemSelected(int id){
        boolean isViewAttached = isViewAttached();
        switch (id) {
            case (R.id.nav_start):
                if (isViewAttached) {
                    getView().showStartFragment();
                    getView().setTitle("Home");
                }
            break;
            case (R.id.nav_reading):
                if (isViewAttached) {
                    getView().showReadingFragment();
                    getView().setTitle("Reading");
                }
            break;
            case (R.id.nav_fav):
                if (isViewAttached) {
                    getView().showBooksFragment(new Collection(FAVOURITE));
                    getView().setTitle("Collections");
                }
            break;
            case (R.id.nav_read):
                if (isViewAttached) {
                    getView().showBooksFragment(new Collection(HAVE_READ));
                    getView().setTitle("Collections");
                }
            break;
            case (R.id.nav_other):
                if (isViewAttached) {
                    getView().showCollectionListFragment();
                    getView().setTitle("Collections");
                }
            break;
            case (R.id.nav_setting):
                if (isViewAttached) {
                    getView().showSettingFragment();
                    getView().setTitle("Настройки");
                }
            break;
            case (R.id.nav_info):
                if(isViewAttached){
                    getView().showInfoFragment();
                    getView().setTitle("О приложении");
                }
            break;
        }
    }

    void loadLastFragment(@Nullable String fragment){
        boolean isViewAttached = isViewAttached();
        if(fragment == null){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
            boolean is_read = preferences.getBoolean("is_read", false);

            if(is_read && isViewAttached){
                getView().showReadingFragment();
            }else if (isViewAttached){
                getView().showStartFragment();
            }
        }else {
            switch (fragment) {
                case START:
                default:
                    if (isViewAttached) {
                        getView().showStartFragment();
                        getView().setTitle("Home");
                    }
                    break;
                case READING:
                    if (isViewAttached) {
                        getView().showReadingFragment();
                        getView().setTitle("Reading");
                    }
                    break;
                case COLLECTIONS:
                    if (isViewAttached) {
                        getView().showCollectionListFragment();
                        getView().setTitle("Collections");
                    }
                    break;
            }
        }

    }

}
