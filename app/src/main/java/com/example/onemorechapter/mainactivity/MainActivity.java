package com.example.onemorechapter.mainactivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.onemorechapter.R;
import com.example.onemorechapter.StartFragment;
import com.example.onemorechapter.collections.books.BooksFragment;
import com.example.onemorechapter.collections.collectionlist.CollectionListFragment;
import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.database.entities.Collection;
import com.example.onemorechapter.model.App;
import com.example.onemorechapter.reading.ReadingFragment;
import com.google.android.material.navigation.NavigationView;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import static com.example.onemorechapter.model.Constants.BOOKS;
import static com.example.onemorechapter.model.Constants.COLLECTIONS;
import static com.example.onemorechapter.model.Constants.OTHERS;
import static com.example.onemorechapter.model.Constants.READING;
import static com.example.onemorechapter.model.Constants.START;
import static com.example.onemorechapter.model.Constants.TARGET_FRAGMENT;

public class MainActivity extends MvpActivity<IMainActivityView, MainActivityPresenter>
        implements NavigationView.OnNavigationItemSelectedListener,
        IMainActivityView{

    private String targetFragment;

    FragmentManager fragmentManager;

    DrawerLayout drawer;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        if (savedInstanceState != null){
            targetFragment = savedInstanceState.getString(TARGET_FRAGMENT);

        }else {
            //TODO: actions for first launch
            targetFragment = START;
        }

        initUI();
    }

    @Override protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @NonNull
    @Override
    public MainActivityPresenter createPresenter() {
         return new MainActivityPresenter();
    }


    @Override public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    public void onSaveInstanceState(@NotNull Bundle bundle) {
        bundle.putString(TARGET_FRAGMENT, targetFragment);
        super.onSaveInstanceState(bundle);
    }


    public void initUI(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        loadLastFragment(targetFragment);

    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        getPresenter().onMenuItemSelected(item.getItemId());
        return true;
    }


    @Override
    public void showStartFragment(){
        StartFragment firstFragment = new StartFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, firstFragment)
                .commitAllowingStateLoss();
        targetFragment = START;
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showReadingFragment(){
        ReadingFragment readingFragment = ReadingFragment
                .newInstance(App.getInstance().getCurrentBook(), App.getInstance().getCurrentPage());
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, readingFragment)
                .commitAllowingStateLoss();
        targetFragment = READING;
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showCollectionListFragment(){
        CollectionListFragment collectionFragment = new CollectionListFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, collectionFragment)
                .commitAllowingStateLoss();
        targetFragment = COLLECTIONS;
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showBooksFragment(Collection collection) {
        BooksFragment booksFragment = new BooksFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BOOKS, collection);
        fragmentManager = getSupportFragmentManager();
        booksFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, booksFragment)
                .commitAllowingStateLoss();
        targetFragment = BOOKS;
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void loadLastFragment(String targetFragment){
        getPresenter().loadLastFragment(targetFragment);
    }

    @Override
    public void onBackPressed() {
        if(targetFragment.equals(BOOKS)) {
            showCollectionListFragment();
        }
    }

    @Override
    public void setTitle(String title) {
        toolbar.setTitle(title);
    }



}
