package com.example.buchverwaltung;

import com.google.gson.annotations.SerializedName;

public class ApiResponseBook {

    private String isbn;
    @SerializedName("volumeInfo")
    private ApiDetails apiDetails;

    public ApiResponseBook(String isbn, ApiDetails volumeInfo) {
        this.isbn = isbn;
        this.apiDetails = volumeInfo;
    }

    @Override
    public String toString() {
        return "ApiResponseBook{" +
                "isbn='" + isbn + '\'' +
                ", apiDetails=" + apiDetails +
                '}';
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public ApiDetails getApiDetails() {
        return apiDetails;
    }

    public void setApiDetails(ApiDetails apiDetails) {
        this.apiDetails = apiDetails;
    }

}
