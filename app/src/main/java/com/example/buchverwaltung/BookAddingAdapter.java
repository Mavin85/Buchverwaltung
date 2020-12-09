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

        holder.b_title.setText(b.getTitle());
        holder.b_author.setText(b.getAuthor());
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

