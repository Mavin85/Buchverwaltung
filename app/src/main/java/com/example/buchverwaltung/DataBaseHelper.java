package com.example.buchverwaltung;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    // Table Book
    public static final String BOOK = "Book"; // Table name
    // Column names
    public static final String BOOK_ID = "book_id";
    public static final String ISBN = "isbn";
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String FAVOURITE = "favourite";
    public static final String COVER = "cover";
    public static final String COMMENT_BOOK = "comment_book";

    // Table BorrowingProcess
    public static final String BORROWING_PROCESS = "Borrowing_Process"; // Table name
    // Column names
    public static final String BORROWING_ID = "borrowing_id";
    public static final String BORROWER = "borrower";
    public static final String BEGINNING = "beginning";
    public static final String END = "ending";
    public static final String COMPLETED = "completed";
    public static final String COMMENT_BORROWING = "comment_borrowing";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "book.db", null, 1);
    }

    // creates the db on the first launch of the app
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableBook = "CREATE TABLE " + BOOK + " (" + BOOK_ID + " Integer PRIMARY KEY AUTOINCREMENT, " + ISBN + " String, " + TITLE + " String, " + AUTHOR + " String, " + COVER + " String, " + FAVOURITE + " int, " + COMMENT_BOOK + " String)";
        db.execSQL(createTableBook);
        String createTableBorrowingProcess = "CREATE TABLE " + BORROWING_PROCESS + " (" + BORROWING_ID + " Integer PRIMARY KEY AUTOINCREMENT, " + BOOK_ID + " int, " + BORROWER + " String, " + BEGINNING + " String, " + END + " String, " + COMPLETED + " int, " + COMMENT_BORROWING + " String)";
        db.execSQL(createTableBorrowingProcess);
    }

    // upgrades the db if a new version of the app is installed
    // currently all the stored data will be lost after upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("Upgrade", "Updating tables");

        db.execSQL("DROP TABLE IF EXISTS " + BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + BORROWING_PROCESS);
        onCreate(db);
    }

    // write

    // adds a book to the db
    public void addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ISBN, book.getIsbn());
        cv.put(TITLE, book.getTitle());
        cv.put(AUTHOR, book.getAuthor());
        cv.put(FAVOURITE, book.isFavourite() ? 1 : 0);
        cv.put(COVER, book.getCover());
        cv.put(COMMENT_BOOK, book.getComment());

        try {
            db.insert(BOOK, null, cv);
        }
        catch(Exception e) {
            Log.e("addBook", e.getMessage());
        }
    }

    // edits a book
    public void editBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        int bookId = book.getId();
        ContentValues values = new ContentValues();
        values.put("isbn", book.getIsbn());
        values.put("title", book.getTitle());
        values.put("author", book.getAuthor());
        values.put("cover", book.getCover());
        values.put("favourite", book.isFavourite() ? 1 : 0);
        values.put("comment_book", book.getComment());

        try {
            db.update("Book", values, "book_id = " + bookId, null);
        }
        catch(Exception e) {
            Log.e("editBook", e.getMessage());
        }
    }

    // removes a book from the db
    public void remBook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // first delete all borrowing processes of the book
        Cursor cursor = db.rawQuery("SELECT * FROM BORROWING_PROCESS WHERE book_id = " + id + " ORDER BY borrowing_id desc;", null);
        try {
            if(cursor.moveToFirst()) {
                do {
                    remBorrowingProcess(cursor.getInt(cursor.getColumnIndex("borrowing_id")));
                } while (cursor.moveToNext());
            }
        }
        catch(Exception e) {
            Log.e("remBook,remBoPo", e.getMessage());
        }
        cursor.close();

        // delete the book
        try {
            db.delete("Book", "book_id = " + id, null);
        }
        catch(Exception e) {
            Log.e("remBook", e.getMessage());
        }
    }

    // adds a borrowing process to the db
    public void addBorrowingProcess(BorrowingProcess proc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(BOOK_ID, proc.getBook_id());
        cv.put(BORROWER, proc.getBorrower());
        cv.put(BEGINNING, proc.getBeginning());
        cv.put(END, proc.getEnding());
        cv.put(COMPLETED, proc.isCompleted() ? 1 : 0);
        cv.put(COMMENT_BORROWING, proc.getComment());

        try {
            db.insert(BORROWING_PROCESS, null, cv);
        }
        catch(Exception e) {
            Log.e("addBorrowingProcess", e.getMessage());
        }
    }

    // edits a borrowing process
    public void editBorrowingProcess(BorrowingProcess proc) {
        SQLiteDatabase db = this.getWritableDatabase();

        int procId = proc.getId();
        ContentValues values = new ContentValues();
        values.put("book_id", proc.getBook_id());
        values.put("borrower", proc.getBorrower());
        values.put("beginning", proc.getBeginning());
        values.put("ending", proc.getEnding());
        values.put("completed", proc.isCompleted() ? 1 : 0);
        values.put("comment_borrowing", proc.getComment());

        try {
            db.update("Borrowing_Process", values, "borrowing_id = " + procId, null);
        }
        catch(Exception e) {
            Log.e("editBorrowingProcess", e.getMessage());
        }
    }

    // removes a borrowing process from the db
    public void remBorrowingProcess(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(BORROWING_PROCESS, "borrowing_id = " + id, null);
        }
        catch(Exception e) {
            Log.e("remBorrowingProcess", e.getMessage());
        }
    }

    // read

    // returns a list of books that are results of the query
    public List<Book> getBooks(String bookQuery) {
        List<Book> bookList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(bookQuery, null);

        try {
            if(cursor.moveToFirst()) {
                do {
                    int bookId = cursor.getInt(cursor.getColumnIndex("book_id"));
                    String bookIsbn = cursor.getString(cursor.getColumnIndex("isbn"));
                    String bookTitle = cursor.getString(cursor.getColumnIndex("title"));
                    String bookAuthor = cursor.getString(cursor.getColumnIndex("author"));
                    String bookCover = cursor.getString(cursor.getColumnIndex("cover"));
                    boolean bookFavourite = cursor.getInt(cursor.getColumnIndex("favourite")) == 1;
                    String bookComment = cursor.getString(cursor.getColumnIndex("comment_book"));

                    Book newBook = new Book(bookId, bookIsbn, bookTitle, bookAuthor, bookFavourite, bookCover, bookComment);
                    bookList.add(newBook);
                } while (cursor.moveToNext());
            }
        }
        catch(Exception e) {
            Log.e("getBooks: " + bookQuery, e.getMessage());
        }

        cursor.close();
        return bookList;
    }

    // returns all saved books
    public List<Book> getAllBooks() {
        return getBooks("SELECT * FROM " + BOOK);
    }

    // returns favourite books
    public List<Book> getFavouriteBooks() {
        return getBooks("SELECT * FROM Book WHERE favourite = 1;");
    }

    // returns borrowed books
    public List<Book> getBorrowedBooks() {
        return getBooks("SELECT * FROM Borrowing_Process b LEFT JOIN Book bo ON bo.book_id = b.book_id where completed = 0;");
    }

    // returns borrowing processes for one book
    public List<BorrowingProcess> getBorrowProcesses(int bookId) {
        List<BorrowingProcess> borrowList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM BORROWING_PROCESS WHERE book_id = " + bookId + " ORDER BY borrowing_id desc;", null);

        try {
            if(cursor.moveToFirst()) {
                do {
                    int borrowId = cursor.getInt(cursor.getColumnIndex("borrowing_id"));
                    String borrower = cursor.getString(cursor.getColumnIndex("borrower"));
                    String beginning = cursor.getString(cursor.getColumnIndex("beginning"));
                    String ending = cursor.getString(cursor.getColumnIndex("ending"));
                    boolean completed = cursor.getInt(cursor.getColumnIndex("completed")) == 1;
                    String comment_borrowing = cursor.getString(cursor.getColumnIndex("comment_borrowing"));

                    BorrowingProcess newBoPo = new BorrowingProcess(borrowId, bookId, borrower, beginning, ending, completed, comment_borrowing);
                    borrowList.add(newBoPo);
                } while (cursor.moveToNext());
            }
        }
        catch(Exception e) {
            Log.e("getBorrowProcesses", e.getMessage());
        }

        cursor.close();
        return borrowList;
    }
}