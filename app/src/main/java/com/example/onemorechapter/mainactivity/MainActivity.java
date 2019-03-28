package com.example.onemorechapter.mainactivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.onemorechapter.R;
import com.example.onemorechapter.StartFragment;
import com.example.onemorechapter.collections.BooksFragment;
import com.example.onemorechapter.collections.CollectionListFragment;
import com.example.onemorechapter.database.entities.Collection;
import com.example.onemorechapter.library.LibraryFragment;
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

import static com.example.onemorechapter.model.Constants.COLLECTIONS;
import static com.example.onemorechapter.model.Constants.CURRENT_DIR;
import static com.example.onemorechapter.model.Constants.LIBRARY;
import static com.example.onemorechapter.model.Constants.OTHERS;
import static com.example.onemorechapter.model.Constants.READING;
import static com.example.onemorechapter.model.Constants.START;
import static com.example.onemorechapter.model.Constants.STORAGE_DIR;
import static com.example.onemorechapter.model.Constants.TARGET_FRAGMENT;

public class MainActivity extends MvpActivity<IMainActivityView, MainActivityPresenter>
        implements NavigationView.OnNavigationItemSelectedListener,
        IMainActivityView{

    private String currentDir;
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
            currentDir = savedInstanceState.getString(CURRENT_DIR);
            targetFragment = savedInstanceState.getString(TARGET_FRAGMENT);

        }else {
            //TODO: actions for first launch
            currentDir = STORAGE_DIR;
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
        super.onSaveInstanceState(bundle);
        bundle.putString(CURRENT_DIR, currentDir);
        bundle.putString(TARGET_FRAGMENT, targetFragment);
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
                .commit();
        targetFragment = START;
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showReadingFragment(){
        ReadingFragment readingFragment = new ReadingFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, readingFragment)
                .commit();
        targetFragment = READING;
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showLibraryFragment(){
        LibraryFragment libraryFragment = new LibraryFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, libraryFragment)
                .commit();
        targetFragment = LIBRARY;
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showCollectionListFragment(){
        CollectionListFragment collectionFragment = new CollectionListFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, collectionFragment)
                .commit();
        targetFragment = OTHERS;
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showBooksFragment(Collection collection) {
        BooksFragment booksFragment = new BooksFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(COLLECTIONS, collection);
        fragmentManager = getSupportFragmentManager();
        booksFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, booksFragment)
                .commit();
        targetFragment = collection.name;
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void loadLastFragment(String targetFragment){
        getPresenter().loadLastFragment(targetFragment);
    }



    @Override
    public void onBackPressed() {
        androidx.fragment.app.Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.frame);
        if(fragment instanceof LibraryFragment) {
            getPresenter().onBackPressed(LIBRARY);
        }else
            getPresenter().onBackPressed("other");
    }

    @Override
    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void onBackPressedLibrary() {
        androidx.fragment.app.Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.frame);
        String curDir = ((LibraryFragment) fragment).getCurrentDir();
        if(curDir.split("/").length > 2){
            curDir = curDir.substring(0, curDir.lastIndexOf("/"));
            ((LibraryFragment) fragment).setCurrentDir(curDir);
        }

        else{
            Toast.makeText(this, "Root dir", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressedOthers() {

    }


}
