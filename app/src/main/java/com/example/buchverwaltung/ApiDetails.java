package com.example.buchverwaltung;

import java.util.List;

public class ApiDetails {

    private String title;
    private String subtitle;
    private List<String> authors;
    private ApiImageLink imageLinks;

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

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public ApiImageLink getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ApiImageLink imageLinks) {
        this.imageLinks = imageLinks;
    }

    public ApiDetails(String title, String subtitle, List<String> authors, ApiImageLink imageLinks) {
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.imageLinks = imageLinks;
    }
}
