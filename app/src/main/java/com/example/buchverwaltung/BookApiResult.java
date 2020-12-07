package com.example.buchverwaltung;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookApiResult {

    @SerializedName("items")
    private List<ApiResponseBook> book;

    @SerializedName("totalItems")
    private String totalItems;

    public List<ApiResponseBook> getBook() {return book;}

    public void setBook(List<ApiResponseBook> book) {
        this.book = book;
    }

    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }
}
