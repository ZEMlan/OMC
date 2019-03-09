package com.example.onemorechapter.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.onemorechapter.controller.adapters.recyclerAdapter.CardRecyclerAdapter;
import com.example.onemorechapter.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.onemorechapter.model.Constants.*;

public class LibraryFragment extends androidx.fragment.app.Fragment {
    private String currentDir;


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
        }else {
            currentDir = ROOT_DIR;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_library, container, false);
        RecyclerView recyclerView = layout.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        infoAdapter = new CardRecyclerAdapter(currentDir);
        recyclerView.setAdapter(infoAdapter);
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
        currentDir = infoAdapter.getCurrentDir();
        return currentDir;
    }

    public void setCurrentDir(String currentDir) {
        this.currentDir = currentDir;
        infoAdapter.setCurrentDir(currentDir);
    }

}
