package com.example.buchverwaltung;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMapper {

    String foo;
    @SerializedName("items")
    List<ApiResponseBook> book;
}
