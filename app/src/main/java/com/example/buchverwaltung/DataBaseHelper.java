package com.example.buchverwaltung;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String BOOK = "book";
    public static final String BOOK_ID = "book_id";
    public static final String AUTHOR = "author";
    public static final String ISBN = "isbn";
    public static final String TITLE = "title";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "book.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + BOOK + " (" + BOOK_ID + " Integer PRIMARY KEY AUTOINCREMENT, " + ISBN + " String, " + TITLE + " String, " + AUTHOR + " String)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addBook(Book book) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ISBN, book.getIsbn());
        cv.put(TITLE, book.getTitle());

        long insert = db.insert(BOOK, null, cv);
        if (insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public List<Book> getAllBooks() {
        List<Book> returnList = new ArrayList<>();

        // get data from database
        String queryString = "SELECT * FROM " + BOOK;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            // loop through the cursor and create new book object
            do {
                int bookId = cursor.getInt(0);
                String bookIsbn = cursor.getString(1);
                String bookTitle = cursor.getString(2);
                //boolean bookFavourite = cursor.getInt(3) == 1 ? true : false;

                Book newBook = new Book(bookTitle, bookIsbn);
                returnList.add(newBook);
            } while (cursor.moveToNext());
        }
        else {
            // failure
        }

        // close cursor and db
        cursor.close();
        db.close();
        return returnList;
    }

}
