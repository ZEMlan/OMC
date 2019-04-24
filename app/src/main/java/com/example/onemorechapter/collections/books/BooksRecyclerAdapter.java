package com.example.onemorechapter.collections.books;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onemorechapter.R;
import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.database.entities.Collection;
import com.example.onemorechapter.model.App;
import com.example.onemorechapter.reading.ReadingFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class BooksRecyclerAdapter extends RecyclerView.Adapter<BooksRecyclerAdapter.CardViewHolder> {

    private FragmentActivity activity;


    private ArrayList<Book> books;


    BooksRecyclerAdapter(Collection collection, BooksPresenter presenter) {
        presenter.loadCollection(collection);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.library_card_view_layout, parent, false);
        activity = (FragmentActivity) view.getContext();
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, final int position) {
        holder.bind(books.get(position));
        holder.itemView.setOnClickListener(v -> openBook(books.get(position)));
    }

    private void openBook(Book book) {
        ReadingFragment fragment = ReadingFragment.newInstance(
                DocumentFile.fromSingleUri(App.getInstance(), book.getUriAsUri()));

        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment).commit();
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }


    public void setBooks(ArrayList<Book> data) {
        books = data;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }


    class CardViewHolder extends RecyclerView.ViewHolder {
        private TextView bookFileName;


        CardViewHolder(View view) {
            super(view);
            bookFileName = view.findViewById(R.id.fileName);
        }

        void bind(final Book book) {
            bookFileName.setText(book.getName());
        }
    }

}