package com.example.buchverwaltung;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    List<Book> bookList;
    Context context;

    public BookAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
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
                Intent i = new Intent(context, DetailActivityBook.class);
                i.putExtra("id", b.getId());
                context.startActivity(i);
            }
        });

        // load default cover
        if(b.getCoverInt() == 0) {
            holder.b_cover.setImageResource(R.drawable.bookexamplecover);
        }
        else {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("coverDir", Context.MODE_PRIVATE);
            File myImageFile = new File(directory, b.getId() + "_cover.jpeg");
            Picasso.get().load(myImageFile).into(holder.b_cover);
        }

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
