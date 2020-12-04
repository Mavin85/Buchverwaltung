package com.example.buchverwaltung;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMapper {

    @SerializedName("items")
    private List<ApiResponseBook> book;

    public List<ApiResponseBook> getBook() {return book;}
}
