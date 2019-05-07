package com.example.onemorechapter.database.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.RxRoom;
import androidx.room.SharedSQLiteStatement;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.database.entities.BookCollectionJoin;
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
public final class IBookCollectionJoinDao_Impl implements IBookCollectionJoinDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfBookCollectionJoin;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfBookCollectionJoin;

  private final SharedSQLiteStatement __preparedStmtOfUpdateName;

  public IBookCollectionJoinDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBookCollectionJoin = new EntityInsertionAdapter<BookCollectionJoin>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `book_collection_join`(`collectionId`,`bookId`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BookCollectionJoin value) {
        if (value.getCollectionId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getCollectionId());
        }
        if (value.getBookId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, value.getBookId());
        }
      }
    };
    this.__deletionAdapterOfBookCollectionJoin = new EntityDeletionOrUpdateAdapter<BookCollectionJoin>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `book_collection_join` WHERE `collectionId` = ? AND `bookId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BookCollectionJoin value) {
        if (value.getCollectionId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getCollectionId());
        }
        if (value.getBookId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, value.getBookId());
        }
      }
    };
    this.__preparedStmtOfUpdateName = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE book_collection_join SET collectionId=? WHERE collectionId=?";
        return _query;
      }
    };
  }

  @Override
  public void insert(BookCollectionJoin join) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfBookCollectionJoin.insert(join);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMany(List<BookCollectionJoin> collectionJoin) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfBookCollectionJoin.insert(collectionJoin);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteMany(List<BookCollectionJoin> bookCollectionJoins) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfBookCollectionJoin.handleMultiple(bookCollectionJoins);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(BookCollectionJoin join) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfBookCollectionJoin.handle(join);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateName(int oldNameHash, int newNameHash) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateName.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      _stmt.bindLong(_argIndex, newNameHash);
      _argIndex = 2;
      _stmt.bindLong(_argIndex, oldNameHash);
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateName.release(_stmt);
    }
  }

  @Override
  public Flowable<List<Book>> getBooksForCollection(int targetCollectionId) {
    final String _sql = "SELECT * FROM books INNER JOIN book_collection_join ON books.bookKey=book_collection_join.bookId WHERE book_collection_join.collectionId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, targetCollectionId);
    return RxRoom.createFlowable(__db, new String[]{"books","book_collection_join"}, new Callable<List<Book>>() {
      @Override
      public List<Book> call() throws Exception {
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfBookKey = _cursor.getColumnIndexOrThrow("bookKey");
          final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
          final int _cursorIndexOfUri = _cursor.getColumnIndexOrThrow("uri");
          final List<Book> _result = new ArrayList<Book>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Book _item;
            final Integer _tmpBookKey;
            if (_cursor.isNull(_cursorIndexOfBookKey)) {
              _tmpBookKey = null;
            } else {
              _tmpBookKey = _cursor.getInt(_cursorIndexOfBookKey);
            }
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            _item = new Book(_tmpBookKey,_tmpUri,_tmpName);
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
