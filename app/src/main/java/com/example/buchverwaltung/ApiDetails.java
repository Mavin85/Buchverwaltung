package com.example.buchverwaltung;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiDetails {

    @SerializedName("title")
    private String title;
    @SerializedName("authors")
    private List<String> authors;
    @SerializedName("imageLinks")
    private ApiImageLink imageLinks;
    @SerializedName("industryIdentifiers")
    private List<ApiIndustryIdentifier> industryIdentifiers;

    public ApiDetails(String title, List<String> authors, ApiImageLink imageLinks, List<ApiIndustryIdentifier> industryIdentifiers) {
        this.title = title;
        this.authors = authors;
        this.imageLinks = imageLinks;
        this.industryIdentifiers = industryIdentifiers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<ApiIndustryIdentifier> getIndustryIdentifiers() {
        return industryIdentifiers;
    }

    public void setIndustryIdentifiers(List<ApiIndustryIdentifier> industryIdentifiers) {
        this.industryIdentifiers = industryIdentifiers;
    }
}
