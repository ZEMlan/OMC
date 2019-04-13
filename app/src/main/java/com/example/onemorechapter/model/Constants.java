package com.example.onemorechapter.model;

import android.os.Environment;

public class Constants {
    public static final String CURRENT_DIR = "currentDir";

    public static final String ROOT_DIR = Environment.getRootDirectory().getAbsolutePath();

    public static final String STORAGE_DIR = "/storage";

    public final static String CURRENT_BOOK = "current_book";

    public static final String CURRENT_PAGE = "currentPage";

    public static final String COLLECTIONS = "collections";

    public static final String BOOKS = "books";

    public static final String DATABASE_NAME = "booksAndCollections";

    public static final String KEY_DATA = "state-data";

    public static final String TARGET_FRAGMENT = "fragment";

    public static final int PDF = 0;

    public static final int TXT = 1;

    public static final int FB2 = 2;

    public static final int EPUB = 3;

    public static final String START = "start";

    public static final String LIBRARY = "library";

    public static final String READING = "fragment_reading";

    public static final String FAVOURITE = "favourite";

    public static final String HAVE_READ = "haveRead";

    public static final String OTHERS = "others";

    public static final int REQUEST_CODE_PICK_MANY_FILES = 2;

    public static final int REQUEST_CODE_PICK_FILE = 1;
}
