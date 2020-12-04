package com.example.buchverwaltung;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse {
    List<ApiResponseBook> bookList;

    public ApiResponse(){
        bookList = new ArrayList<ApiResponseBook>();
    }

    public static ApiResponse parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        ApiResponse apiResponse = gson.fromJson(response, ApiResponse.class);
        return apiResponse;
    }
}
