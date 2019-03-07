package com.example.onemorechapter.controller.adapters.recyclerAdapter;

import android.os.AsyncTask;
import android.os.StrictMode;

import com.example.onemorechapter.model.entities.Book;

import java.io.File;
import java.util.ArrayList;

public class mAsyncTask extends AsyncTask<File, Void, ArrayList<Book>> {
    IAsyncRespons delegate = null;

    @Override
    protected ArrayList<Book> doInBackground(File... files) {
        if(files.length != 0){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            File file = files[0];
            return Book.getBookArray(file.listFiles());
        }else
            return new ArrayList<>();

    }

    @Override
    protected void onPostExecute(ArrayList<Book> result) {
        delegate.processFinish(result);
    }
}
