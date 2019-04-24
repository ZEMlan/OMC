package com.example.onemorechapter.collections.collectionlist;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.onemorechapter.R;
import com.example.onemorechapter.database.entities.Collection;
import com.example.onemorechapter.model.App;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.onemorechapter.model.Constants.COLLECTIONS;
import static com.example.onemorechapter.model.Constants.FAVOURITE;
import static com.example.onemorechapter.model.Constants.HAVE_READ;


public class CollectionListFragment extends
        MvpFragment<ICollectionListView, CollectionListPresenter>
        implements ICollectionListView{

    private ArrayList<Collection> collections;

    private CollectionListRecyclerAdapter adapter;

    private ImageView blank;
    private RecyclerView recyclerView;
    private View layout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            collections = (ArrayList<Collection>) savedInstanceState.getSerializable(COLLECTIONS);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> createDialog().show());

        recyclerView = view.findViewById(R.id.collectionListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new CollectionListRecyclerAdapter(getPresenter());
        recyclerView.setAdapter(adapter);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(400);
        itemAnimator.setRemoveDuration(400);
        itemAnimator.setMoveDuration(400);

        recyclerView.setItemAnimator(itemAnimator);

        enableSwipeToDeleteAndUndo();

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
       layout = inflater.inflate(R.layout.fragment_collection_list, container, false);
       return layout;
    }



    @Override
    public void showCollectionList(ArrayList<Collection> collections) {
        if(collections != null){
            blank.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            this.collections = collections;
            adapter.setCollections(collections);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCollectionsUpdate() {
        blank.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.setCollections(collections);
        adapter.notifyDataSetChanged();
    }


    private Dialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        EditText name = view.findViewById(R.id.colName);
        builder.setView(view)
                .setPositiveButton("Создать", (dialog, id) -> {
                    Collection collection = new Collection(name.getText().toString());
                    collections.add(collection);
                    getPresenter().onCollectionAdd(collection);
                })
                .setNegativeButton("Отмена", (dialog, id) -> {
                });
        return builder.create();
    }

    private Dialog editDialog(Collection collection, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        EditText name = view.findViewById(R.id.colName);
        builder.setView(view)
                .setPositiveButton("Переименовать", (dialog, id) -> {
                    getPresenter().renameCollection(collection, name.getText().toString());
                    adapter.editItem(position, name.getText().toString());
                })
                .setNegativeButton("Отмена", (dialog, id) -> {
                    adapter.restoreItem(collection, position);
                    recyclerView.scrollToPosition(position);
                    getPresenter().addCollection(collection);
                });
        return builder.create();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteAndEditCallback swipeToDeleteAndEditCallback = new SwipeToDeleteAndEditCallback(App.getInstance()) {

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                final Collection item = adapter.getCollections().get(position);

                if(!item.getName().equals(FAVOURITE) && !item.getName().equals(HAVE_READ)) {
                    if (direction == ItemTouchHelper.LEFT) {
                        adapter.removeItem(position);
                        getPresenter().deleteCollection(item);

                        Snackbar snackbar = Snackbar
                                .make(layout, "Коллекция удалена успешно.", Snackbar.LENGTH_SHORT);
                        snackbar.setAction("ОТМЕНИТЬ", view -> {
                            adapter.restoreItem(item, position);
                            recyclerView.scrollToPosition(position);
                            getPresenter().addCollection(item);
                        });
                        snackbar.setActionTextColor(getResources().getColor(R.color.secondaryLightColor));
                        snackbar.show();
                    }
                    if (direction == ItemTouchHelper.RIGHT) {
                        editDialog(item, position).show();
                    }
                }else{
                    Snackbar snackbar = Snackbar.make(getView(), "Эта коллекция неизменяема", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    adapter.removeItem(position);
                    adapter.restoreItem(item, position);
                    recyclerView.scrollToPosition(position);
                }
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteAndEditCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


}
