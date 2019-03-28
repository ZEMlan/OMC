package com.example.onemorechapter.database;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.example.onemorechapter.database.dao.IBookCollectionJoinDao;
import com.example.onemorechapter.database.dao.IBookCollectionJoinDao_Impl;
import com.example.onemorechapter.database.dao.IBookDao;
import com.example.onemorechapter.database.dao.IBookDao_Impl;
import com.example.onemorechapter.database.dao.ICollectionDao;
import com.example.onemorechapter.database.dao.ICollectionDao_Impl;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public final class AppDatabase_Impl extends AppDatabase {
  private volatile IBookDao _iBookDao;

  private volatile ICollectionDao _iCollectionDao;

  private volatile IBookCollectionJoinDao _iBookCollectionJoinDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `books` (`bookKey` INTEGER NOT NULL, `path` TEXT, PRIMARY KEY(`bookKey`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `collections` (`collectionKey` INTEGER NOT NULL, `name` TEXT, PRIMARY KEY(`collectionKey`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `book_collection_join` (`collectionId` INTEGER NOT NULL, `bookId` INTEGER NOT NULL, PRIMARY KEY(`collectionId`, `bookId`), FOREIGN KEY(`collectionId`) REFERENCES `collections`(`collectionKey`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`bookId`) REFERENCES `books`(`bookKey`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE  INDEX `index_book_collection_join_collectionId` ON `book_collection_join` (`collectionId`)");
        _db.execSQL("CREATE  INDEX `index_book_collection_join_bookId` ON `book_collection_join` (`bookId`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"b56893e14e53dbc44f7d43d47dbd5c88\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `books`");
        _db.execSQL("DROP TABLE IF EXISTS `collections`");
        _db.execSQL("DROP TABLE IF EXISTS `book_collection_join`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        _db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsBooks = new HashMap<String, TableInfo.Column>(2);
        _columnsBooks.put("bookKey", new TableInfo.Column("bookKey", "INTEGER", true, 1));
        _columnsBooks.put("path", new TableInfo.Column("path", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBooks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBooks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBooks = new TableInfo("books", _columnsBooks, _foreignKeysBooks, _indicesBooks);
        final TableInfo _existingBooks = TableInfo.read(_db, "books");
        if (! _infoBooks.equals(_existingBooks)) {
          throw new IllegalStateException("Migration didn't properly handle books(com.example.onemorechapter.database.entities.Book).\n"
                  + " Expected:\n" + _infoBooks + "\n"
                  + " Found:\n" + _existingBooks);
        }
        final HashMap<String, TableInfo.Column> _columnsCollections = new HashMap<String, TableInfo.Column>(2);
        _columnsCollections.put("collectionKey", new TableInfo.Column("collectionKey", "INTEGER", true, 1));
        _columnsCollections.put("name", new TableInfo.Column("name", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCollections = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCollections = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCollections = new TableInfo("collections", _columnsCollections, _foreignKeysCollections, _indicesCollections);
        final TableInfo _existingCollections = TableInfo.read(_db, "collections");
        if (! _infoCollections.equals(_existingCollections)) {
          throw new IllegalStateException("Migration didn't properly handle collections(com.example.onemorechapter.database.entities.Collection).\n"
                  + " Expected:\n" + _infoCollections + "\n"
                  + " Found:\n" + _existingCollections);
        }
        final HashMap<String, TableInfo.Column> _columnsBookCollectionJoin = new HashMap<String, TableInfo.Column>(2);
        _columnsBookCollectionJoin.put("collectionId", new TableInfo.Column("collectionId", "INTEGER", true, 1));
        _columnsBookCollectionJoin.put("bookId", new TableInfo.Column("bookId", "INTEGER", true, 2));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBookCollectionJoin = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysBookCollectionJoin.add(new TableInfo.ForeignKey("collections", "CASCADE", "NO ACTION",Arrays.asList("collectionId"), Arrays.asList("collectionKey")));
        _foreignKeysBookCollectionJoin.add(new TableInfo.ForeignKey("books", "CASCADE", "NO ACTION",Arrays.asList("bookId"), Arrays.asList("bookKey")));
        final HashSet<TableInfo.Index> _indicesBookCollectionJoin = new HashSet<TableInfo.Index>(2);
        _indicesBookCollectionJoin.add(new TableInfo.Index("index_book_collection_join_collectionId", false, Arrays.asList("collectionId")));
        _indicesBookCollectionJoin.add(new TableInfo.Index("index_book_collection_join_bookId", false, Arrays.asList("bookId")));
        final TableInfo _infoBookCollectionJoin = new TableInfo("book_collection_join", _columnsBookCollectionJoin, _foreignKeysBookCollectionJoin, _indicesBookCollectionJoin);
        final TableInfo _existingBookCollectionJoin = TableInfo.read(_db, "book_collection_join");
        if (! _infoBookCollectionJoin.equals(_existingBookCollectionJoin)) {
          throw new IllegalStateException("Migration didn't properly handle book_collection_join(com.example.onemorechapter.database.entities.BookCollectionJoin).\n"
                  + " Expected:\n" + _infoBookCollectionJoin + "\n"
                  + " Found:\n" + _existingBookCollectionJoin);
        }
      }
    }, "b56893e14e53dbc44f7d43d47dbd5c88", "54ecb994523fce1f952d8962fb0fa2a9");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "books","collections","book_collection_join");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `books`");
      _db.execSQL("DELETE FROM `collections`");
      _db.execSQL("DELETE FROM `book_collection_join`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public IBookDao bookDao() {
    if (_iBookDao != null) {
      return _iBookDao;
    } else {
      synchronized(this) {
        if(_iBookDao == null) {
          _iBookDao = new IBookDao_Impl(this);
        }
        return _iBookDao;
      }
    }
  }

  @Override
  public ICollectionDao collectionDao() {
    if (_iCollectionDao != null) {
      return _iCollectionDao;
    } else {
      synchronized(this) {
        if(_iCollectionDao == null) {
          _iCollectionDao = new ICollectionDao_Impl(this);
        }
        return _iCollectionDao;
      }
    }
  }

  @Override
  public IBookCollectionJoinDao bookCollectionJoinDao() {
    if (_iBookCollectionJoinDao != null) {
      return _iBookCollectionJoinDao;
    } else {
      synchronized(this) {
        if(_iBookCollectionJoinDao == null) {
          _iBookCollectionJoinDao = new IBookCollectionJoinDao_Impl(this);
        }
        return _iBookCollectionJoinDao;
      }
    }
  }
}
