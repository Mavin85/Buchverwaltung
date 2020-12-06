package com.example.buchverwaltung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.Group;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
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
                getTheBook(new Callback<BookApiResult>() {
                    @Override
                    public void onResponse(Call<BookApiResult> call, Response<BookApiResult> response) {
                        Log.d("Tag0","hallo wir sind on Response");
                        ApiResponseBook book = response.body().getBook().get(0);




                        //Diese Drecksarbeit möchte Marvin irgendwann erledigen... dann einfach beim theBook erstellen ein int angeben...!!!


                        //save the thumbnail in drawable and change the imagesource of the apiresponseBook with a Int to the local location
                        String thumbnailPath = book.getApiDetails().getImageLinks().getThumbnail() + ".jpg";
                        String[] parts = thumbnailPath.split(":");

                        String newThumbnailPath = parts[0] + "s:" + parts[1];

                        Picasso.get().load(newThumbnailPath).error(R.drawable.ic_emptythumbnail).into(coverView);

                        theBook = new Book(isbn, book.getApiDetails().getTitle(), book.getApiDetails().getAuthors().get(0), false, R.drawable.bookexamplecover, "");
                        //show the book
                        group.setVisibility(group.VISIBLE);
                        titleView.setText(theBook.getTitle());
                        authorView.setText(theBook.getAuthor());

                        dataBaseHelper = new DataBaseHelper(DetailActivityBookAdding.this);
                        confirmButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dataBaseHelper.addBook(theBook);
                                Intent iBacktoMain = new Intent(DetailActivityBookAdding.this,MainActivity.class);
                                startActivity(iBacktoMain);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<BookApiResult> call, Throwable t) {
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


    public void getTheBook(Callback<BookApiResult> callback, String isbn){

        br.getABook(new Callback<BookApiResult>(){

            @Override
            public void onResponse(@NotNull Call<BookApiResult> call, @NotNull Response<BookApiResult> response) {
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
            public void onFailure(@NotNull Call<BookApiResult> call, @NotNull Throwable t) {
                Log.d("MainActivity", "getBookList: onResponse -> FAILED \n" + t);

                callback.onFailure(call,t);
                call.enqueue(callback);
                return;
            }
        }, isbn);
    }

}


