package com.example.onemorechapter.collections;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.onemorechapter.R;
import com.example.onemorechapter.database.entities.Collection;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.onemorechapter.model.Constants.COLLECTIONS;


public class CollectionListFragment extends
        MvpFragment<ICollectionListView, CollectionListPresenter>
        implements ICollectionListView{

    private ArrayList<Collection> collections;

    private CollectionListRecyclerAdapter adapter;

    private ImageView blank;
    private RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            collections = (ArrayList<Collection>) savedInstanceState.getSerializable(COLLECTIONS);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.collectionListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new CollectionListRecyclerAdapter(getPresenter());
        recyclerView.setAdapter(adapter);

        if(collections == null || collections.isEmpty()){
            blank = view.findViewById(R.id.blank);
            blank.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(COLLECTIONS, collections);
    }

    public CollectionListFragment() {
        // Required empty public constructor
    }

    @NotNull
    @Override
    public CollectionListPresenter createPresenter() {
        return new CollectionListPresenter();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collection_list, container, false);
    }



    @Override
    public void showCollectionList(ArrayList<Collection> collections) {
        if(this.collections == null || this.collections.isEmpty()){
            blank.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }else {
            this.collections = collections;
            adapter.setCollections(collections);
            adapter.notifyDataSetChanged();
        }
    }


}
