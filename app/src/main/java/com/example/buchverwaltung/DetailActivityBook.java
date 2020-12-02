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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class DetailActivityBook extends AppCompatActivity {

    RoundedImageView coverView;
    TextView titleView, authorView, isbnView, commentView;
    ImageView favouriteIcon;
    RecyclerView lendingListView;

    Lending lendingTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent i = getIntent();
        int bookId = Integer.parseInt(i.getStringExtra("id"));

        DataBaseHelper dataBaseHelper = new DataBaseHelper(DetailActivityBook.this);
        Book b = dataBaseHelper.getBook(bookId);

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

        List<Lending> lendingList = dataBaseHelper.getLending(b.getId());
        lendingListView = (RecyclerView) findViewById(R.id.detailBookRecyclerViewLendings);
        LendingAdapter la = new LendingAdapter(lendingList);
        lendingListView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        lendingListView.setAdapter(la);

    }
}