package com.example.buchverwaltung;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    ArrayList<Book> bookList = new ArrayList<>();

    public BookAdapter(ArrayList<Book> bookList) {
        this.bookList = bookList;
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

        holder.b_cover.setImageResource(b.getCover());
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
