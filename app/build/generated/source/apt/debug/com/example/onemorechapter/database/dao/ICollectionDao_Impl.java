package com.example.onemorechapter.database.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.RxRoom;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.onemorechapter.database.entities.Collection;
import io.reactivex.Flowable;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings("unchecked")
public final class ICollectionDao_Impl implements ICollectionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfCollection;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfCollection;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfCollection;

  public ICollectionDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCollection = new EntityInsertionAdapter<Collection>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `collections`(`collectionKey`,`name`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Collection value) {
        stmt.bindLong(1, value.collectionKey);
        if (value.name == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.name);
        }
      }
    };
    this.__deletionAdapterOfCollection = new EntityDeletionOrUpdateAdapter<Collection>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `collections` WHERE `collectionKey` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Collection value) {
        stmt.bindLong(1, value.collectionKey);
      }
    };
    this.__updateAdapterOfCollection = new EntityDeletionOrUpdateAdapter<Collection>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `collections` SET `collectionKey` = ?,`name` = ? WHERE `collectionKey` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Collection value) {
        stmt.bindLong(1, value.collectionKey);
        if (value.name == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.name);
        }
        stmt.bindLong(3, value.collectionKey);
      }
    };
  }

  @Override
  public void insert(List<Collection> collections) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfCollection.insert(collections);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(List<Collection> collections) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfCollection.handleMultiple(collections);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(List<Collection> collections) {
    __db.beginTransaction();
    try {
      __updateAdapterOfCollection.handleMultiple(collections);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Flowable<List<Collection>> getAll() {
    final String _sql = "SELECT * FROM collections";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return RxRoom.createFlowable(__db, new String[]{"collections"}, new Callable<List<Collection>>() {
      @Override
      public List<Collection> call() throws Exception {
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfCollectionKey = _cursor.getColumnIndexOrThrow("collectionKey");
          final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
          final List<Collection> _result = new ArrayList<Collection>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Collection _item;
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            _item = new Collection(_tmpName);
            _item.collectionKey = _cursor.getInt(_cursorIndexOfCollectionKey);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }
}
