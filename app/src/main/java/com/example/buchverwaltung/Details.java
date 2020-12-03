package com.example.buchverwaltung;

import java.util.List;

public class Details {
    private List<Author> authors;
    private String title;
    private String subtitle;


    public Details(List<Author> authors, String title, String subtitle) {
        this.authors = authors;
        this.title = title;
        this.subtitle = subtitle;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
