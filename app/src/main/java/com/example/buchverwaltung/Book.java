package com.example.buchverwaltung;

public class Book {

    private int id;
    private String isbn;
    private String title;
    private String author;
    private boolean favourite;
    private String coverString;
    private int coverInt;
    private String comment;

    public Book(int id, String isbn, String title, String author, boolean favourite, int coverInt, String comment) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.favourite = favourite;
        this.coverInt = coverInt;
        this.comment = comment;
    }


    // small constructor for testing
    public Book(String title, String isbn) {
        this.title = title;
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", favourite=" + favourite +
                ", coverString='" + coverString + '\'' +
                ", coverInt=" + coverInt +
                ", comment='" + comment + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String title) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public int getCoverInt() {
        return coverInt;
    }

    public void setCoverInt(int cover) {
        this.coverInt = cover;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
