package com.example.onemorechapter.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fileex.FileEx;
import com.example.onemorechapter.Adapter.CardRecyclerAdapter;
import com.example.onemorechapter.R;

import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LibraryFragment extends androidx.fragment.app.Fragment {

    private static final String CURRENT_DIR = "currentDir";

    private String currentDir;

    public FileEx fileEx;

    private List<String> files;

    private RecyclerView recyclerView;
    CardRecyclerAdapter infoAdapter;

    public LibraryFragment() {
        // Required empty public constructor
    }

    public static LibraryFragment newInstance(String param1) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        args.putString(CURRENT_DIR, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            currentDir = getArguments().getString(CURRENT_DIR);
        }

        fileEx = FileEx.newFileManager(currentDir);
        files = fileEx.listFiles();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_library, container, false);
        recyclerView = layout.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        infoAdapter = new CardRecyclerAdapter();
        recyclerView.setAdapter(infoAdapter);
        infoAdapter.setItems(files);

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
        return fileEx.getCurrentDir();
    }

    public void setCurrentDir(String currentDir) {
        this.currentDir = currentDir;
        fileEx.setCurrentDir(currentDir);
        infoAdapter.setCurrentDir(currentDir);
        infoAdapter.setItems(fileEx.listFiles());
    }
}
