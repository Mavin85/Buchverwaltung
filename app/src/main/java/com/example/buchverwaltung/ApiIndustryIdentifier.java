package com.example.buchverwaltung;

import com.google.gson.annotations.SerializedName;

public class ApiIndustryIdentifier {
    @SerializedName("identifier")
    private String isbn;
    @SerializedName("type")
    private String type;

    public ApiIndustryIdentifier(String isbn, String type) {
        this.isbn = isbn;
        this.type = type;
    }

    public ApiIndustryIdentifier(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
