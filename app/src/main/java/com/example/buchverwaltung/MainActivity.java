package com.example.buchverwaltung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    Book bookTest;
    Lending lendingTestFalse;
    Lending lendingTestTrue;

    List<Book> allBooks;
    DataBaseHelper dataBaseHelper;
    BookAdapter ba;

    RecyclerView bookListView;
    ImageView addBookView;
    ImageView filterRecyclerView;

    private int filterCount;

    //Definitions of Sorting Functions for the RecyclerView
    final Comparator<Book> bookComparatorByTitle = new Comparator<Book>() {
        @Override
        public int compare(Book b1, Book b2) {
            return b1.getTitle().compareTo(b2.getTitle());
        }
    };
    final Comparator<Book> bookComparatorByAuthor = new Comparator<Book>() {
        @Override
        public int compare(Book b1, Book b2) {
            return b1.getAuthor().compareTo(b2.getAuthor());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();


        //creating example Books and Lendings
        bookTest = new Book(5,"isbn", "ztiteltest","aauthortest",false,2131165279,"comment");

        lendingTestFalse = new Lending(5, 1, "Lender", "Start", "Ende", false, "Das ist ein Kommentar");
        lendingTestTrue = new Lending(1, 1, "Lender 3", "12/03/2020", "01/10/2021", false, "Das ist ein Kommentar");

        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        //dataBaseHelper.addLending(lendingTestTrue);

        allBooks = dataBaseHelper.getAllBooks();
        //Toast.makeText(MainActivity.this, allBooks.toString(), Toast.LENGTH_LONG).show();

        int coverInt = R.drawable.bookexamplecover;
        //Toast.makeText(MainActivity.this, String.valueOf(coverInt), Toast.LENGTH_LONG).show();
        Log.d("Tag","dies ist ein test wie oft das kommt");


        // filling RecyclerView with sorted by title
        bookListView = (RecyclerView) findViewById(R.id.mainBooksRecyclerView);
            //sorting List by Title for first call of activity
        Collections.sort(allBooks, bookComparatorByTitle);
        ba = new BookAdapter(allBooks, MainActivity.this);
        bookListView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        bookListView.setAdapter(ba);


        //Button for change the listsorting
        //set filterCount to 1 when starting the activity cause the actual filter is byTitle
        filterCount = 1;
        filterRecyclerView = findViewById(R.id.mainFilterIcon);
        filterRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                if(filterCount == 0) {  //sorting by title
                    filterCount = 1;
                    allBooks.clear();
                    allBooks.addAll(dataBaseHelper.getAllBooks());
                    Collections.sort(allBooks, bookComparatorByTitle);
                    Log.d("Tag",allBooks.toString());
                    ba.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, R.string.mainActivitySortingByTitel, Toast.LENGTH_SHORT).show();
                }else {
                    if(filterCount == 1) {  //sorting by author
                        filterCount = 2;
                        allBooks.clear();
                        allBooks.addAll(dataBaseHelper.getAllBooks());
                        Collections.sort(allBooks, bookComparatorByAuthor);
                        Log.d("Tag",allBooks.toString());
                        ba.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, R.string.mainActivitySortingByAuthor, Toast.LENGTH_SHORT).show();

                    }else {
                        if(filterCount == 2) {  //sorting by favourites
                            filterCount = 0;
                            allBooks.clear();
                            allBooks.addAll(dataBaseHelper.getFavouriteBooks());
                            Collections.sort(allBooks, bookComparatorByTitle);
                            Log.d("Tag",allBooks.toString());
                            ba.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, R.string.mainActivityFilterFavourites, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        //Button for adding a Book
        addBookView = findViewById(R.id.mainAddIcon);
        addBookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iAddBook = new Intent(MainActivity.this, DetailActivityBookAdding.class);
                startActivity(iAddBook);
            }
        });





    }
}