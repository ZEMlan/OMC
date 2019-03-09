package com.example.onemorechapter.controller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onemorechapter.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

import static com.example.onemorechapter.model.Constants.*;


public class CollectionFragment extends androidx.fragment.app.Fragment {

    private static ArrayList<ArrayList> collections;

    public CollectionFragment() {
        // Required empty public constructor
    }

    public static CollectionFragment newInstance() {
        CollectionFragment fragment = new CollectionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(COLLECTIONS, collections);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null){
            collections = new ArrayList<>();
        } else {
            collections = (ArrayList<ArrayList>) getArguments().getSerializable(COLLECTIONS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if (collections == null){
            view = inflater.inflate(R.layout.collections_blank, container, false);
        }else {
            view = inflater.inflate(R.layout.fragment_collection, container, false);
        }
        return view;
    }

}
