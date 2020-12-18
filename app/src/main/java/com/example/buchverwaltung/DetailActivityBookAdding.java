package com.example.buchverwaltung;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivityBookAdding extends AppCompatActivity {

    private final BookRepo br = new BookRepo();

    SearchView searchIsbnView;
    ImageView coverView;
    TextView titleView, authorView, isbnManual, titleManual, authorManual;
    RecyclerView bookResultView;
    Button confirmButton, addManualButton;
    Group groupByIsbn,groupByTitle,groupByManual;
    DataBaseHelper dataBaseHelper;
    String isbn,thumbnailPath;
    Book theBook;
    ApiResponseBook apiResponseBook;
    List<ApiResponseBook> apiBookList;
    List<Book> normalBookList = new ArrayList<>();
    Context context;
    BookAddingAdapter ba;

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
        groupByManual = findViewById(R.id.detailBookAddingByManual);

        groupByTitle.setVisibility(View.GONE);
        groupByIsbn.setVisibility(View.GONE);
        groupByManual.setVisibility(View.GONE);
        addManualButton.setVisibility(View.GONE);

        dataBaseHelper = new DataBaseHelper(DetailActivityBookAdding.this);

        ba = new BookAddingAdapter(normalBookList, context, this);
        bookResultView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        bookResultView.setAdapter(ba);

        searchIsbnView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String searchText) {
                groupByTitle.setVisibility(View.GONE);
                groupByIsbn.setVisibility(View.GONE);
                groupByManual.setVisibility(View.GONE);
                addManualButton.setVisibility(View.GONE);

                getABookByIsbn(new Callback<BookApiResult>() {

                    @Override
                    // occurs when Api responded for ISBN-Request
                    public void onResponse(@NotNull Call<BookApiResult> call, @NotNull Response<BookApiResult> response) {
                        searchIsbnView.clearFocus();
                        assert response.body() != null;

                        // occurs when Api response for ISBN-Request is empty
                        if(response.body().getTotalItems().equals("0")) {

                            getBooksByTitle(new Callback<BookApiResult>() {

                                @Override
                                // occurs when Api responded for Title-Request
                                public void onResponse(@NotNull Call<BookApiResult> call, Response<BookApiResult> response) {
                                    searchIsbnView.clearFocus();
                                    assert response.body() != null;

                                    // occurs when Api response for Title-Request is empty
                                    if(response.body().getTotalItems().equals("0")) {
                                        handleManualAdding();
                                        return;
                                    }
                                    handleTitleAdding(response);
                                }

                                @Override
                                // occurs when Api not responded for Title-Request
                                public void onFailure(@NotNull Call<BookApiResult> call, @NotNull Throwable t) {
                                    searchIsbnView.clearFocus();
                                    Toast.makeText(context, R.string.apiErrorFailure, Toast.LENGTH_LONG).show();
                                }
                            },searchText);
                            return;
                        }
                        handleIsbnAdding(response, searchText);
                    }

                    @Override
                    // occurs when Api not responded for ISBN-Request
                    public void onFailure(@NotNull Call<BookApiResult> call, Throwable t) {
                        searchIsbnView.clearFocus();
                        Toast.makeText(context, R.string.apiErrorFailure, Toast.LENGTH_LONG).show();
                    }
                },searchText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void handleIsbnAdding(Response<BookApiResult> response, String searchText) {
        isbn = searchText;
        assert response.body() != null;
        ApiResponseBook book = response.body().getBook().get(0);

        if(book.getApiDetails().getIndustryIdentifiers() == null) {
            List<ApiIndustryIdentifier> noIsbnList = Collections.singletonList(new ApiIndustryIdentifier(getString(R.string.noIsbnReceived)));
            book.getApiDetails().setIndustryIdentifiers(noIsbnList);
        }

        if(book.getApiDetails().getTitle() == null) {
            book.getApiDetails().setTitle(getString(R.string.noTitleReceived));
        }

        if(book.getApiDetails().getAuthors() == null) {
            List<String> noAuthorList = Collections.singletonList(getString(R.string.noAuthorReceived));
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
        groupByIsbn.setVisibility(View.VISIBLE);
        titleView.setText(theBook.getTitle());
        authorView.setText(theBook.getAuthor());

        // add book to the app
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add the book to the database
                dataBaseHelper.addBook(theBook);
                if (theBook.getCoverInt() == 1) {
                    // store the cover
                    Picasso.get().load(theBook.getCoverString()).error(R.drawable.ic_emptythumbnail).into(DetailActivityBookAdding.picassoImageTarget(context, dataBaseHelper.getBookByTitle(theBook.getTitle()).getId() + "_cover.jpeg"));
                }

                Intent iBacktoMain = new Intent(DetailActivityBookAdding.this, MainActivity.class);
                startActivity(iBacktoMain);
            }
        });
    }

    private void handleTitleAdding(Response<BookApiResult> response) {
        groupByTitle.setVisibility(View.VISIBLE);

        assert response.body() != null;
        apiBookList = response.body().getBook();
        normalBookList.clear();

        while(!apiBookList.isEmpty()) {
            apiResponseBook = apiBookList.remove(0);

            if(apiResponseBook.getApiDetails().getIndustryIdentifiers() == null) {
                List<ApiIndustryIdentifier> noIsbnList = Collections.singletonList(new ApiIndustryIdentifier(getString(R.string.noIsbnReceived)));
                apiResponseBook.getApiDetails().setIndustryIdentifiers(noIsbnList);
            }

            if(apiResponseBook.getApiDetails().getTitle() == null) {
                apiResponseBook.getApiDetails().setTitle(getString(R.string.noTitleReceived));
            }

            if(apiResponseBook.getApiDetails().getAuthors() == null) {
                List<String> noAuthorList = Collections.singletonList(getString(R.string.noAuthorReceived));
                apiResponseBook.getApiDetails().setAuthors(noAuthorList);
            }

            theBook = new Book(apiResponseBook.getApiDetails().getIndustryIdentifiers().get(0).getIsbn(), apiResponseBook.getApiDetails().getTitle(), apiResponseBook.getApiDetails().getAuthors().get(0), false, 0, "");

            //built the correct thumbnail url (https instead of http)
            if(apiResponseBook.getApiDetails().getImageLinks() != null) {
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

    private void handleManualAdding() {
        Toast toast = Toast.makeText(context, R.string.apiNoBookReceived, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
        addManualButton.setVisibility(View.VISIBLE);

        //show manual adding components
        addManualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addManualButton.setVisibility(View.GONE);
                groupByManual.setVisibility(View.VISIBLE);
            }
        });

        // add a book manual to the app
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theBook = new Book(String.valueOf(isbnManual.getText()), String.valueOf(titleManual.getText()), String.valueOf(authorManual.getText()), false, 0, "");
                dataBaseHelper.addBook(theBook);
                Intent iBacktoMain = new Intent(DetailActivityBookAdding.this, MainActivity.class);
                startActivity(iBacktoMain);
            }
        });
    }

    public void getABookByIsbn(Callback<BookApiResult> callback, String isbn){
        br.getABook(new Callback<BookApiResult>(){
            @Override
            public void onResponse(@NotNull Call<BookApiResult> call, @NotNull Response<BookApiResult> response) {
                if (response.isSuccessful()) {
                    try {
                        callback.onResponse(call,response);
                    }
                    catch(Exception e){
                    }
                }
                else {
                    Toast.makeText(context, R.string.apiErrorResponse, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<BookApiResult> call, @NotNull Throwable t) {
                callback.onFailure(call,t);
            }
        }, isbn);
    }

    public void getBooksByTitle(Callback<BookApiResult> callback, String title){
        br.getBooksByTitle(new Callback<BookApiResult>(){
            @Override
            public void onResponse(@NotNull Call<BookApiResult> call, @NotNull Response<BookApiResult> response) {
                if (response.isSuccessful()) {
                    try {
                        callback.onResponse(call,response);
                    }
                    catch(Exception e){
                    }
                }
                else {
                    Toast.makeText(context, R.string.apiErrorResponse, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<BookApiResult> call, @NotNull Throwable t) {
                callback.onFailure(call,t);
            }
        }, title);
    }

    // method to save the book cover with picasso
    static Target picassoImageTarget(Context context, final String imageName) {
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir("coverDir", Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
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
                                assert fos != null;
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
    }

    @Override
    public void onBackPressed() {
        Intent iBackToMain = new Intent(DetailActivityBookAdding.this, MainActivity.class);
        startActivity(iBackToMain);
    }
}