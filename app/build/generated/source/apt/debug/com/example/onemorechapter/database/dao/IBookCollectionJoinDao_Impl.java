package com.example.onemorechapter.database.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.RxRoom;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.onemorechapter.database.entities.Book;
import com.example.onemorechapter.database.entities.BookCollectionJoin;
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
public final class IBookCollectionJoinDao_Impl implements IBookCollectionJoinDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfBookCollectionJoin;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfBookCollectionJoin;

  public IBookCollectionJoinDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBookCollectionJoin = new EntityInsertionAdapter<BookCollectionJoin>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `book_collection_join`(`collectionId`,`bookId`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BookCollectionJoin value) {
        stmt.bindLong(1, value.collectionId);
        stmt.bindLong(2, value.bookId);
      }
    };
    this.__deletionAdapterOfBookCollectionJoin = new EntityDeletionOrUpdateAdapter<BookCollectionJoin>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `book_collection_join` WHERE `collectionId` = ? AND `bookId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BookCollectionJoin value) {
        stmt.bindLong(1, value.collectionId);
        stmt.bindLong(2, value.bookId);
      }
    };
  }

  @Override
  public void insert(List<BookCollectionJoin> collectionJoin) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfBookCollectionJoin.insert(collectionJoin);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delet(List<BookCollectionJoin> bookCollectionJoins) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfBookCollectionJoin.handleMultiple(bookCollectionJoins);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
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
          final int _cursorIndexOfPath = _cursor.getColumnIndexOrThrow("path");
          final List<Book> _result = new ArrayList<Book>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Book _item;
            _item = new Book();
            _item.bookKey = _cursor.getInt(_cursorIndexOfBookKey);
            _item.path = _cursor.getString(_cursorIndexOfPath);
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

  @Override
  public Flowable<List<Collection>> getCollectionsForBook(int targetBookId) {
    final String _sql = "SELECT * FROM collections INNER JOIN book_collection_join ON collections.collectionKey=book_collection_join.collectionId WHERE book_collection_join.bookId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, targetBookId);
    return RxRoom.createFlowable(__db, new String[]{"collections","book_collection_join"}, new Callable<List<Collection>>() {
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
