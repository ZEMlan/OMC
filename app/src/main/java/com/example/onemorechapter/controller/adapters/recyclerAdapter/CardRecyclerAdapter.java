package com.example.onemorechapter.controller.adapters.recyclerAdapter;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.onemorechapter.R;
import com.example.onemorechapter.controller.fragments.ReadingFragment;
import com.example.onemorechapter.controller.Observables;
import com.example.onemorechapter.model.entities.Book;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.observers.BlockingMultiObserver;
import io.reactivex.internal.observers.SubscriberCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;

public class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.CardViewHolder> implements IAsyncRespons {

    FragmentActivity activity;

    private String currentDir;
    private FilenameFilter filter;
    private ArrayList<Book> books = new ArrayList<>();

    mAsyncTask getBooks;

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
        getBooks(new File(currentDir));
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

            if (new File(currentDir + "/" + fileName).isFile()) {
                Book book = books.get(position);
                if (book.getType().equals(".pdf")) {
                    openBook(books.get(position), v);
                } else {
                    Toast.makeText(v.getContext(), "File is opened", Toast.LENGTH_SHORT).show();
                }
            } else {
                currentDir = currentDir + "/" + fileName;
                getBooks(new File(currentDir));

            }
        });
    }

    private void updateList(ArrayList<Book> newList) {

        Observable.fromCallable(() -> DiffUtil.calculateDiff(new BookDiffUtilCallback(newList, books)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<DiffUtil.DiffResult>() {
                    DiffUtil.DiffResult result;

                    @Override
                    public void onNext(DiffUtil.DiffResult diffResult) {
                        result = diffResult;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        books.clear();
                        books.addAll(newList);
                        result.dispatchUpdatesTo(CardRecyclerAdapter.this);
                    }
                });
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Bundle bundle = (Bundle) payloads.get(0);
            Book book = books.get(position);
            for (String key : bundle.keySet()) {
                if (key.equals("isRead")) {
                    book.setRead(bundle.getBoolean("isRead"));
                }
                if (key.equals("isFavourite")) {
                    book.setFavourite(bundle.getBoolean("isFavourite"));
                }
            }
            holder.bind(book);
        }
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

    public ArrayList<Book> getBooks(){
        return books;
    }

    public String getCurrentDir() {
        return currentDir;
    }

    public void setCurrentDir(String curDir) {
        currentDir = curDir;
        getBooks(new File(curDir));
    }

    private void getBooks(File file){
        getBooks = new mAsyncTask();
        getBooks.delegate = this;
        getBooks.execute(file);
    }

    @Override
    public void processFinish(ArrayList<Book> output) {
        updateList(output);
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

        public void bind(final Book book) {

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
