package com.example.onemorechapter.controller;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.onemorechapter.R;
import com.example.onemorechapter.controller.fragments.CollectionFragment;
import com.example.onemorechapter.controller.fragments.LibraryFragment;
import com.example.onemorechapter.controller.fragments.ReadingFragment;
import com.example.onemorechapter.controller.fragments.StartFragment;
import com.example.onemorechapter.model.entities.Book;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final String CUR_DIR = "curDir";

    private File file;
    private FilenameFilter filter;

    private ArrayList<Book> books;

    private String currentDir = Environment.getRootDirectory().getAbsolutePath();

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        if (savedInstanceState != null){
            currentDir = savedInstanceState.getString(CUR_DIR);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        StartFragment firstFragment = new StartFragment();
        fragmentTransaction.replace(R.id.frame, firstFragment).commit();

        file = new File(currentDir);
        filter  = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String[] s = {".fb2", ".txt", ".doc", ".docx", ".pdf"};
                for (String i : s) {
                    if (name.contains(i) || !name.contains(".")) {
                        return true;
                    }
                }
                return false;
            }
        };
        books = Book.getBookArray(file.listFiles());
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(CUR_DIR, currentDir);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onBackPressed() {
        androidx.fragment.app.Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.frame);
        if (fragment instanceof LibraryFragment){
            String curDir = ((LibraryFragment) fragment).getCurrentDir();
            if(curDir.split("/").length > 2){
                curDir = curDir.substring(0, curDir.lastIndexOf("/"));
                ((LibraryFragment) fragment).setCurrentDir(curDir);
            }

            else{
                Toast.makeText(this, "Root dir", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        androidx.fragment.app.Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_start){
            setTitle("OneMoreChapter");
            fragment = new StartFragment();
        }
        if (id == R.id.nav_lib){
            setTitle("Library");
            fragment = LibraryFragment.newInstance(currentDir);
        }
        if (id == R.id.nav_reading){
            fragment = ReadingFragment.newInstance();
            setTitle("Now");
        }
        if (id == R.id.nav_fav){
            fragment = CollectionFragment.newInstance();
            setTitle("Favourite");
        }
        if (id == R.id.nav_1){
            fragment = CollectionFragment.newInstance();
            setTitle("Have read");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (fragment != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment).commit();
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;


    }
}
