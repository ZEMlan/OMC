package com.example.onemorechapter.database.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.RxRoom;
import androidx.room.SharedSQLiteStatement;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.onemorechapter.database.entities.Collection;
import io.reactivex.Flowable;
import java.lang.Exception;
import java.lang.Integer;
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

  private final SharedSQLiteStatement __preparedStmtOfUpdateName;

  public ICollectionDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCollection = new EntityInsertionAdapter<Collection>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `collections`(`collectionKey`,`name`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Collection value) {
        if (value.getCollectionKey() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getCollectionKey());
        }
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
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
        if (value.getCollectionKey() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getCollectionKey());
        }
      }
    };
    this.__preparedStmtOfUpdateName = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE collections SET name=?, collectionKey=? WHERE collectionKey=?";
        return _query;
      }
    };
  }

  @Override
  public void insert(Collection collection) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfCollection.insert(collection);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(Collection collection) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfCollection.handle(collection);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateName(int oldKey, String newName, int newKey) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateName.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      if (newName == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, newName);
      }
      _argIndex = 2;
      _stmt.bindLong(_argIndex, newKey);
      _argIndex = 3;
      _stmt.bindLong(_argIndex, oldKey);
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateName.release(_stmt);
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
            final Integer _tmpCollectionKey;
            if (_cursor.isNull(_cursorIndexOfCollectionKey)) {
              _tmpCollectionKey = null;
            } else {
              _tmpCollectionKey = _cursor.getInt(_cursorIndexOfCollectionKey);
            }
            _item.setCollectionKey(_tmpCollectionKey);
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
