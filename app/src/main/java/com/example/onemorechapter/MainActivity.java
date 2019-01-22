package com.example.onemorechapter;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fileex.FileEx;
import com.example.onemorechapter.Fragments.CollectionFragment;
import com.example.onemorechapter.Fragments.LibraryFragment;
import com.example.onemorechapter.Fragments.ReadingFragment;
import com.example.onemorechapter.Fragments.StartFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    public FileEx fileEx;

    private List<String> files;

    private String currentDir = Environment.getRootDirectory().getAbsolutePath();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

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

        StartFragment firstFragment = new StartFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame, firstFragment).commit();
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString("currentDir", currentDir);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, fragment).commit();
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;


    }
}
