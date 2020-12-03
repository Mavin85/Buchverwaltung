package com.example.buchverwaltung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    Book bookTest;
    Lending lendingTestFalse;
    Lending lendingTestTrue;

    RecyclerView bookListView;
    ImageView addBookView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    @Override
    protected void onStart() {
        super.onStart();

        bookTest = new Book(5,"isbn", "titel","author",true,2131165279,"comment");

        lendingTestFalse = new Lending(5, 1, "Lender", "Start", "Ende", false, "Das ist ein Kommentar");
        lendingTestTrue = new Lending(5, 1, "Lender", "Start", "Ende", true, "Das ist ein Kommentar");

        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
        //dataBaseHelper.addLending(lendingTestTrue);

        List<Book> allBooks = dataBaseHelper.getAllBooks();
        //Toast.makeText(MainActivity.this, allBooks.toString(), Toast.LENGTH_LONG).show();

        int coverInt = R.drawable.bookexamplecover;
        //Toast.makeText(MainActivity.this, String.valueOf(coverInt), Toast.LENGTH_LONG).show();
        Log.d("Tag",String.valueOf(R.drawable.bookexamplecover));


        bookListView = (RecyclerView) findViewById(R.id.mainBooksRecyclerView);
        BookAdapter ba = new BookAdapter(allBooks, MainActivity.this);
        bookListView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        bookListView.setAdapter(ba);

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