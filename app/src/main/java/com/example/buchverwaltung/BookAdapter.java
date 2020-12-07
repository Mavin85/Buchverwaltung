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

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    List<Book> bookList;
    Context con;

    public BookAdapter(List<Book> bookList, Context con) {
        this.bookList = bookList;
        this.con = con;
    }

    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mainbooks, parent, false);
        BookViewHolder vh = new BookViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookViewHolder holder, int position) {
        Book b = bookList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(con, DetailActivityBook.class);
                i.putExtra("id", b.getId());
                con.startActivity(i);
            }
        });

        // load cover
        ContextWrapper cw = new ContextWrapper(con);
        File directory = cw.getDir("coverDir", Context.MODE_PRIVATE);
        File myImageFile = new File(directory, b.getIsbn() + "_cover.jpeg");
        Picasso.get().load(myImageFile).into(holder.b_cover);

        holder.b_title.setText(b.getTitle());
        holder.b_author.setText(b.getAuthor());
        holder.b_isbn.setText(b.getIsbn());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder{
        ImageView b_cover;
        TextView b_title;
        TextView b_author;
        TextView b_isbn;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            b_cover = (ImageView) itemView.findViewById(R.id.mainbooksCover);
            b_title = (TextView) itemView.findViewById(R.id.mainbooksBookTitle);
            b_author = (TextView) itemView.findViewById(R.id.mainbooksBookAuthor);
            b_isbn = (TextView) itemView.findViewById(R.id.mainbooksIsbn);
        }
    }

}
