package com.example.buchverwaltung;

import com.google.gson.annotations.SerializedName;

public class ApiIndustryIdentifier {
    @SerializedName("identifier")
    private String isbn;

    public ApiIndustryIdentifier(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

}
