package com.example.onemorechapter.controller.adapters.recyclerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.onemorechapter.R;
import com.example.onemorechapter.controller.fragments.ReadingFragment;
import com.example.onemorechapter.model.entities.Book;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.CardViewHolder> {

    private FragmentActivity activity;

    private String currentDir;
    private FilenameFilter filter;
    private ArrayList<Book> books = new ArrayList<>();

    public CardRecyclerAdapter(String currentDir) {
        this.currentDir = currentDir;
        filter = (dir, name) -> {
            String[] s = {".fb2", ".txt", ".doc", ".docx", ".pdf"};
            for (String i : s) {
                if (name.contains(i) || !name.contains(".")) {
                    return true;
                }
            }
            return false;
        };
        fetchBookList(currentDir);
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
                Book book = books.get(position);
                if (book.getType().equals(".pdf")) {
                    openBook(books.get(position), v);
                } else {
                    Toast.makeText(v.getContext(), "File is opened", Toast.LENGTH_SHORT).show();
                }
            } else {
                currentDir = currentDir + "/" + fileName;
                fetchBookList(currentDir);

            }
        });
    }

    private void fetchBookList(final String curDir) {
        Single.create((SingleEmitter<ArrayList<Book>> emitter) ->  {
            File mFile = new File(curDir);
            Log.d("Storage", "Can read: " +  Boolean.toString(mFile.canRead()));
            emitter.onSuccess(Book.getBookArray(mFile.listFiles()));
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<Book>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("Single", "Subscribe on thread " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onSuccess(ArrayList<Book> bookArrayList) {
                        books = bookArrayList;
                        notifyDataSetChanged();
                        Log.d("Single", "Success on thread " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }


    private boolean openBook(Book book, View view) {
        ReadingFragment fragment = ReadingFragment.newInstance(book);
        DrawerLayout drawer = view.findViewById(R.id.drawer_layout);

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }

    public String getCurrentDir() {
        return currentDir;
    }

    public void setCurrentDir(String curDir) {
        if (!currentDir.equals(curDir)) {
            currentDir = curDir;
            fetchBookList(curDir);
        }
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
