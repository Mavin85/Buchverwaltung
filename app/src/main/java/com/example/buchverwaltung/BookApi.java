package com.example.buchverwaltung;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookApi {

    // url beispiel
    // https://www.googleapis.com/books/v1/volumes?q=isbn0735619670

    @GET("books/v1/volumes")
    Call<BookApiResult> getABook(@Query("q") String isbn);
}
