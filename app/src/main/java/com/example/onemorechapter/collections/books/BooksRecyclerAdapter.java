package com.example.onemorechapter.collections.books;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onemorechapter.R;
import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.database.entities.Collection;
import com.example.onemorechapter.reading.ReadingFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class BooksRecyclerAdapter extends RecyclerView.Adapter<BooksRecyclerAdapter.CardViewHolder> {

    private FragmentActivity activity;

    private Collection collection;
    private ArrayList<Book> books;

    private BooksPresenter presenter;

    BooksRecyclerAdapter(Collection collection, BooksPresenter presenter) {
        this.collection = collection;
        this.presenter = presenter;
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
        holder.itemView.setOnClickListener(v -> {

            TextView fileText = v.findViewById(R.id.fileName);
            String fileName = fileText.getText().toString();

            openBook(books.get(position), v);
        });
    }



    private void openBook(Book book, View view) {
        //TODO: checking book format
        ReadingFragment fragment = new ReadingFragment();
        DrawerLayout drawer = view.findViewById(R.id.drawer_layout);

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment).commit();
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }

    Collection getCurrentCol() {
        return collection;
    }

    void setCurrentDir(Collection collection) {
        if (!collection.equals(this.collection)) {
            this.collection = collection;

        }
    }


    public void setBooks(ArrayList<Book> data) {
        books = data;
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        private TextView bookFileName;
        private androidx.appcompat.widget.AppCompatImageButton butFav, butIsRead;


        CardViewHolder(View view) {
            super(view);
            bookFileName = view.findViewById(R.id.fileName);
            butFav = view.findViewById(R.id.buttonFav);
            butIsRead = view.findViewById(R.id.buttonIsRead);

        }

        void bind(final Book book) {

            bookFileName.setText(book.getName());

            if (book.isRead()) {
                butIsRead.setImageResource(R.drawable.check_all);
            } else {
                butIsRead.setImageResource(R.drawable.check);
            }

            if (book.isFavourite()) {
                butFav.setImageResource(R.drawable.heart_multiple);
            } else {
                butFav.setImageResource(R.drawable.heart);
            }

            butFav.setOnClickListener(v -> {
                if (!book.isFavourite()) {
                    butFav.setImageResource(R.drawable.heart_multiple);
                    book.setFavourite(true);
                } else {
                    butFav.setImageResource(R.drawable.heart);
                    book.setFavourite(false);
                }
            });
            butIsRead.setOnClickListener(v -> {
                if (!book.isRead()) {
                    butIsRead.setImageResource(R.drawable.check_all);
                    book.setRead(true);
                } else {
                    butIsRead.setImageResource(R.drawable.check);
                    book.setRead(false);
                }
            });
        }
    }

}