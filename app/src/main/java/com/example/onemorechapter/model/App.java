package com.example.onemorechapter.model;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.example.onemorechapter.database.AppDatabase;
import androidx.documentfile.provider.DocumentFile;
import androidx.room.Room;

import static com.example.onemorechapter.model.Constants.CURRENT_BOOK;
import static com.example.onemorechapter.model.Constants.CURRENT_PAGE;
import static com.example.onemorechapter.model.Constants.DATABASE_NAME;

public class App extends Application {

    public static App instance;

    private AppDatabase database;

    DataRepository dataRepository;

    private DocumentFile currentBook;
    private int currentPage;

    SharedPreferences sPref;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        database =  Room.databaseBuilder(this,
                AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
        dataRepository = DataRepository.getInstance(database);

        sPref = getSharedPreferences(CURRENT_BOOK, Context.MODE_PRIVATE);

        initCurrentBookAndPage();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public DataRepository getDataRepository(){
        return dataRepository;
    }

    public DocumentFile getCurrentBook() {
        return currentBook;
    }

    public void setCurrentBook(DocumentFile currentBook) {
        this.currentBook = currentBook;

        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(CURRENT_BOOK, currentBook.getUri().toString());
        editor.apply();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;

        SharedPreferences.Editor editor = sPref.edit();
        editor.putInt(CURRENT_PAGE, currentPage);
        editor.apply();
    }

    private void initCurrentBookAndPage(){
        if (sPref.contains(CURRENT_BOOK))
            currentBook = DocumentFile.fromSingleUri(
                    this, Uri.parse(sPref.getString(CURRENT_BOOK, null)));
        if(sPref.contains(CURRENT_PAGE))
            currentPage = sPref.getInt(CURRENT_PAGE, 0);
    }

}
