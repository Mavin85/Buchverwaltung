package com.example.buchverwaltung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    Book bookTest;

    RecyclerView bookListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    @Override
    protected void onStart() {
        super.onStart();

        bookTest = new Book(5,"isbn", "titel","author",true,2131165279,"comment");

        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
        dataBaseHelper.addBook(bookTest);
        dataBaseHelper.addBook(bookTest);
        dataBaseHelper.addBook(bookTest);
        List<Book> allBooks = dataBaseHelper.getAllBooks();
        Toast.makeText(MainActivity.this, allBooks.toString(), Toast.LENGTH_LONG).show();

        int coverInt = R.drawable.bookexamplecover;
        //Toast.makeText(MainActivity.this, String.valueOf(coverInt), Toast.LENGTH_LONG).show();
        Log.d("Tag",String.valueOf(R.drawable.bookexamplecover));


        bookListView = (RecyclerView) findViewById(R.id.mainBooksRecyclerView);
        BookAdapter ba = new BookAdapter(allBooks);
        bookListView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        bookListView.setAdapter(ba);

    }
}