package com.example.onemorechapter.collections;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onemorechapter.R;
import com.example.onemorechapter.database.entities.Collection;
import com.example.onemorechapter.mainactivity.MainActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class CollectionListRecyclerAdapter
        extends RecyclerView.Adapter<CollectionListRecyclerAdapter.CardViewHolder> {

     private ArrayList<Collection> collections;
    private CollectionListPresenter presenter;

    private MainActivity activity;

    CollectionListRecyclerAdapter(CollectionListPresenter presenter){
        this.presenter = presenter;
    }


    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collection_card_view_layout, parent, false);
        activity = (MainActivity) view.getContext();
        return new CollectionListRecyclerAdapter.CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(collections.get(position));
        holder.itemView.setOnClickListener(v-> openCollection(collections.get(position)));
    }

    @Override
    public int getItemCount() {
        return collections == null ? 0 : collections.size();
    }

    public ArrayList<Collection> getCollections() {
        return collections;
    }

    public void setCollections(ArrayList<Collection> collections) {
        this.collections = collections;
    }

    private void openCollection(Collection collection){
       activity.showBooksFragment(collection);
    }


    class CardViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        CardViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.collectionName);
        }

        void bind(final Collection collection) {
            name.setText(collection.getName());
        }
    }
}

