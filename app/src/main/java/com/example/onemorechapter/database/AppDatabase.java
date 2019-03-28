package com.example.onemorechapter.database;

import android.content.Context;

import com.example.onemorechapter.database.dao.IBookCollectionJoinDao;
import com.example.onemorechapter.database.dao.IBookDao;
import com.example.onemorechapter.database.dao.ICollectionDao;
import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.database.entities.BookCollectionJoin;
import com.example.onemorechapter.database.entities.Collection;

import java.util.List;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import static com.example.onemorechapter.model.Constants.DATABASE_NAME;

@Database(entities = {Book.class, Collection.class, BookCollectionJoin.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private boolean mIsDatabaseCreated = false;


    public abstract IBookDao bookDao();
    public abstract ICollectionDao collectionDao();
    public abstract IBookCollectionJoinDao bookCollectionJoinDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .build();
                    INSTANCE.updateDatabaseCreated(context);
                }
            }
        }
        return INSTANCE;
    }

    private static void insertData(final AppDatabase database, final List<Book> books,
                                   final List<Collection> collections,
                                   final List<BookCollectionJoin> joinList) {
        database.runInTransaction(() -> {
            database.bookDao().insert(books);
            database.collectionDao().insert(collections);
            database.bookCollectionJoinDao().insert(joinList);
        });
    }

    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated = true;
    }

    public Boolean getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}
