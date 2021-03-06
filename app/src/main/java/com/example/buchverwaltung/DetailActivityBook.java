package com.example.buchverwaltung;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DetailActivityBook extends AppCompatActivity {

    RoundedImageView coverView;
    TextView titleView, authorView, isbnView;
    ImageView favouriteIcon;
    EditText commentView;
    Button deleteButton, newLendingButton;

    List<Lending> lendingList;
    LendingAdapter la;
    RecyclerView lendingListView;
    DataBaseHelper dataBaseHelper;
    Book b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent i = getIntent();
        int bookId = i.getIntExtra("id",0);

        dataBaseHelper = new DataBaseHelper(DetailActivityBook.this);
        b = dataBaseHelper.getBook(bookId);

        coverView = findViewById(R.id.detailBookCover);
        if(b.getCoverInt() == 0) {
            coverView.setImageResource(R.drawable.bookexamplecover);
        }
        else {
            // load cover
            ContextWrapper cw = new ContextWrapper(DetailActivityBook.this);
            File directory = cw.getDir("coverDir", Context.MODE_PRIVATE);
            File myImageFile = new File(directory, b.getId() + "_cover.jpeg");
            Picasso.get().load(myImageFile).into(coverView);
        }

        titleView = findViewById(R.id.detailBookTitle);
        titleView.setText(b.getTitle());

        authorView = findViewById(R.id.detailBookAuthor);
        authorView.setText(b.getAuthor());

        isbnView = findViewById(R.id.detailBookIsbn);
        isbnView.setText(b.getIsbn());

        commentView = findViewById(R.id.detailBookComment);
        commentView.setText(b.getComment());

        favouriteIcon = findViewById(R.id.detailBookFavorite);
        if(b.isFavourite()) {
            favouriteIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorRatingActive)));
        } else {
            favouriteIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorRatingInactive)));
        }
        //listener to change Favourite
        favouriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b.isFavourite()) {
                    b.setFavourite(false);
                    favouriteIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(DetailActivityBook.this, R.color.colorRatingInactive)));
                } else {
                    b.setFavourite(true);
                    favouriteIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(DetailActivityBook.this, R.color.colorRatingActive)));
                }
                dataBaseHelper.editBook(b);
            }
        });

        lendingList = dataBaseHelper.getLendingsForBook(b.getId());
        lendingListView = (RecyclerView) findViewById(R.id.detailBookRecyclerViewLendings);
        la = new LendingAdapter(lendingList,this);
        lendingListView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        lendingListView.setAdapter(la);

        deleteButton = findViewById(R.id.detailBookButtonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailActivityBook.this)
                        .setTitle(R.string.dialogDeleteBook)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                // delete the cover
                                // exapmle path: /data/data/com.example.buchverwaltung/app_coverDir/3_cover.jpeg
                                String baseDir = DetailActivityBook.this.getFilesDir().getPath().replace("files", "");
                                String coverDir = baseDir + "app_coverDir/" + b.getId() + "_cover.jpeg";
                                File file = new File(coverDir);
                                if(file.exists()){
                                    try {
                                        file.getCanonicalFile().delete();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                // delete the entries from the db
                                Intent iDeleteBookBackToMain = new Intent(DetailActivityBook.this, MainActivity.class);
                                dataBaseHelper.remBook(b.getId());
                                startActivity(iDeleteBookBackToMain);
                            }})
                        .setNegativeButton(android.R.string.cancel, null).show();
            }
        });

        newLendingButton = findViewById(R.id.detailBookButtonLending);
        // only show button when no lending is active
        if(!dataBaseHelper.getActiveLendingsForBook(b.getId()).isEmpty()) {
            newLendingButton.setVisibility(View.GONE);
        }
        newLendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComment();
                Intent iAddALending = new Intent(DetailActivityBook.this, DetailActivityLending.class);
                iAddALending.putExtra("bookId", b.getId());
                startActivity(iAddALending);
            }
        });
    }

    @Override
    public void onBackPressed() {
        saveComment();
        Intent iBacktoMain = new Intent(DetailActivityBook.this, MainActivity.class);
        startActivity(iBacktoMain);
    }

    public void saveComment() {
        b.setComment(commentView.getText().toString());
        dataBaseHelper.editBook(b);
    }
}