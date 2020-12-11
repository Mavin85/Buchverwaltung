package com.example.buchverwaltung;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAddingAdapter extends RecyclerView.Adapter<BookAddingAdapter.BookAddingViewHolder> {
    List<Book> bookList;
    Context context;
    DataBaseHelper dataBaseHelper;
    Activity parentActivity;

    public BookAddingAdapter(List<Book> bookList, Context context, Activity parentActivity) {
        this.bookList = bookList;
        this.context = context;
        this.parentActivity = parentActivity;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(parentActivity)
                        .setTitle(R.string.dialogAddBook)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // add the book to the database
                                dataBaseHelper = new DataBaseHelper(context);
                                dataBaseHelper.addBook(b);
                                if (b.getCoverString() != null) {
                                    // store the cover
                                    Picasso.get().load(b.getCoverString()).into(DetailActivityBookAdding.picassoImageTarget(context, dataBaseHelper.getBookByTitle(b.getTitle()).getId() + "_cover.jpeg"));
                                }
                                Intent i = new Intent(context, MainActivity.class);
                                //context.startActivity(i);
                                parentActivity.startActivity(i);
                            }})
                        .setNegativeButton(android.R.string.cancel, null).show();
            }
        });

        if (b.getCoverInt() == 0) {
            holder.b_cover.setImageResource(R.drawable.bookexamplecover);
        }
        else {
            Picasso.get().load(b.getCoverString()).into(holder.b_cover);
        }
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

