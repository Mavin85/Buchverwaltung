package com.example.buchverwaltung;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    List<Book> allBooks;
    DataBaseHelper dataBaseHelper;
    BookAdapter ba;
    RecyclerView bookListView;
    ImageView addBookView, welcomeView;
    TextView descriptionView;
    Spinner filterSpinner;
    View backgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        allBooks = dataBaseHelper.getAllBooks();

        // create the spinner
        filterSpinner = (Spinner) findViewById(R.id.mainFilterSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.filter_array, R.layout.item_spinner);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        filterSpinner.setAdapter(adapter);
        filterSpinner.setOnItemSelectedListener(this);

        // show description (only if there is no book)
        descriptionView = findViewById(R.id.mainDescription);
        welcomeView = findViewById(R.id.main_welcomeSign);
        backgroundView = findViewById(R.id.mainBackgroundView);
        descriptionView.setVisibility(View.GONE);
        welcomeView.setVisibility(View.GONE);
        backgroundView.setVisibility(View.GONE);

        if(allBooks.isEmpty()) {
            descriptionView.setVisibility(View.VISIBLE);
            welcomeView.setVisibility(View.VISIBLE);
            backgroundView.setVisibility(View.VISIBLE);
        }

        // filling RecyclerView with sorted by title
        bookListView = (RecyclerView) findViewById(R.id.mainBooksRecyclerView);
        // sorting List by Title for first call of activity
        Collections.sort(allBooks, bookComparatorByTitle);
        ba = new BookAdapter(allBooks, MainActivity.this);
        bookListView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        bookListView.setAdapter(ba);

        // button for adding a book
        addBookView = findViewById(R.id.mainAddIcon);
        addBookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iAddBook = new Intent(MainActivity.this, DetailActivityBookAdding.class);
                startActivity(iAddBook);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent iExitApp;
        iExitApp = new Intent(Intent.ACTION_MAIN);
        iExitApp.addCategory(Intent.CATEGORY_HOME);
        iExitApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(iExitApp);
    }

    // definitions of sorting functions for the RecyclerView
    final Comparator<Book> bookComparatorByTitle = new Comparator<Book>() {
        @Override
        public int compare(Book b1, Book b2) {
            return b1.getTitle().toLowerCase().compareTo(b2.getTitle().toLowerCase());
        }
    };
    final Comparator<Book> bookComparatorByAuthor = new Comparator<Book>() {
        @Override
        public int compare(Book b1, Book b2) {
            return b1.getAuthor().toLowerCase().compareTo(b2.getAuthor().toLowerCase());
        }
    };

    // button for change the sorting of the list
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        allBooks.clear();

        // sorting by title
        if(position == 0) {
            allBooks.addAll(dataBaseHelper.getAllBooks());
            Collections.sort(allBooks, bookComparatorByTitle);
        }

        // sorting by author
        if(position == 1) {
            allBooks.addAll(dataBaseHelper.getAllBooks());
            Collections.sort(allBooks, bookComparatorByAuthor);
        }

        // filter by favorites
        if(position == 2) {
            allBooks.addAll(dataBaseHelper.getFavouriteBooks());
            Collections.sort(allBooks, bookComparatorByTitle);
        }

        // filter by active lendings
        if(position == 3) {
            allBooks.addAll(dataBaseHelper.getLendedBooks());
            Collections.sort(allBooks, bookComparatorByTitle);
        }

        ba.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}