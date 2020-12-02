package com.example.buchverwaltung;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    /*
    Button btnAdd, btnView;
    EditText title, isbn;
    ListView bookList;
     */
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);
        title = findViewById(R.id.bookTitle);
        isbn = findViewById(R.id.isbn);
        bookList = findViewById(R.id.bookList);

        btnAdd.setOnClickListener(v -> {
            Book book = null;
            try {
                book = new Book(title.getText().toString(), isbn.getText().toString());
                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                dataBaseHelper.addBook(book);
                Toast.makeText(MainActivity.this, "creating book", Toast.LENGTH_LONG).show();
            }
            catch(Exception e) {
                Toast.makeText(MainActivity.this, "Error creating book", Toast.LENGTH_LONG).show();
            }
        });

        btnView.setOnClickListener(v -> {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
            //List<Book> allBooks = dataBaseHelper.getAllBooks();
            //List<Book> allBooks = dataBaseHelper.getFavouriteBooks();
            List<Book> allBooks = dataBaseHelper.getBorrowedBooks();
            Toast.makeText(MainActivity.this, allBooks.toString(), Toast.LENGTH_LONG).show();
        });
        */
    }
}