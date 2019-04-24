package com.example.onemorechapter.model;

import android.os.Environment;

public class Constants {

    public static final String PICK_DIALOG = "pick";

    public static final String DATA_DIR = Environment.getDataDirectory().getAbsolutePath();

    public final static String CURRENT_BOOK = "current_book";

    public static final String CURRENT_PAGE = "currentPage";

    public static final String COLLECTIONS = "collections";

    public static final String BOOKS = "books";

    public static final String DATABASE_NAME = "booksAndCollections";

    public static final String TARGET_FRAGMENT = "fragment";

    public static final String START = "start";

    public static final String READING = "fragment_reading";

    public static final String FAVOURITE = "favourite";

    public static final String HAVE_READ = "haveRead";

    public static final String OTHERS = "others";

    public static final int REQUEST_CODE_OPEN_FILE = 2;

    public static final int REQUEST_CODE_PICK_FILE = 1;
}
