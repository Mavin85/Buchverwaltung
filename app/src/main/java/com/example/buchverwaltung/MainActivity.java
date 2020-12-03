package com.example.buchverwaltung;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Person;
import android.content.Context;
import android.os.Bundle;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    BookRepo br = new BookRepo();
    private List<ApiResponseBook> bookList = new ArrayList<>();

    /*
    Button btnAdd, btnView;
    EditText title, isbn;
    ListView bookList;
     */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBookList("0201558025");

        Button reload = (Button) findViewById(R.id.reload);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBookList("0201558025");
            }
        });

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

    private void getBookList(String isbn){
        br.getBook(new Callback<ResponseMapper>(){

            @Override
            public void onResponse(Call<ResponseMapper> call, Response<ResponseMapper> response) {
                if (response.isSuccessful()) {
                    Log.d("MainActivity", "getBookList: onResponse -> SUCCESSFUL");
                    ResponseMapper responseMapper = response.body();
                    bookList.addAll(responseMapper.book);
                    ApiResponseBook book = bookList.get(0);
                    setText(book);
                }
                else
                    Log.d("MainActivity", "getPersonList: onResponse -> NOT SUCCESSFUL");
            }

            @Override
            public void onFailure(Call<ResponseMapper> call, Throwable t) {
                Log.d("MainActivity", "getBookList: onResponse -> FAILED \n" + t);
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                String error = getString(R.string.error);
                Toast toast = Toast.makeText(context, error, duration);
                toast.show();
            }
        }, isbn);
    }

    public void setText(ApiResponseBook responsebook){
        String test = responsebook.getVolumeInfo().getImageLinks() + " " + responsebook.getVolumeInfo().getAuthors() + " " + responsebook.getVolumeInfo().getSubtitle();
        Toast toast=Toast.makeText(getApplicationContext(),test,Toast.LENGTH_SHORT);
        toast.show();


    }
}