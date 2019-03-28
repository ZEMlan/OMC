package com.example.onemorechapter.library;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onemorechapter.R;
import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.reading.ReadingFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class LibraryRecyclerAdapter extends RecyclerView.Adapter<LibraryRecyclerAdapter.CardViewHolder> {

    private FragmentActivity activity;


    public String currentDir;
    public ArrayList<Book> books = new ArrayList<>();
    //list of bookKey of favourite books
    public int[] listFavoiurite;
    //list of bookKey of read books
    public int[] listRead;

    private LibraryPresenter presenter;

    LibraryRecyclerAdapter(String currentDir, LibraryPresenter presenter) {
        this.currentDir = currentDir;
        this.presenter = presenter;
        loadLists();
        fetchBookList(currentDir);
    }

    private void loadLists() {
        presenter.loadFavList();
        presenter.loadReadList();
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

            if (new File(currentDir + "/" + fileName).isFile() && fileName.contains(".")) {
                openBook(books.get(position), v);
            } else {
                currentDir = currentDir + "/" + fileName;
                fetchBookList(currentDir);

            }
        });
    }

    private void fetchBookList(final String curDir) {
        presenter.loadBooks(false, curDir);
    }


    private void openBook(Book book, View view) {
        //TODO: checking book format
        ReadingFragment fragment = ReadingFragment.newInstance(book);
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


    void setCurrentDir(String curDir) {
        if (!currentDir.equals(curDir)) {
            currentDir = curDir;
            fetchBookList(curDir);
        }
    }

    public void setBooks(List<Book> books){
        this.books = (ArrayList<Book>) books;
        notifyDataSetChanged();
    }

    List<Book> getBooks() {
        return books;
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

            bookFileName.setText(book.name);

            if (book.isRead) {
                butIsRead.setImageResource(R.drawable.check_all);
            } else {
                butIsRead.setImageResource(R.drawable.check);
            }

            if (book.isFavourite) {
                butFav.setImageResource(R.drawable.heart_multiple);
            } else {
                butFav.setImageResource(R.drawable.heart);
            }

            butFav.setOnClickListener(v -> {
                if (!book.isFavourite) {
                    butFav.setImageResource(R.drawable.heart_multiple);
                    book.isFavourite = true;
                } else {
                    butFav.setImageResource(R.drawable.heart);
                    book.isFavourite = false;
                }
            });
            butIsRead.setOnClickListener(v -> {
                if (!book.isRead) {
                    butIsRead.setImageResource(R.drawable.check_all);
                    book.isRead = true;
                } else {
                    butIsRead.setImageResource(R.drawable.check);
                    book.isRead = false;
                }
            });
        }
    }

}
