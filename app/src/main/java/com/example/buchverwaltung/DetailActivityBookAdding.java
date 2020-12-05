package com.example.buchverwaltung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.Group;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivityBookAdding extends AppCompatActivity {

    private BookRepo br = new BookRepo();

    SearchView searchIsbnView;
    ImageView coverView;
    TextView titleView, authorView;
    Button confirmButton;
    Group group;
    String isbn;

    String title;
    DataBaseHelper dataBaseHelper;

    Book theBook;
    //BookRepo br;
    ApiResponseBook apiResponseBook;
    List<ApiResponseBook> bookList;
    List<Book> normalBookList;

    public DetailActivityBookAdding() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book_adding);
    }

    @Override
    protected void onStart() {
        super.onStart();

        searchIsbnView = findViewById(R.id.detailBookAddingSearchViewIsbn);
        coverView = findViewById(R.id.detailBookAddingCover);
        titleView = findViewById(R.id.detailBookAddingTitle);
        authorView = findViewById(R.id.detailBookAddingAuthor);
        confirmButton = findViewById(R.id.detailBookAddingButtonAdd);
        group = findViewById(R.id.detailBookAddingGroupPreview);

        group.setVisibility(group.GONE);

        searchIsbnView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                isbn = "0735619670";
                //isbn = query;
                Log.d("tag5", "Log funktioniert");
                getTheBook(new Callback<ResponseMapper>() {
                    @Override
                    public void onResponse(Call<ResponseMapper> call, Response<ResponseMapper> response) {

                        ApiResponseBook book = response.body().getBook().get(0);
                        Book normalBook = new Book(isbn, book.getVolumeInfo().getTitle(), book.getVolumeInfo().getAuthors().get(0), false, R.drawable.bookexamplecover, "");

                        title = normalBook.getTitle();

                        //show the book
                        group.setVisibility(group.VISIBLE);
                        titleView.setText(normalBook.getTitle());
                        authorView.setText(normalBook.getAuthor());
                        coverView.setImageResource(normalBook.getCoverInt());
                        Log.d("tag5", "ganz durch");
                    }

                    @Override
                    public void onFailure(Call<ResponseMapper> call, Throwable t) {
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;
                        String error = getString(R.string.error);
                        Toast toast = Toast.makeText(context, error, duration);
                        toast.show();
                    }
                },isbn);


                Log.d("tag5", "title: " + title);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    public void getTheBook(Callback<ResponseMapper> callback,String isbn){

        br.getBook(new Callback<ResponseMapper>(){

            @Override
            public void onResponse(@NotNull Call<ResponseMapper> call, @NotNull Response<ResponseMapper> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("MainActivity", "getBookList: onResponse -> SUCCESSFUL");

                        callback.onResponse(call,response);
                        call.enqueue(callback);

                        return;
                    }
                    catch(Exception e){
                        Log.d("MainActivity", "getBookList: onResponse -> Exception: "+ e);
                        return;
                    }
                }
                else {
                    Log.d("tag5", "not successful");
                    Log.d("MainActivity", "getPersonList: onResponse -> NOT SUCCESSFUL");
                    return;
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseMapper> call, @NotNull Throwable t) {
                Log.d("MainActivity", "getBookList: onResponse -> FAILED \n" + t);

                callback.onFailure(call,t);
                call.enqueue(callback);
                return;
            }
        }, isbn);
    }
}
