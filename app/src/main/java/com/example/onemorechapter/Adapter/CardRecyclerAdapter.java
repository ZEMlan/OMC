package com.example.onemorechapter.Adapter;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fileex.FileEx;
import com.example.onemorechapter.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.CardViewHolder>
        implements View.OnClickListener{
    RecyclerView.ViewHolder holder;
    FileEx fileEx;
    String currentDir = Environment.getRootDirectory().getAbsolutePath();
    //String currentDir = "/mnt/sdcard/Download";
    public List<String> files = new ArrayList<>();

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.library_card_view_layout, parent, false);
        fileEx = FileEx.newFileManager(currentDir);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(files.get(position));
        holder.itemView.setOnClickListener(this);
        this.holder = holder;
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public void setItems(List<String> fileList) {
        files = fileList;
        notifyDataSetChanged();
    }

    public void onItemDissmiss(int position){
        files.remove(position);
        notifyItemRemoved(position);
    }

    public String getCurrentDir(){
        return currentDir;
    }

    public void setCurrentDir(String dir){
        currentDir = dir;
    }


    class CardViewHolder extends RecyclerView.ViewHolder{
        private TextView bookFileName;
        private ImageView imageFav, imageIsRead;

        CardViewHolder(View view){
            super(view);
            bookFileName = view.findViewById(R.id.fileName);
            imageFav = view.findViewById(R.id.imageFav);
            imageIsRead = view.findViewById(R.id.imageIsRead);

        }

        public void bind(String bookName){

            bookFileName.setText(bookName);

        }
    }

    @Override
    public void onClick(View v) {
        TextView file = v.findViewById(R.id.fileName);
        String filePath = file.getText().toString();
        if(fileEx.isFile(filePath)){
            Toast.makeText(v.getContext(), "File is opened",Toast.LENGTH_SHORT).show();
        }
        else {
            files = fileEx.openDir(filePath);
            currentDir = currentDir + "/" + filePath;
            fileEx.setCurrentDir(currentDir);
            notifyDataSetChanged();
        }
    }

}
