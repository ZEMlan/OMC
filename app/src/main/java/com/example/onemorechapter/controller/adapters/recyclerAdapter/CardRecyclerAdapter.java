package com.example.onemorechapter.controller.adapters.recyclerAdapter;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.onemorechapter.R;
import com.example.onemorechapter.model.entities.Book;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.CardViewHolder> {
    private RecyclerView.ViewHolder holder;
    private File file;
    private String currentDir = Environment.getRootDirectory().getAbsolutePath();
    private FilenameFilter filter;
    private ArrayList<Book> books = new ArrayList<Book>();

    public CardRecyclerAdapter(String currentDir){
        this.currentDir = currentDir;

        file = new File(currentDir);
        filter  = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String[] s = {".fb2", ".txt", ".doc", ".docx", ".pdf"};
                for (String i : s) {
                    if (name.contains(i) || !name.contains(".")) {
                        return true;
                    }
                }
                return false;
            }
        };
        books.addAll(Book.getBookArray(file.listFiles()));
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.library_card_view_layout, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(books.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView fileText = v.findViewById(R.id.fileName);
                String fileName = fileText.getText().toString();
                if(new File(currentDir + "/" + fileName).isFile()){
                    Toast.makeText(v.getContext(), "File is opened",Toast.LENGTH_SHORT).show();
                }
                else {
                    currentDir = currentDir + "/" + fileName;
                    file = new File(currentDir);
                    updateList(Book.getBookArray(file.listFiles()));
                }
            }
        });
        this.holder = holder;
    }

        @Override
        public void onBindViewHolder(@NonNull CardViewHolder holder, int position, List<Object> payloads) {
            if(payloads.isEmpty()){
                super.onBindViewHolder(holder, position, payloads);
            }else{
                Bundle bundle = (Bundle)payloads.get(0);
                Book book = books.get(position);
                for(String key: bundle.keySet()){
                    if(key.equals("isRead")){
                        book.setRead(bundle.getBoolean("isRead"));
                    }
                    if(key.equals("isFavourite")){
                        book.setFavourite(bundle.getBoolean("isFavourite"));
                    }
                }
                holder.bind(book);
            }
        }

    public void updateList(ArrayList<Book> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new BookDiffUtilCallback(newList, books));
        books.clear();
        books.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return books == null ? 0 : books.size();
    }

    public String getCurrentDir(){
        return currentDir;
    }

    public void setCurrentDir(String curDir){
        currentDir = curDir;
        file = new File(curDir);
        updateList(Book.getBookArray(file.listFiles()));
    }

    class CardViewHolder extends RecyclerView.ViewHolder{
        private TextView bookFileName;
        private androidx.appcompat.widget.AppCompatImageButton butFav, butIsRead;

        private int butFavState = 0, butIsReadState = 0;

        CardViewHolder(View view){
            super(view);
            bookFileName = view.findViewById(R.id.fileName);
            butFav = view.findViewById(R.id.buttonFav);
            butIsRead = view.findViewById(R.id.buttonIsRead);

        }

        public void bind(final Book book){

            bookFileName.setText(book.getName());

            if(book.isRead()){
                butIsRead.setImageResource(R.drawable.check_all);
            }else{
                butIsRead.setImageResource(R.drawable.check);
            }

            if(book.isFavourite()){
                butFav.setImageResource(R.drawable.heart_multiple);
            }else{
                butFav.setImageResource(R.drawable.heart);
            }

            butFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!book.isFavourite()){
                        butFav.setImageResource(R.drawable.heart_multiple);
                        book.setFavourite(true);
                    }
                    else{
                        butFav.setImageResource(R.drawable.heart);
                        book.setFavourite(false);
                    }
                }
            });
            butIsRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!book.isRead()){
                        butIsRead.setImageResource(R.drawable.check_all);
                        book.setRead(true);
                    }
                    else{
                        butIsRead.setImageResource(R.drawable.check);
                        book.setRead(false);
                    }
                }
            });
        }
    }

}
