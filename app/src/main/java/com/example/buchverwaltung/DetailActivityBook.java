package com.example.buchverwaltung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DetailActivityBook extends AppCompatActivity {

    RoundedImageView coverView;
    TextView titleView, authorView, isbnView, commentView;
    ImageView favouriteIcon;
    Button deleteButton, newLendingButton;


    List<Lending> lendingList;
    LendingAdapter la;
    RecyclerView lendingListView;

    DataBaseHelper dataBaseHelper;

    Book b;
    //Definition of Sorting Functions for the RecyclerView
    final Comparator<Lending> lendingComparatorByStart = new Comparator<Lending>() {
        @Override
        public int compare(Lending l1, Lending l2) {
            return String.valueOf(l1.getId()).compareTo(String.valueOf(l2.getId()));
        }
    };


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
        coverView.setImageResource(b.getCoverInt());

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
        //listener to change Favourite...
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

        lendingList = dataBaseHelper.getLendings(b.getId());

        // filling RecyclerView with sorted by title
        lendingListView = (RecyclerView) findViewById(R.id.detailBookRecyclerViewLendings);
        //sorting List by Title for first call of activity
        Collections.sort(lendingList, lendingComparatorByStart);
        la = new LendingAdapter(lendingList,this);
        lendingListView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        lendingListView.setAdapter(la);

        deleteButton = findViewById(R.id.detailBookButtonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iDeleteBookBackToMain = new Intent(DetailActivityBook.this, MainActivity.class);
                dataBaseHelper.remBook(b.getId());
                startActivity(iDeleteBookBackToMain);
            }
        });

        newLendingButton = findViewById(R.id.detailBookButtonLending);
        newLendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iAddALending = new Intent(DetailActivityBook.this, DetailActivityLending.class);
                iAddALending.putExtra("bookId", b.getId());
                startActivity(iAddALending);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent iBacktoMain = new Intent(DetailActivityBook.this, MainActivity.class);
        startActivity(iBacktoMain);
    }
}