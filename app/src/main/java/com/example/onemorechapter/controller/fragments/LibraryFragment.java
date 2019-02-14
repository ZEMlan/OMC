package com.example.onemorechapter.controller.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.onemorechapter.controller.adapters.recyclerAdapter.CardRecyclerAdapter;
import com.example.onemorechapter.R;
import com.example.onemorechapter.model.entities.Book;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LibraryFragment extends androidx.fragment.app.Fragment {

    private static final String CURRENT_DIR = "currentDir";

    private String currentDir = Environment.getRootDirectory().getPath();
    private File file;
    private ArrayList<Book> books;
    private FilenameFilter filter;

    private RecyclerView recyclerView;
    private CardRecyclerAdapter infoAdapter;

    public LibraryFragment() {
        // Required empty public constructor
    }

    public static LibraryFragment newInstance(String curDir) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        args.putString(CURRENT_DIR, curDir);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentDir = getArguments().getString(CURRENT_DIR);
        }

        file = new File(currentDir);
        filter  = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String[] s = {".fb2", ".txt", ".doc", ".docx", ".pdf"};
                for (String i : s) {
                    if (name.contains(i)) {
                        return true;
                    }
                }
                return false;
            }
        };
        books = Book.getBookArray(file.listFiles());

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_library, container, false);
        recyclerView = layout.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        infoAdapter = new CardRecyclerAdapter(currentDir);
        recyclerView.setAdapter(infoAdapter);
        infoAdapter.updateList(books);
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        bundle.putString(CURRENT_DIR, currentDir);
    }

    public void onPause() {
        super.onPause();
    }


    public String getCurrentDir() {
        return infoAdapter.getCurrentDir();
    }

    public void setCurrentDir(String currentDir) {
        this.currentDir = currentDir;
        file = new File(currentDir);
        infoAdapter.setCurrentDir(currentDir);
    }
}
