package com.example.onemorechapter.model;

import android.app.Application;

import com.example.onemorechapter.database.AppDatabase;

import androidx.room.Room;

import static com.example.onemorechapter.model.Constants.DATABASE_NAME;

public class App extends Application {

    public static App instance;

    private AppDatabase database;

    DataRepository dataRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database =  Room.databaseBuilder(this,
                AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
        dataRepository = DataRepository.getInstance(database);
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
}
