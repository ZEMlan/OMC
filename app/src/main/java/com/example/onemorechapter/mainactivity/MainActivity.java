package com.example.onemorechapter.mainactivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.onemorechapter.R;
import com.example.onemorechapter.start.StartFragment;
import com.example.onemorechapter.collections.books.BooksFragment;
import com.example.onemorechapter.collections.collectionlist.CollectionListFragment;
import com.example.onemorechapter.database.entities.Collection;
import com.example.onemorechapter.info.InfoFragment;
import com.example.onemorechapter.model.App;
import com.example.onemorechapter.reading.ReadingFragment;
import com.example.onemorechapter.settings.SettingFragment;
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
import static com.example.onemorechapter.model.Constants.READING;
import static com.example.onemorechapter.model.Constants.START;
import static com.example.onemorechapter.model.Constants.TARGET_FRAGMENT;

public class MainActivity extends MvpActivity<IMainActivityView, MainActivityPresenter>
        implements NavigationView.OnNavigationItemSelectedListener,
        IMainActivityView{

    private String lastFragment;

    FragmentManager fragmentManager;

    DrawerLayout drawer;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        if (savedInstanceState != null){
            lastFragment = savedInstanceState.getString(TARGET_FRAGMENT);
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
        bundle.putString(TARGET_FRAGMENT, lastFragment);
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
        getPresenter().loadLastFragment(lastFragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        getPresenter().onMenuItemSelected(item.getItemId());
        return true;
    }


    @Override
    public void showStartFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, new StartFragment())
                .commitAllowingStateLoss();
        lastFragment = START;
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
        lastFragment = READING;
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showCollectionListFragment(){
        fragmentManager.beginTransaction()
                .replace(R.id.frame, new CollectionListFragment())
                .commitAllowingStateLoss();
        lastFragment = COLLECTIONS;
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
        lastFragment = BOOKS;
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showSettingFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, new SettingFragment())
                .commitAllowingStateLoss();

        lastFragment = START;
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showInfoFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.frame, new InfoFragment())
                .commitAllowingStateLoss();

        lastFragment = START;
        drawer.closeDrawer(GravityCompat.START);
    }


    @Override
    public void onBackPressed() {
        if(lastFragment.equals(BOOKS)) {
            showCollectionListFragment();
        }
    }

    @Override
    public void setTitle(String title) {
        toolbar.setTitle(title);
    }



}
