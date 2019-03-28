package com.example.onemorechapter.library;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onemorechapter.R;
import com.example.onemorechapter.database.entities.Book;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.onemorechapter.model.Constants.CURRENT_DIR;
import static com.example.onemorechapter.model.Constants.STORAGE_DIR;

public class LibraryFragment
        extends MvpFragment<ILibraryView, LibraryPresenter>
        implements ILibraryView {

    private String currentDir;

    private LibraryRecyclerAdapter infoAdapter;

    private SharedPreferences preferences;

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CURRENT_DIR, currentDir);
        super.onSaveInstanceState(outState);
    }

    @NotNull
    @Override
    public LibraryPresenter createPresenter() {
        return new LibraryPresenter();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);

        if(savedInstanceState != null)
            currentDir = savedInstanceState.getString(CURRENT_DIR);
        else {// means that it's first call so we load saved currentDirectory
            preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            currentDir = preferences.getString(CURRENT_DIR, STORAGE_DIR);
        }

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.libraryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        infoAdapter = new LibraryRecyclerAdapter(currentDir, getPresenter());
        recyclerView.setAdapter(infoAdapter);
    }


    @Override
    public void showData(List<Book> books) {
        infoAdapter.setBooks(books);
    }

    @Override
    public String getCurrentDir() {
        currentDir = infoAdapter.currentDir;
        preferences.edit().putString(CURRENT_DIR, currentDir).apply();
        return currentDir;
    }

    @Override
    public void setCurrentDir(String curDir) {
        currentDir = curDir;
        preferences.edit().putString(CURRENT_DIR, currentDir).apply();
        if(infoAdapter != null)
            infoAdapter.setCurrentDir(currentDir);
    }


    @Override
    public void setFavList(int[] books) {
        infoAdapter.listFavoiurite = books;
    }

    @Override
    public void setReadList(int[] books) {
        infoAdapter.listRead = books;
    }
}
