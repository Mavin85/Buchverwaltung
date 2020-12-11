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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivityBookAdding extends AppCompatActivity {

    private BookRepo br = new BookRepo();

    SearchView searchIsbnView;
    ImageView coverView;
    TextView titleView, authorView, isbnManual, titleManual, authorManual;
    Button confirmButton, addManualButton;
    Group groupByIsbn;
    Group groupByTitle;
    String isbn;

    String title;
    String thumbnailPath;
    DataBaseHelper dataBaseHelper;

    Book theBook;
    //BookRepo br;
    ApiResponseBook apiResponseBook;
    List<ApiResponseBook> apiBookList;
    List<Book> normalBookList = new ArrayList<>();
    RecyclerView bookResultView;
    Context context;

    BookAddingAdapter ba;



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

        searchIsbnView = findViewById(R.id.detailBookAddingSearchViewIsbn);
        coverView = findViewById(R.id.detailBookAddingCover);
        titleView = findViewById(R.id.detailBookAddingTitle);
        authorView = findViewById(R.id.detailBookAddingAuthor);
        bookResultView = findViewById(R.id.detailBookAddingRecyclerView);
        confirmButton = findViewById(R.id.detailBookAddingButtonAdd);
        addManualButton = findViewById(R.id.detailBookAddingButtonManual);
        isbnManual = findViewById(R.id.detailBookisbnAddManual);
        titleManual = findViewById(R.id.detailBookTitleAddManual);
        authorManual = findViewById(R.id.detailBookAuthorAddManual);
        groupByIsbn = findViewById(R.id.detailBookAddingByIsbnPreview);
        groupByTitle = findViewById(R.id.detailBookAddingByTitelRecyclerPreview);

        groupByTitle.setVisibility(groupByTitle.GONE);
        groupByIsbn.setVisibility(groupByIsbn.GONE);
        confirmButton.setVisibility(View.GONE);
        addManualButton.setVisibility(View.GONE);
        isbnManual.setVisibility(View.GONE);
        titleManual.setVisibility(View.GONE);
        authorManual.setVisibility(View.GONE);

        dataBaseHelper = new DataBaseHelper(DetailActivityBookAdding.this);

        ba = new BookAddingAdapter(normalBookList, context, this);
        bookResultView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        bookResultView.setAdapter(ba);



        searchIsbnView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                groupByTitle.setVisibility(groupByTitle.GONE);
                groupByIsbn.setVisibility(groupByIsbn.GONE);
                Log.d("tag0", "TextSubmit");
                //isbn = "0735619670";
                //isbn = "361301548X";
                //isbn = "9789385031595";
                isbn = query;

                getTheBook(new Callback<BookApiResult>() {
                    @Override
                    public void onResponse(Call<BookApiResult> call, Response<BookApiResult> response) {
                        Log.d("tag0","hallo wir sind on Response");
                        searchIsbnView.clearFocus();
                        // check if there is a book in the answer
                        if(response.body().getTotalItems().equals("0")) {
                            Log.d("tag0","keine ISBN Antwort -> also nach Titel");

                            getBooksByTitle(new Callback<BookApiResult>() {
                                @Override
                                public void onResponse(Call<BookApiResult> call, Response<BookApiResult> response) {
                                    Log.d("tag0","hallo wir sind on Response");
                                    call.cancel();
                                    Log.d("tag0",String.valueOf(call.isCanceled()) + " " + String.valueOf(call.isExecuted()));

                                    searchIsbnView.clearFocus();
                                    Log.d("tag0","Anzahl Treffer: " + response.body().getTotalItems());
                                    // check if there is a book in the answer

                                    if(response.body().getTotalItems().equals("0")) {
                                        Log.d("tag0","no books on answer");
                                        Toast.makeText(context, R.string.apiNoBookReceived, Toast.LENGTH_LONG).show();
                                        Toast toast = Toast.makeText(context, R.string.apiNoBookReceived, Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                                        toast.show();
                                        addManualButton.setVisibility(View.VISIBLE);

                                        //show manual adding components
                                        addManualButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                addManualButton.setVisibility(View.GONE);
                                                isbnManual.setVisibility(View.VISIBLE);
                                                titleManual.setVisibility(View.VISIBLE);
                                                authorManual.setVisibility(View.VISIBLE);
                                                confirmButton.setVisibility(View.VISIBLE);
                                            }
                                        });

                                        // add a book manual to the app
                                        confirmButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                theBook = new Book(String.valueOf(isbnManual.getText()), String.valueOf(titleManual.getText()), String.valueOf(authorManual.getText()), false, 0, "");
                                                dataBaseHelper.addBook(theBook);
                                                Intent iBacktoMain = new Intent(DetailActivityBookAdding.this,MainActivity.class);
                                                startActivity(iBacktoMain);
                                            }
                                        });
                                        return;
                                    }

                                    groupByTitle.setVisibility(groupByTitle.VISIBLE);

                                    apiBookList = response.body().getBook();
                                    normalBookList.clear();

                                    while(!apiBookList.isEmpty()) {
                                        apiResponseBook = apiBookList.remove(0);
                                        Log.d("tag0",apiResponseBook.getApiDetails().getTitle());

                                        if(apiResponseBook.getApiDetails().getIndustryIdentifiers() == null) {
                                            //Log.d("tag0","Keine ISBN bei: " + apiResponseBook.getApiDetails().getTitle());
                                            List<ApiIndustryIdentifier> noIsbnList = Arrays.asList(new ApiIndustryIdentifier(getString(R.string.noIsbnReceived)));
                                            apiResponseBook.getApiDetails().setIndustryIdentifiers(noIsbnList);
                                        }

                                        if(apiResponseBook.getApiDetails().getTitle() == null) {
                                            apiResponseBook.getApiDetails().setTitle(getString(R.string.noTitleReceived));
                                        }

                                        if(apiResponseBook.getApiDetails().getAuthors() == null) {
                                            List<String> noAuthorList = Arrays.asList(getString(R.string.noAuthorReceived));
                                            apiResponseBook.getApiDetails().setAuthors(noAuthorList);
                                        }

                                        theBook = new Book(apiResponseBook.getApiDetails().getIndustryIdentifiers().get(0).getIsbn(), apiResponseBook.getApiDetails().getTitle(), apiResponseBook.getApiDetails().getAuthors().get(0), false, 0, "");

                                        //Log.d("tag0", theBook.getTitle());

                                        //built the correct thumbnail url (https instead of http)
                                        if(apiResponseBook.getApiDetails().getImageLinks() == null) {

                                        }
                                        else{
                                            thumbnailPath = apiResponseBook.getApiDetails().getImageLinks().getThumbnail() + ".jpg";
                                            String[] parts = thumbnailPath.split(":");
                                            thumbnailPath = parts[0] + "s:" + parts[1];
                                            theBook.setCoverString(thumbnailPath);
                                            theBook.setCoverInt(1);  //hat also Cover
                                        }


                                        normalBookList.add(theBook);
                                    }

                                    ba.notifyDataSetChanged();

                                }

                                @Override
                                public void onFailure(Call<BookApiResult> call, Throwable t) {
                                    Log.d("tag0","onFailure oben");
                                    searchIsbnView.clearFocus();
                                    Toast.makeText(context, R.string.apiErrorFailure, Toast.LENGTH_LONG).show();
                                }
                            },isbn);

                            return;
                        }

                        ApiResponseBook book = response.body().getBook().get(0);

                        if(book.getApiDetails().getIndustryIdentifiers() == null) {
                            List<ApiIndustryIdentifier> noIsbnList = Arrays.asList(new ApiIndustryIdentifier(getString(R.string.noIsbnReceived)));
                            book.getApiDetails().setIndustryIdentifiers(noIsbnList);
                        }

                        if(book.getApiDetails().getTitle() == null) {
                            book.getApiDetails().setTitle(getString(R.string.noTitleReceived));
                        }

                        if(book.getApiDetails().getAuthors() == null) {
                            List<String> noAuthorList = Arrays.asList(getString(R.string.noAuthorReceived));
                            book.getApiDetails().setAuthors(noAuthorList);
                        }

                        theBook = new Book(isbn, book.getApiDetails().getTitle(), book.getApiDetails().getAuthors().get(0), false,0, "");
                        if (book.getApiDetails().getImageLinks() == null) {
                            coverView.setImageResource(R.drawable.bookexamplecover);
                        }
                        else {
                            theBook.setCoverInt(1);
                            // built the correct thumbnail url (https instead of http)
                            String thumbnailPath = book.getApiDetails().getImageLinks().getThumbnail() + ".jpg";
                            String[] parts = thumbnailPath.split(":");
                            String newThumbnailPath = parts[0] + "s:" + parts[1];
                            theBook.setCoverString(newThumbnailPath);

                            // load cover into the book preview
                            Picasso.get().load(newThumbnailPath).error(R.drawable.ic_emptythumbnail).into(coverView);
                        }

                        // show the book
                        groupByIsbn.setVisibility(groupByIsbn.VISIBLE);
                        titleView.setText(theBook.getTitle());
                        authorView.setText(theBook.getAuthor());

                        // show confirm button
                        //confirmButton.setVisibility(View.VISIBLE);

                        // add book to the app
                        confirmButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // add the book to the database
                                dataBaseHelper.addBook(theBook);
                                if (theBook.getCoverInt() == 1) {
                                    // store the cover
                                    Picasso.get().load(theBook.getCoverString()).into(DetailActivityBookAdding.picassoImageTarget(context, "coverDir", dataBaseHelper.getBookByTitle(theBook.getTitle()).getId() + "_cover.jpeg"));
                                }

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
                groupByTitle.setVisibility(groupByTitle.GONE);
                groupByIsbn.setVisibility(groupByIsbn.GONE);
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
                        //return;
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
    static Target picassoImageTarget(Context context, final String imageDir, final String imageName) {
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