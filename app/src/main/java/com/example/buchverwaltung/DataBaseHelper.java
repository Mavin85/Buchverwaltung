package com.example.buchverwaltung;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "book.db";

    // Table Book
    private static final String BOOK = "Book"; // Table name
    // Column names
    private static final String BOOK_ID = "book_id";
    private static final String ISBN = "isbn";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String FAVOURITE = "favourite";
    private static final String COVER = "cover";
    private static final String COMMENT_BOOK = "comment_book";

    // Table BorrowingProcess
    private static final String LENDING = "Lending"; // Table name
    // Column names
    private static final String LENDING_ID = "lending_id";
    private static final String LENDER = "lender";
    private static final String BEGINNING = "start";
    private static final String END = "planned_end";
    private static final String COMPLETED = "completed";
    private static final String COMMENT_LENDING = "comment_lending";

    private final SQLiteDatabase readableDb;
    private final SQLiteDatabase writableDb;

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        readableDb = this.getReadableDatabase();
        writableDb = this.getWritableDatabase();
    }

    // creates the db on the first launch of the app
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableBook = "CREATE TABLE " + BOOK + " (" +
                BOOK_ID + " Integer PRIMARY KEY AUTOINCREMENT, " +
                ISBN + " Text, " +
                TITLE + " Text, " +
                AUTHOR + " Text, " +
                COVER + " int, " +
                FAVOURITE + " int, " +
                COMMENT_BOOK + " Text)";
        db.execSQL(createTableBook);
        String createTableBorrowingProcess = "CREATE TABLE " + LENDING + " (" +
                LENDING_ID + " Integer PRIMARY KEY AUTOINCREMENT, " +
                BOOK_ID + " int, " +
                LENDER + " Text, " +
                BEGINNING + " Text, " +
                END + " Text, " +
                COMPLETED + " int, " +
                COMMENT_LENDING + " Text)";
        db.execSQL(createTableBorrowingProcess);
    }

    // upgrades the db if a new version of the app is installed
    // currently all the stored data will be lost after upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + LENDING);
        onCreate(db);
    }

    // converter

    // get book from cursor
    private Book getBookObject(Cursor cur) {
        return new Book(
                cur.getInt(cur.getColumnIndex(BOOK_ID)),
                cur.getString(cur.getColumnIndex(ISBN)),
                cur.getString(cur.getColumnIndex(TITLE)),
                cur.getString(cur.getColumnIndex(AUTHOR)),
                cur.getInt(cur.getColumnIndex(FAVOURITE)) == 1,
                cur.getInt(cur.getColumnIndex(COVER)),
                cur.getString(cur.getColumnIndex(COMMENT_BOOK)));
    }

    // get content values from book
    private ContentValues getBookContentValues(Book book) {
        ContentValues cv = new ContentValues();
        cv.put(ISBN, book.getIsbn());
        cv.put(TITLE, book.getTitle());
        cv.put(AUTHOR, book.getAuthor());
        cv.put(FAVOURITE, book.isFavourite() ? 1 : 0);
        cv.put(COVER, book.getCoverInt());
        cv.put(COMMENT_BOOK, book.getComment());
        return cv;
    }

    // get lending from cursor
    private Lending getLendingObject(Cursor cur) {
        return new Lending(
                cur.getInt(cur.getColumnIndex(LENDING_ID)),
                cur.getInt(cur.getColumnIndex(BOOK_ID)),
                cur.getString(cur.getColumnIndex(LENDER)),
                cur.getString(cur.getColumnIndex(BEGINNING)),
                cur.getString(cur.getColumnIndex(END)),
                cur.getInt(cur.getColumnIndex(COMPLETED)) == 1,
                cur.getString(cur.getColumnIndex(COMMENT_LENDING)));
    }

    // get content values from lending
    private ContentValues getLendingContentValues(Lending lending) {
        ContentValues cv = new ContentValues();
        cv.put(BOOK_ID, lending.getBook_id());
        cv.put(LENDER, lending.getLender());
        cv.put(BEGINNING, lending.getStart());
        cv.put(END, lending.getPlanned_end());
        cv.put(COMPLETED, lending.getIsBack() ? 1 : 0);
        cv.put(COMMENT_LENDING, lending.getComment());
        return cv;
    }


    // write

    // adds a book to the db
    public void addBook(Book book) {
        ContentValues cv = getBookContentValues(book);
        try {
            writableDb.insert(BOOK, null, cv);
        }
        catch(Exception e) {
            Log.e("db", "addBook: " + e.getMessage());
        }
    }

    // edits a book
    public void editBook(Book book) {
        int bookId = book.getId();
        ContentValues cv = getBookContentValues(book);
        try {
            writableDb.update(BOOK, cv, BOOK_ID + " = " + bookId, null);
        }
        catch(Exception e) {
            Log.e("db", "editBook: " + e.getMessage());
        }
    }

    // removes a book from the db
    public void remBook(int id) {
        String[] selectionArgs = new String[]{Integer.toString(id)};

        // first delete all lendings of the book
        String query = "SELECT * FROM " + LENDING + " WHERE " + BOOK_ID + " = ? ORDER BY " + LENDING_ID + " desc;";
        Cursor cur = writableDb.rawQuery(query, selectionArgs);
        try {
            if(cur.moveToFirst()) {
                do {
                    remLending(cur.getInt(cur.getColumnIndex(LENDING_ID)));
                } while (cur.moveToNext());
            }
        }
        catch(Exception e) {
            Log.e("db", "remBook,remLending: " + e.getMessage());
        }
        cur.close();

        // delete the book
        try {
            writableDb.delete(BOOK, BOOK_ID + " = ?", selectionArgs);
        }
        catch(Exception e) {
            Log.e("db", "remBook: " + e.getMessage());
        }
    }

    // adds a lending to the db
    public void addLending(Lending lending) {
        ContentValues cv = getLendingContentValues(lending);
        try {
            writableDb.insert(LENDING, null, cv);
        }
        catch(Exception e) {
            Log.e("db", "addLending: " + e.getMessage());
        }
    }

    // edits a lending
    public void editLending(Lending lending) {
        int lendingId = lending.getId();
        ContentValues cv = getLendingContentValues(lending);
        try {
            writableDb.update(LENDING, cv, LENDING_ID + " = " + lendingId, null);
        }
        catch(Exception e) {
            Log.e("db", "editLending: " + e.getMessage());
        }
    }

    // removes a lending from the db
    public void remLending(int id) {
        String[] selectionArgs = new String[]{Integer.toString(id)};
        try {
            writableDb.delete(LENDING, LENDING_ID + " = ?", selectionArgs);
        }
        catch(Exception e) {
            Log.e("db", "remLending: " + e.getMessage());
        }
    }


    // read

    // returns a list of books that are results of the query
    public List<Book> getBooks(String bookQuery, String[] selectionArgs) {
        List<Book> bookList = new ArrayList<>();
        Cursor cur = readableDb.rawQuery(bookQuery, selectionArgs);

        try {
            if(cur.moveToFirst()) {
                do {
                    bookList.add(getBookObject(cur));
                } while (cur.moveToNext());
            }
        }
        catch(Exception e) {
            Log.e("db", "getBooks: " + e.getMessage());
        }

        return bookList;
    }

    // returns one book by id
    public Book getBook(int bookId) {
        String query = "SELECT * FROM " + BOOK + " WHERE " + BOOK_ID + " = ?;";
        String[] selectionArgs = new String[]{Integer.toString(bookId)};
        return getBooks(query, selectionArgs).get(0);
    }

    // returns one book by title
    public Book getBookByTitle(String title) {
        String query = "SELECT * FROM " + BOOK + " WHERE " + TITLE + " = ?";
        String[] selectionArgs = new String[] {title};
        return getBooks(query, selectionArgs).get(0);
    }

    // returns all saved books
    public List<Book> getAllBooks() {
        return getBooks("SELECT * FROM " + BOOK, null);
    }

    // returns favourite books
    public List<Book> getFavouriteBooks() {
        return getBooks("SELECT * FROM " + BOOK + " WHERE " + FAVOURITE + " = 1;", null);
    }

    // returns lended books
    public List<Book> getLendedBooks() {
        return getBooks("SELECT * FROM " + LENDING + " l LEFT JOIN " + BOOK + " b ON b." + BOOK_ID + " = l." + BOOK_ID + " WHERE " + COMPLETED + " = 0;", null);
    }


    // returns a list of lendings that are results of the query
    public List<Lending> getLendings(String lendingQuery, String[] selectionArgs) {
        List<Lending> lendingList = new ArrayList<>();
        Cursor cur = readableDb.rawQuery(lendingQuery, selectionArgs);

        try {
            if(cur.moveToFirst()) {
                do {
                    lendingList.add(getLendingObject(cur));
                } while (cur.moveToNext());
            }
        }
        catch(Exception e) {
            Log.e("db", "getLendings: " + e.getMessage());
        }

        return lendingList;
    }

    // returns one lending by id
    public Lending getLending(int lendingId) {
        String query = "SELECT * FROM " + LENDING + " WHERE " + LENDING_ID + " = ?;";
        String[] selectionArgs = new String[]{Integer.toString(lendingId)};
        return  getLendings(query, selectionArgs).get(0);
    }

    // returns lendings for one book
    public List<Lending> getLendingsForBook(int bookId) {
        String query = "SELECT * FROM " + LENDING + " WHERE " + BOOK_ID + " = ? ORDER BY " + LENDING_ID + " asc;";
        String[] selectionArgs = new String[]{Integer.toString(bookId)};
        return  getLendings(query, selectionArgs);
    }

    // returns only active lendings for one book
    public List<Lending> getActiveLendingsForBook(int bookId) {
        String query = "SELECT * FROM " + LENDING + " WHERE " + BOOK_ID + " = ? AND " + COMPLETED + " = 0;";
        String[] selectionArgs = new String[]{Integer.toString(bookId)};
        return  getLendings(query, selectionArgs);
    }
}