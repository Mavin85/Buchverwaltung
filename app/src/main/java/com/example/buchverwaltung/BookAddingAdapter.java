package com.example.buchverwaltung;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookAddingAdapter extends RecyclerView.Adapter<BookAddingAdapter.BookAddingViewHolder> {
    List<Book> bookList;
    Context con;

    public BookAddingAdapter(List<Book> bookList, Context con) {
        this.bookList = bookList;
        this.con = con;
    }

    @NonNull
    @Override
    public BookAddingAdapter.BookAddingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addbooks, parent, false);
        BookAddingViewHolder vh = new BookAddingViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BookAddingAdapter.BookAddingViewHolder holder, int position) {
        Book b = bookList.get(position);
        //holder.itemView.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
//
        //        //store the cover
        //        Picasso.get().load(newThumbnailPath).into(picassoImageTarget(context, "coverDir", isbn + "_cover.jpeg"));
        //        add the book to the database
        //        dataBaseHelper.addBook(theBook);
        //        Intent i = new Intent(con, DetailActivityBook.class);
        //        i.putExtra("id", b.getId());
        //        con.startActivity(i);
        //    }
        //});

        holder.b_title.setText(b.getTitle());
        holder.b_author.setText(b.getAuthor());
        Picasso.get().load(b.getCoverString()).into(holder.b_cover);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookAddingViewHolder extends RecyclerView.ViewHolder{
        ImageView b_cover;
        TextView b_title;
        TextView b_author;

        public BookAddingViewHolder(@NonNull View itemView) {
            super(itemView);
            b_cover = (ImageView) itemView.findViewById(R.id.detailBookAddingBooksCover);
            b_title = (TextView) itemView.findViewById(R.id.detailBookAddingBooksTitle);
            b_author = (TextView) itemView.findViewById(R.id.detailBookAddingBooksAuthor);
        }
    }

}

