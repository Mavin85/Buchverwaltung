package com.example.buchverwaltung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import java.io.IOException;
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

    BookAdapter ba;
    RecyclerView bookListView;
    Context context;

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
        context = getApplicationContext();
        bookListView = (RecyclerView) findViewById(R.id.mainBooksRecyclerView);

        searchIsbnView = findViewById(R.id.detailBookAddingSearchViewIsbn);

        /*
        coverView = findViewById(R.id.detailBookAddingCover);
        titleView = findViewById(R.id.detailBookAddingTitle);
        authorView = findViewById(R.id.detailBookAddingAuthor);
         */
        confirmButton = findViewById(R.id.detailBookAddingButtonAdd);
        group = findViewById(R.id.detailBookAddingGroupPreview);

        group.setVisibility(group.GONE);
        confirmButton.setVisibility(View.GONE);

        dataBaseHelper = new DataBaseHelper(DetailActivityBookAdding.this);

        searchIsbnView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //isbn = "0735619670";
                //isbn = "361301548X";
                //isbn = "9789385031595";
                isbn = query;
                /*
                getTheBook(new Callback<BookApiResult>() {
                    @Override
                    public void onResponse(Call<BookApiResult> call, Response<BookApiResult> response) {
                        Log.d("tag0","hallo wir sind on Response");
                        searchIsbnView.clearFocus();
                        // check if there is a book in the answer
                        if(response.body().getTotalItems().equals("0")) {
                            Log.d("tag0","no books on answer");
                            Toast.makeText(context, R.string.apiNoBookReceived, Toast.LENGTH_LONG).show();
                            return;
                        }

                        ApiResponseBook book = response.body().getBook().get(0);

                        // built the correct thumbnail url (https instead of http)
                        String thumbnailPath = book.getApiDetails().getImageLinks().getThumbnail() + ".jpg";
                        String[] parts = thumbnailPath.split(":");
                        String newThumbnailPath = parts[0] + "s:" + parts[1];

                        // load cover into the book preview
                        Picasso.get().load(newThumbnailPath).error(R.drawable.ic_emptythumbnail).into(coverView);

                        theBook = new Book(isbn, book.getApiDetails().getTitle(), book.getApiDetails().getAuthors().get(0), false, R.drawable.bookexamplecover, "");

                        // show the book
                        group.setVisibility(group.VISIBLE);
                        titleView.setText(theBook.getTitle());
                        authorView.setText(theBook.getAuthor());

                        // show confirm button
                        confirmButton.setVisibility(View.VISIBLE);

                        // add book to the app
                        confirmButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // store the cover
                                Picasso.get().load(newThumbnailPath).into(picassoImageTarget(context, "coverDir", isbn + "_cover.jpeg"));
                                // add the book to the database
                                dataBaseHelper.addBook(theBook);
                                Intent iBacktoMain = new Intent(DetailActivityBookAdding.this,MainActivity.class);
                                startActivity(iBacktoMain);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<BookApiResult> call, Throwable t) {
                        Log.d("tag0","onFailure oben");
                        searchIsbnView.clearFocus();
                        Toast.makeText(context, R.string.apiErrorFailure, Toast.LENGTH_LONG).show();
                    }
                },isbn);
                 */

                getBooksByTitle(new Callback<BookApiResult>() {
                    @Override
                    public void onResponse(Call<BookApiResult> call, Response<BookApiResult> response) {
                        Log.d("tag0","hallo wir sind on Response");
                        searchIsbnView.clearFocus();
                        Log.d("tag0","Anzahl Treffer: " + response.body().getTotalItems());
                        // check if there is a book in the answer
                        if(response.body().getTotalItems().equals("0")) {
                            Log.d("tag0","no books on answer");
                            Toast.makeText(context, R.string.apiNoBookReceived, Toast.LENGTH_LONG).show();
                            return;
                        }


                        List<ApiResponseBook> responseBooks = response.body().getBook();
                        List<Book> foundBooks = null;
                        int bookCounter = 0;

                        while(!responseBooks.isEmpty()) {
                            ApiResponseBook bo = responseBooks.remove(bookCounter);
                            Log.d("tag0","Book Counter: " + bookCounter);
                            bookCounter++;
                            foundBooks.add(new Book(bo.getIndustryIdentifiers().get(0).getIsbn(), bo.getApiDetails().getTitle(), bo.getApiDetails().getAuthors().get(0), false, 0, ""));
                        }


                        ba = new BookAdapter(foundBooks, context);
                        bookListView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
                        bookListView.setAdapter(ba);

                        /*
                        // built the correct thumbnail url (https instead of http)
                        String thumbnailPath = book.getApiDetails().getImageLinks().getThumbnail() + ".jpg";
                        String[] parts = thumbnailPath.split(":");
                        String newThumbnailPath = parts[0] + "s:" + parts[1];

                        // load cover into the book preview
                        Picasso.get().load(newThumbnailPath).error(R.drawable.ic_emptythumbnail).into(coverView);

                        theBook = new Book(isbn, book.getApiDetails().getTitle(), book.getApiDetails().getAuthors().get(0), false, R.drawable.bookexamplecover, "");

                        // show the book
                        group.setVisibility(group.VISIBLE);
                        titleView.setText(theBook.getTitle());
                        authorView.setText(theBook.getAuthor());
                         */

                        // show confirm button
                        confirmButton.setVisibility(View.VISIBLE);

                        // add book to the app
                        confirmButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // store the cover
                                //Picasso.get().load(newThumbnailPath).into(picassoImageTarget(context, "coverDir", isbn + "_cover.jpeg"));
                                // add the book to the database
                                //dataBaseHelper.addBook(theBook);
                                Intent iBacktoMain = new Intent(DetailActivityBookAdding.this,MainActivity.class);
                                startActivity(iBacktoMain);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<BookApiResult> call, Throwable t) {
                        Log.d("tag0","onFailure oben");
                        searchIsbnView.clearFocus();
                        Toast.makeText(context, R.string.apiErrorFailure, Toast.LENGTH_LONG).show();
                    }
                },isbn);

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
                        Log.d("tag0", "getBookList: onResponse -> SUCCESSFUL");
                        callback.onResponse(call,response);
                        //call.enqueue(callback);
                        return;
                    }
                    catch(Exception e){
                        Log.d("tag0", "getBookList: onResponse -> Exception: "+ e);
                        return;
                    }
                }
                else {
                    Log.d("tag0", "getPersonList: onResponse -> NOT SUCCESSFUL");
                    Toast.makeText(context, R.string.apiErrorResponse, Toast.LENGTH_LONG).show();
                    return;
                }
            }

            @Override
            public void onFailure(@NotNull Call<BookApiResult> call, @NotNull Throwable t) {
                Log.d("tag0", "getBookList: onResponse -> FAILED \n" + t);
                callback.onFailure(call,t);
                //call.enqueue(callback);
                //return;
            }
        }, isbn);
    }

    public void getBooksByTitle(Callback<BookApiResult> callback, String title){
        br.getBooksByTitle(new Callback<BookApiResult>(){
            @Override
            public void onResponse(@NotNull Call<BookApiResult> call, @NotNull Response<BookApiResult> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.d("tag0", "getBookList: onResponse -> SUCCESSFUL");
                        callback.onResponse(call,response);
                        //call.enqueue(callback);
                        return;
                    }
                    catch(Exception e){
                        Log.d("tag0", "getBookList: onResponse -> Exception: "+ e);
                        return;
                    }
                }
                else {
                    Log.d("tag0", "getPersonList: onResponse -> NOT SUCCESSFUL");
                    Toast.makeText(context, R.string.apiErrorResponse, Toast.LENGTH_LONG).show();
                    return;
                }
            }

            @Override
            public void onFailure(@NotNull Call<BookApiResult> call, @NotNull Throwable t) {
                Log.d("tag0", "getBookList: onResponse -> FAILED \n" + t);
                callback.onFailure(call,t);
                //call.enqueue(callback);
                //return;
            }
        }, isbn);
    }

    // method to save the book cover with picasso
    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {
        Log.d("picassoImageTarget", " picassoImageTarget");
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {}
            }
        };
    }


    @Override
    public void onBackPressed() {
        Intent iBackToMain = new Intent(DetailActivityBookAdding.this, MainActivity.class);
        startActivity(iBackToMain);
    }
}


